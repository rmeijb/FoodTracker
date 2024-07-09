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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.FoodlogAdapter
import com.example.foodtracker.FoodlogRepository
import com.example.foodtracker.FoodtrackerApplication
import com.example.foodtracker.database.foodlog.FoodlogDao
import com.example.foodtracker.database.foodlog.Type
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.database.ontlasting.OntlastingDao
import com.example.foodtracker.database.period.PeriodDao
import com.example.foodtracker.databinding.FoodlogFragmentBinding
import com.example.foodtracker.viewmodels.FoodlogViewModel
import com.example.foodtracker.viewmodels.FoodlogViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FoodlogFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: FoodlogFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var repository: FoodlogRepository
    private lateinit var foodlogDao : FoodlogDao
    private lateinit var consumeDao : ConsumeDao
    private lateinit var complaintDao : ComplaintDao
    private lateinit var ontlastingDao : OntlastingDao
    private lateinit var periodDao : PeriodDao

    private lateinit var stopName: String
    private lateinit var unit: String
    private var categoryId: Int = 0

    private val viewModel: FoodlogViewModel by activityViewModels {
        consumeDao = (activity?.application as FoodtrackerApplication).database.consumeDao()
        complaintDao = (activity?.application as FoodtrackerApplication).database.complaintDao()
        ontlastingDao = (activity?.application as FoodtrackerApplication).database.ontlastingDao()
        periodDao = (activity?.application as FoodtrackerApplication).database.periodDao()
        foodlogDao = (activity?.application as FoodtrackerApplication).database.foodlogDao()
        repository = FoodlogRepository(foodlogDao)
        FoodlogViewModelFactory(repository)
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
        _binding = FoodlogFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Adds a [DividerItemDecoration] between items
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        val foodlogAdapter = FoodlogAdapter {
            if (it.type == Type.Consume) {
                val action = FoodlogFragmentDirections
                    .actionFoodlogFragmentToConsumeUpdateFragment(
                        consume = consumeDao.getConsume(it.id)
                    )
                view.findNavController().navigate(action)
            } else if (it.type == Type.Complaint) {
                val action = FoodlogFragmentDirections
                    .actionFoodlogFragmentToComplaintUpdateFragment(
                        complaint = complaintDao.getComplaint(it.id)
                    )
                view.findNavController().navigate(action)
            } else if (it.type == Type.Ontlasting) {
                val action = com.example.foodtracker.fragments.FoodlogFragmentDirections
                    .actionFoodlogFragmentToOntlastingUpdateFragment(
                        ontlasting = ontlastingDao.getOntlasting(it.id)
                    )
                view.findNavController().navigate(action)
            } else if (it.type == Type.Period) {
                val action = com.example.foodtracker.fragments.FoodlogFragmentDirections
                    .actionFoodlogFragmentToPeriodUpdateFragment(
                        period = periodDao.getPeriod(it.id)
                    )
                view.findNavController().navigate(action)
            }

        }
        // by passing in the stop name, filtered results are returned,
        // and tapping rows won't trigger navigation
        recyclerView.adapter = foodlogAdapter
        lifecycle.coroutineScope.launch {
        //    viewModel.fullSchedule().collect() {
        //        productAdapter.submitList(it)
            viewModel.fullConsume().collect() {
                foodlogAdapter.submitList(it.sortedBy { LocalDate.parse(it.startdate,  DateTimeFormatter.ofPattern("d/M/yyyy")) })


                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
