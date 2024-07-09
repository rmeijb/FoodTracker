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
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.databinding.ComplaintFragmentBinding
import com.example.foodtracker.databinding.ComplaintUpdateFragmentBinding
import com.example.foodtracker.databinding.ConsumeFragmentBinding
import com.example.foodtracker.viewmodels.ComplaintViewModel
import com.example.foodtracker.viewmodels.ComplaintViewModelFactory
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory
import com.google.android.material.textview.MaterialTextView

class ComplaintUpdateFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ComplaintUpdateFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var complaint: Complaint
    private lateinit var dao : ComplaintDao
    private lateinit var repository: ComplaintRepository
    lateinit var datePickerDialog: DatePickerDialog


    private val viewModel: ComplaintViewModel by activityViewModels {
        dao = (activity?.application as FoodtrackerApplication).database.complaintDao()
        repository = ComplaintRepository(dao)
        ComplaintViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            complaint = (it.getSerializable("complaint") as Complaint)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ComplaintUpdateFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val complaintSpinner: Spinner = view.findViewById(R.id.complaint_spinner)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.severeness_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setSelection(complaint.severeness);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.complaint_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            complaintSpinner.adapter = adapter
            complaintSpinner.setSelection(adapter.getPosition(complaint.symptom));
        }

        val startdate = view.findViewById<EditText>(R.id.startdate_edittext)

        val enddate = view.findViewById<EditText>(R.id.enddate_edittext)
        val description = view.findViewById<EditText>(R.id.description_TextMultiLine)
        description.setText(complaint.description)

        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        startdate.setText(complaint.startdatetime)
        enddate.setText(complaint.enddatetime)
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
        button.setOnClickListener {
            val severeness = spinner.selectedItemPosition
            val complaint_text =
                complaintSpinner?.getItemAtPosition(complaintSpinner.selectedItemPosition)
                    .toString()
            complaint.startdatetime = startdate.text.toString()
            complaint.enddatetime = enddate.text.toString()
            complaint.description = description.text.toString()
            complaint.symptom = complaint_text
            complaint.severeness = severeness
            viewModel.updateComplaint(complaint)
            view.findNavController().navigateUp()
        }
        val cancelButton: Button = view.findViewById(R.id.cancel)
        cancelButton.setOnClickListener {
            // Code here executes on main thread after user presses button
            //finish()
            view.findNavController().navigateUp()
        }
        val deleteButton: Button = view.findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            viewModel.deleteComplaint(complaint)
            view.findNavController().navigateUp()
        }
    }


    //override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    //    val text: String = parent?.getItemAtPosition(position).toString()
    //    complaint_text = text
    //}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
