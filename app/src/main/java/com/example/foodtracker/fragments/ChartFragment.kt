/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.foodtracker.fragments

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.applandeo.materialcalendarview.EventDay
import com.example.foodtracker.*
import com.example.foodtracker.database.foodlog.FoodlogDao
import com.example.foodtracker.database.foodlog.Type
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.databinding.ChartFragmentBinding
import com.example.foodtracker.viewmodels.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ChartFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ChartFragmentBinding? = null

    private val binding get() = _binding!!

    private val notes = mutableMapOf<EventDay, String>()

    private lateinit var period: Period
    private lateinit var periodDao: PeriodDao
    private lateinit var complaint: Complaint
    private lateinit var complaintDao : ComplaintDao
    private lateinit var foodlogDao: FoodlogDao
    private lateinit var foodlogRepository: FoodlogRepository

    private lateinit var stopName: String
    private var categoryId: Int = 0
    private val MAX_X_VALUE = 12
    private val GROUPS = 3
    private val GROUP_1_LABEL = "Orders"
    private val GROUP_2_LABEL = ""
    private val BAR_SPACE = 0.1f
    private val BAR_WIDTH = 0.8f
    private var chart: BarChart? = null
    private var pieChart: PieChart? = null
    protected var tfRegular: Typeface? = null
    protected var tfLight: Typeface? = null

    private val statValues: ArrayList<Float> = ArrayList()

    protected val statsTitles = arrayOf(
        "Orders", "Inventory"
    )
    private val foodlogViewModel: FoodlogViewModel by activityViewModels {
        foodlogDao = (activity?.application as FoodtrackerApplication).database.foodlogDao()
        periodDao = (activity?.application as FoodtrackerApplication).database.periodDao()
        complaintDao = (activity?.application as FoodtrackerApplication).database.complaintDao()
        foodlogRepository = FoodlogRepository(foodlogDao)
        FoodlogViewModelFactory(foodlogRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            stopName = it.getString(STOP_NAME).toString()
            categoryId = it.getInt("categoryId")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChartFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        chart = binding.chart //this is our barchart
        getBarStats()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getBarStats() {
        val values1: ArrayList<BarEntry> = ArrayList()
        statValues.clear()


//        for (i in 0 until MAX_X_VALUE) {
//            values1.add(
//                BarEntry(
//                    i.toFloat(),
//                    (Math.random() * 80).toFloat()
//                )
//            )
//        }
        lifecycle.coroutineScope.launch {
            val v = Array(12, { i -> 0 })
            foodlogViewModel.summary().collect() {
                var month = 0
//                var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                it.forEach() {
                    if (it.type == Type.Complaint) {
                        month = it.month
                        v[month] = it.count
                    }
                }
                v.forEachIndexed { month, value ->
                    values1.add(
                        BarEntry(
                            month.toFloat(),
                            value.toFloat()
                        )
                    )
                }
                displayData(values1)
            }
        }
//After preparing our data set, we need to display the data in our bar chart
            //displayData(values1)

    }

    private fun displayData(orderData: ArrayList<BarEntry>) {
        //var set1: BarDataSet
        val values: ArrayList<BarEntry> = ArrayList()
        val start = 1
        val range = 1f
        val count = 10

        // Voeg random wat sterren toe aan de barcharts
//        for (i in start  until count) {
//            val tmpValue = (Math.random() * (range + 1)).toFloat()
//
//            if (Math.random() * 100 < 25) {
//                values.add(BarEntry(i.toFloat(), tmpValue, resources.getDrawable(R.drawable.btn_star)))
//            } else {
//                values.add(BarEntry(i.toFloat(), tmpValue))
//            }
//        }


        var set1 = BarDataSet(orderData, "The year 2017")
        var set2 = BarDataSet(orderData, "The year 2018")
        var set3 = BarDataSet(orderData, "The year 2019")
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val data = BarData(dataSets)

//        val data = BarData(set1)
        configureBarChart()
        prepareChartData(data)
    }

//    private fun createChartData(orderData: ArrayList<BarEntry>): BarData {
//        var data: BarData
////        data!!.dataSets = BarData(1)
//        return data
//    }

    private fun prepareChartData(data: BarData) {
        chart!!.data = data
        chart!!.barData.barWidth = BAR_WIDTH
        val groupSpace = 1f - (BAR_SPACE + BAR_WIDTH) * GROUPS
        chart!!.groupBars(0f, groupSpace, BAR_SPACE)
        chart!!.invalidate()
    }
    private fun configureBarChart() {
        chart!!.setPinchZoom(false)
        chart!!.setDrawBarShadow(false)
        chart!!.setDrawGridBackground(false)

        chart!!.description.isEnabled = false
        val xAxis = chart!!.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        val leftAxis = chart!!.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f
        chart!!.axisRight.isEnabled = false
        chart!!.xAxis.axisMinimum = 1f
        chart!!.xAxis.axisMaximum = MAX_X_VALUE.toFloat()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // in this example, a LineChart is initialized from xml
       // val chart: LineChart = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.chart) as LineChart
        //val lineChart = view. .findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.chart) as LineChart

///        val calendarView = view.findViewById<com.applandeo.materialcalendarview.CalendarView>(R.id.calendarView) // get the reference of CalendarView
//        //val calendarView = view.findViewById<com.applandeo.materialcalendarview.extensions.CalendarGridView>(R.id.calendarView) // get the reference of CalendarView
//        calendarView.tooltipText = "Tooltip"
//        val events: MutableList<EventDay> = ArrayList()

//        lifecycle.coroutineScope.launch {
//            foodlogViewModel.fullConsume().collect() {
//                var foodlogList = it
//                it.forEach() {
//                    val startDate = LocalDate.parse(it.startdate,  DateTimeFormatter.ofPattern("d/M/yyyy"))
//                    var checkDate = it.startdate
//                    var cal = Calendar.getInstance()
//                    cal.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
//                    if (it.type == Type.Consume) {
//                        var eventDay = EventDay(cal, setIcon(foodlogList, checkDate))
//                        notes[eventDay] = it.item
//                    } else if (it.type == Type.Complaint) {
//                        complaint = complaintDao.getComplaint(it.id)
//                        val endDate = LocalDate.parse(complaint.enddatetime,  DateTimeFormatter.ofPattern("d/M/yyyy"))
//                        var date = startDate
//                        while (date <= endDate) {
//                            var cal2 = Calendar.getInstance()
//                            cal2.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
//                            cal2.set(date.year, date.monthValue-1, date.dayOfMonth);
//                            var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
//                            checkDate = date.format(formatter)
//                            var eventDay = EventDay(cal2, setIcon(foodlogList, checkDate))
//                            notes[eventDay] = it.item
//                            date=date.plusDays(1)
//                        }
//                    } else if (it.type == Type.Ontlasting) {
//                        var eventDay = EventDay(cal, setIcon(foodlogList, checkDate))
//                        notes[eventDay] = it.item
//                    } else if (it.type == Type.Period) {
//                        period = periodDao.getPeriod(it.id)
//                        val endDate = LocalDate.parse(period.enddatetime,  DateTimeFormatter.ofPattern("d/M/yyyy"))
//                        var date = startDate
//                        while (date <= endDate) {
//                            var cal2 = Calendar.getInstance()
//                            cal2.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
//                            cal2.set(date.year, date.monthValue-1, date.dayOfMonth);
//                            var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
//                            checkDate = date.format(formatter)
//                            var eventDay = EventDay(cal2, setIcon(foodlogList, checkDate))
//                            notes[eventDay] = it.item
//                            date=date.plusDays(1)
//                        }
//                    }
//                }
//                calendarView.setEvents(notes.keys.toList())
//            }
//        }
//        binding.calendarView.setOnDayClickListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
