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

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.foodtracker.*
import com.example.foodtracker.database.foodlog.Foodlog
import com.example.foodtracker.database.foodlog.FoodlogDao
import com.example.foodtracker.database.foodlog.Type
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.databinding.CalendarFragmentBinding
import com.example.foodtracker.viewmodels.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment: Fragment(), OnDayClickListener {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: CalendarFragmentBinding? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendarView = view.findViewById<com.applandeo.materialcalendarview.CalendarView>(R.id.calendarView) // get the reference of CalendarView
        //val calendarView = view.findViewById<com.applandeo.materialcalendarview.extensions.CalendarGridView>(R.id.calendarView) // get the reference of CalendarView
        calendarView.tooltipText = "Tooltip"
        val events: MutableList<EventDay> = ArrayList()

        lifecycle.coroutineScope.launch {
            foodlogViewModel.fullConsume().collect() {
                var foodlogList = it
                it.forEach() {
                    val startDate = LocalDate.parse(it.startdate,  DateTimeFormatter.ofPattern("d/M/yyyy"))
                    var checkDate = it.startdate
                    var cal = Calendar.getInstance()
                    cal.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
                    if (it.type == Type.Consume) {
                        var eventDay = EventDay(cal, setIcon(foodlogList, checkDate))
                        notes[eventDay] = it.item
                    } else if (it.type == Type.Complaint) {
                        complaint = complaintDao.getComplaint(it.id)
                        val endDate = LocalDate.parse(complaint.enddatetime,  DateTimeFormatter.ofPattern("d/M/yyyy"))
                        var date = startDate
                        while (date <= endDate) {
                            var cal2 = Calendar.getInstance()
                            cal2.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
                            cal2.set(date.year, date.monthValue-1, date.dayOfMonth);
                            var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                            checkDate = date.format(formatter)
                            var eventDay = EventDay(cal2, setIcon(foodlogList, checkDate))
                            notes[eventDay] = it.item
                            date=date.plusDays(1)
                        }
                    } else if (it.type == Type.Ontlasting) {
                        var eventDay = EventDay(cal, setIcon(foodlogList, checkDate))
                        notes[eventDay] = it.item
                    } else if (it.type == Type.Period) {
                        period = periodDao.getPeriod(it.id)
                        val endDate = LocalDate.parse(period.enddatetime,  DateTimeFormatter.ofPattern("d/M/yyyy"))
                        var date = startDate
                        while (date <= endDate) {
                            var cal2 = Calendar.getInstance()
                            cal2.set(startDate.year, startDate.monthValue-1, startDate.dayOfMonth);
                            cal2.set(date.year, date.monthValue-1, date.dayOfMonth);
                            var formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                            checkDate = date.format(formatter)
                            var eventDay = EventDay(cal2, setIcon(foodlogList, checkDate))
                            notes[eventDay] = it.item
                            date=date.plusDays(1)
                        }
                    }
                }
                calendarView.setEvents(notes.keys.toList())
            }
        }
        binding.calendarView.setOnDayClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setIcon(foodlogList: List<Foodlog>, date: String): Int {
        var icon: Int = 0
        var iconType: Int = 0
        if (foodlogList.filter { it.type == Type.Consume }.filter { LocalDate.parse(it.startdate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.filter{LocalDate.parse(it.enddate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.isNotEmpty()) {
             iconType += R.drawable.cutlerygreen
        }
        if (foodlogList.filter { it.type == Type.Complaint }.filter { LocalDate.parse(it.startdate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.filter{LocalDate.parse(it.enddate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.isNotEmpty()) {
            iconType += R.drawable.error
        }
        if (foodlogList.filter { it.type == Type.Ontlasting }.filter { LocalDate.parse(it.startdate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.filter{LocalDate.parse(it.enddate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.isNotEmpty()) {
            iconType += R.drawable.ontlasting
        }
        if (foodlogList.filter { it.type == Type.Period }.filter { LocalDate.parse(it.startdate,DateTimeFormatter.ofPattern("d/M/yyyy")) <= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.filter{LocalDate.parse(it.enddate,DateTimeFormatter.ofPattern("d/M/yyyy")) >= LocalDate.parse(date,DateTimeFormatter.ofPattern("d/M/yyyy")) }.isNotEmpty()) {
            iconType += R.drawable.period
        }
        when (iconType) {
            R.drawable.cutlerygreen ->  icon = R.drawable.cutlerygreen
            R.drawable.error ->  icon = R.drawable.error
            R.drawable.ontlasting ->  icon = R.drawable.ontlasting
            R.drawable.period ->  icon = R.drawable.period
            R.drawable.cutlerygreen+R.drawable.error+R.drawable.ontlasting+R.drawable.period -> icon = R.drawable.ceop
            R.drawable.cutlerygreen+R.drawable.error+R.drawable.ontlasting -> icon = R.drawable.ceo
            R.drawable.cutlerygreen+R.drawable.error -> icon = R.drawable.ce
            R.drawable.cutlerygreen+R.drawable.ontlasting+R.drawable.period -> icon = R.drawable.cop
            R.drawable.cutlerygreen+R.drawable.error+R.drawable.period -> icon = R.drawable.cep
            R.drawable.cutlerygreen+R.drawable.period -> icon = R.drawable.cp
            R.drawable.error+R.drawable.ontlasting+R.drawable.period -> icon = R.drawable.eop
            R.drawable.error+R.drawable.ontlasting -> icon = R.drawable.eo
            R.drawable.error+R.drawable.period -> icon = R.drawable.ep
            R.drawable.ontlasting+R.drawable.period -> icon = R.drawable.op
        }
        return icon
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDayClick(eventDay: EventDay) {
        val supportFragmentManager: FragmentTransaction = parentFragmentManager.beginTransaction()
        // create dialog fragment
        val dialog = MyCustomDialog()

        // optionally pass arguments to the dialog fragment
        val args = Bundle()
        val sdfcur = SimpleDateFormat("yyyy-MM-dd")
        val date = eventDay.calendar.time
        val beginString = sdfcur.format(date)

        args.putString("date", beginString)
        dialog.setArguments(args)

        dialog.show(supportFragmentManager, "MyCustomFragment")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
