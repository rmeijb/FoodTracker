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
import com.example.foodtracker.database.ontlasting.Ontlasting
import com.example.foodtracker.database.ontlasting.OntlastingDao
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.databinding.OntlastingFragmentBinding
import com.example.foodtracker.databinding.ConsumeFragmentBinding
import com.example.foodtracker.databinding.OntlastingUpdateFragmentBinding
import com.example.foodtracker.viewmodels.OntlastingViewModel
import com.example.foodtracker.viewmodels.OntlastingViewModelFactory
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory
import com.google.android.material.textview.MaterialTextView

class OntlastingUpdateFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: OntlastingUpdateFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var ontlasting: Ontlasting
    private lateinit var dao : OntlastingDao
    private lateinit var repository: OntlastingRepository
    lateinit var datePickerDialog: DatePickerDialog


    private val viewModel: OntlastingViewModel by activityViewModels {
        dao = (activity?.application as FoodtrackerApplication).database.ontlastingDao()
        repository = OntlastingRepository(dao)
        OntlastingViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ontlasting = (it.getSerializable("ontlasting") as Ontlasting)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OntlastingUpdateFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // initiate the date picker and a button
        val spinner: Spinner = view.findViewById(R.id.ontlastingSpinner)
        val startdate = view.findViewById<EditText>(R.id.startdate_edittext)

        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        startdate.setText(ontlasting.startdatetime)

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

        val button: Button = view.findViewById(R.id.opslaan)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val ontlasting_text = spinner?.getItemAtPosition(spinner.selectedItemPosition).toString()
                ontlasting.startdatetime = startdate.text.toString()
                ontlasting.type = ontlasting_text
                viewModel.updateOntlasting(ontlasting)
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
                viewModel.deleteOntlasting(ontlasting)
               view.findNavController().navigateUp()
            }
        })
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ontlasting_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
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
