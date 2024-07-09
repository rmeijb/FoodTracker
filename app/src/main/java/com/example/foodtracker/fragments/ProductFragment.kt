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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.*
import com.example.foodtracker.database.product.ProductDao
import com.example.foodtracker.databinding.ProductFragmentBinding
import com.example.foodtracker.viewmodels.ProductViewModel
import com.example.foodtracker.viewmodels.ProductViewModelFactory
import kotlinx.coroutines.launch

class ProductFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ProductFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private var categoryId: Int = 0

    private lateinit var dao : ProductDao
    private lateinit var productRepository: ProductRepository

    private val viewModel: ProductViewModel by activityViewModels {
       /** ProductViewModelFactory(
            (activity?.application as FoodtrackerApplication).database.productDao()
        )
       */
        dao = (activity?.application as FoodtrackerApplication).database.productDao()
        productRepository = ProductRepository(dao)
        ProductViewModelFactory(productRepository)
    }
    /** private val viewModel: CategoryViewModel by activityViewModels {
        FoodTrackerViewModelFactory(
            (activity?.application as FoodtrackerApplication).database.categoryDao()
        )
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
           categoryId = it.getInt("categoryId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Adds a [DividerItemDecoration] between items
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        //val productAdapter = ProductAdapter({})
        //val productAdapter = ProductAdapter({
        val productAdapter = ProductAdapter({
            val action = ProductFragmentDirections
                .actionProductFragmentToConsumeFragment(
                productName = it.productName,
                productId = it.id,
                unit = it.unit
            )
            view.findNavController().navigate(action)
        })
        val button: Button = view.findViewById(R.id.toevoegen)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //val quantityDouble : Double = quantity.text.toString().toDouble()
                //viewModel.updateConsume(consume = Consume(id = 0, productId, productName, date.text.toString(),unit, quantityDouble))
                //consume.datetime = date.text.toString()
                //consume.quantity = quantityDouble
                //viewModel.updateConsume(consume)
                //val action = ProductFragmentDirections
                //    .actionProductFragmentToProductUpdateFragment( category = , it.categoryId = it.c)
                //view.findNavController().navigate(action)
               // val productAdapter = ProductAdapter({
                    val action = ProductFragmentDirections
                        .actionProductFragmentToProductUpdateFragment(
                            category = "category",
                            categoryId = categoryId
                        )
                    view.findNavController().navigate(action)
                //})
            }
        })
        // by passing in the stop name, filtered results are returned,
        // and tapping rows won't trigger navigation
        recyclerView.adapter = productAdapter
        lifecycle.coroutineScope.launch {
        //    viewModel.fullSchedule().collect() {
        //        productAdapter.submitList(it)
            viewModel.fullSchedule(categoryId)?.collect() {
                productAdapter.submitList(it)

                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
