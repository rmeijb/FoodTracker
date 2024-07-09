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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.FoodlogAdapter
import com.example.foodtracker.ConsumeRepository
import com.example.foodtracker.FoodtrackerApplication
import com.example.foodtracker.R
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.databinding.ConsumeFragmentBinding
import com.example.foodtracker.databinding.ConsumeUpdateFragmentBinding
import com.example.foodtracker.viewmodels.ConsumeViewModel
import com.example.foodtracker.viewmodels.ConsumeViewModelFactory
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory

class ConsumeUpdateFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ConsumeUpdateFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var consume: Consume
    private lateinit var dao : ConsumeDao
    private lateinit var repository: ConsumeRepository
    lateinit var datePickerDialog: DatePickerDialog


    private val viewModel: ConsumeViewModel by activityViewModels {
        dao = (activity?.application as FoodtrackerApplication).database.consumeDao()
        repository = ConsumeRepository(dao)
        ConsumeViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            consume = (it.getSerializable("consume") as Consume)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ConsumeUpdateFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val productlabel = view.findViewById<TextView>(R.id.productName)
        productlabel.text = consume.productName
        val unitlabel = view.findViewById<TextView>(R.id.product_unit_textView)
        unitlabel.text = consume.unit
        val quantity = view.findViewById<EditText>(R.id.editTextNumber)
        quantity.setText(consume.quantity.toString())
        val pill = view.findViewById<EditText>(R.id.editTextPill)
        pill.setText(consume.pill.toString())
        // initiate the date picker and a button
        val date = view.findViewById<TextView>(R.id.date)
        //date.setEnabled(false);
        val c: Calendar = Calendar.getInstance()
        val mYear: Int = c.get(Calendar.YEAR) // current year
        val mMonth: Int = c.get(Calendar.MONTH) // current month
        val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
        // perform click event on edit text
        date.setText(consume.datetime)
        date.setOnClickListener { // calender class's instance and get current date , month and year from calender
            // date picker dialog
            datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
//                    date.setText(
//                        dayOfMonth.toString() + "/"
//                                + (monthOfYear + 1) + "/" + year
//                    )
                    date.setText(consume.datetime)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        val button: Button = view.findViewById(R.id.opslaan)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val quantityDouble : Double = quantity.text.toString().toDouble()
                //viewModel.updateConsume(consume = Consume(id = 0, productId, productName, date.text.toString(),unit, quantityDouble))
                consume.datetime = date.text.toString()
                consume.quantity = quantityDouble
                consume.pill = pill.text.toString().toInt()

                viewModel.updateConsume(consume)
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
        val deleteButton: Button = view.findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener (object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewModel.deleteConsume(consume)
                view.findNavController().navigateUp()
            }
        })

        val foodlogAdapter = FoodlogAdapter({})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
