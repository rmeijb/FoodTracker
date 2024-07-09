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
import com.example.foodtracker.databinding.ConsumeFragmentBinding
import com.example.foodtracker.viewmodels.ComplaintViewModel
import com.example.foodtracker.viewmodels.ComplaintViewModelFactory
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory
import com.google.android.material.textview.MaterialTextView

class ComplaintFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ComplaintFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var productName: String
    private lateinit var unit: String
    private var productId: Int = 0
    private lateinit var complaint_text : String

    private lateinit var dao : ComplaintDao
    private lateinit var repository: ComplaintRepository
    lateinit var datePickerDialog: DatePickerDialog


    private val viewModel: ComplaintViewModel by activityViewModels {
        //val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        dao = (activity?.application as FoodtrackerApplication).database.complaintDao()
        repository = ComplaintRepository(dao)
        //val factory = ConsumeViewModelFactory(repository)
        //subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        ComplaintViewModelFactory(repository)
            //(activity?.application as FoodtrackerApplication).database.consumeDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
//            productName = it.getString("productName").toString()
//            productId = it.getInt("productId")
//            unit = it.getString("unit").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ComplaintFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // initiate the date picker and a button
        val complaintSpinner: Spinner = view.findViewById(R.id.complaint_spinner)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val startdate = view.findViewById<EditText>(R.id.startdate_edittext)
        val enddate = view.findViewById<EditText>(R.id.enddate_edittext)
        val description = view.findViewById<EditText>(R.id.description_TextMultiLine)
        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        startdate.setText(
            mDay.toString() + "/"
                    + (mMonth + 1) + "/" + mYear
        )
        enddate.setText(
            mDay.toString() + "/"
                    + (mMonth + 1) + "/" + mYear
        )
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
                val severeness = spinner.selectedItemPosition
                //val severeness = spinner?.getItemAtPosition(spinner.selectedItemPosition)
                val complaint_text = complaintSpinner?.getItemAtPosition(complaintSpinner.selectedItemPosition).toString()
                //val complaint_text =  spinner.getSelectedItem().toString();
                viewModel.insertComplaint(complaint = Complaint(id = 0, complaint_text , startdate.text.toString(),enddate.text.toString(),severeness, description.text.toString()))
                view.findNavController().navigateUp()
            }
        })
        val cancelButton: Button = view.findViewById(R.id.cancel)
        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Code here executesy on main thread after user presses button
                //finish()
                view.findNavController().navigateUp()
            }
        })

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
        }
        //val complaintAdapter = ComplaintAdapter({})
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
