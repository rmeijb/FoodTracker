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

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.*
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.databinding.PeriodFragmentBinding
import com.example.foodtracker.databinding.PeriodUpdateFragmentBinding
import com.example.foodtracker.viewmodels.PeriodViewModel
import com.example.foodtracker.viewmodels.PeriodViewModelFactory

class PeriodUpdateFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: PeriodUpdateFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var period: Period
    private lateinit var dao : PeriodDao
    private lateinit var repository: PeriodRepository
    lateinit var datePickerDialog: DatePickerDialog


    private val viewModel: PeriodViewModel by activityViewModels {
        dao = (activity?.application as FoodtrackerApplication).database.periodDao()
        repository = PeriodRepository(dao)
        PeriodViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            period = (it.getSerializable("period") as Period)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeriodUpdateFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // initiate the date picker and a button
        val startdate = view.findViewById<EditText>(R.id.startdate_edittext)
        val enddate = view.findViewById<EditText>(R.id.enddate_edittext)
        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        startdate.setText(period.startdatetime)
        enddate.setText(period.enddatetime)

        // perform click event on edit text
        startdate.setOnClickListener { // calender class's instance and get current date , month and year from calender
            // date picker dialog
            datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                    startdate.setText(
                        dayOfMonth.toString() + "/"
                                + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        // perform click event on edit text
        enddate.setOnClickListener { // calender class's instance and get current date , month and year from calender
            // date picker dialog
            datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                    enddate.setText(
                        dayOfMonth.toString() + "/"
                                + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        val button: Button = view.findViewById(R.id.opslaan)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                period.startdatetime = startdate.text.toString()
                period.enddatetime = enddate.text.toString()
                viewModel.updatePeriod(period)
                view.findNavController().navigateUp()
            }
        })
        val cancelButton: Button = view.findViewById(R.id.cancel)
        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Code here executes on main thread after user presses button
                //finish()
                view.findNavController().navigateUp()
            }
        })
        val deleteButton: Button = view.findViewById(R.id.delete)
        deleteButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewModel.deletePeriod(period)
                view.findNavController().navigateUp()
            }
        })

    }

    //override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    //    val text: String = parent?.getItemAtPosition(position).toString()
    //    Period_text = text
    //}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
