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
import com.example.foodtracker.FoodtrackerApplication
import com.example.foodtracker.ProductAdapter
import com.example.foodtracker.ProductRepository
import com.example.foodtracker.R
import com.example.foodtracker.database.product.Product
import com.example.foodtracker.database.product.ProductDao
import com.example.foodtracker.databinding.ProductFragmentBinding
import com.example.foodtracker.databinding.ProductUpdateFragmentBinding
import com.example.foodtracker.viewmodels.*

class ProductUpdateFragment: Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: ProductUpdateFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var category: String
    private var categoryId: Int = 0
    private lateinit var dao : ProductDao
    private lateinit var repository: ProductRepository

    private val viewModel: ProductViewModel by activityViewModels {
        //val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        dao = (activity?.application as FoodtrackerApplication).database.productDao()
        repository = ProductRepository(dao)
        //val factory = ConsumeViewModelFactory(repository)
        //subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        ProductViewModelFactory(repository)
            //(activity?.application as FoodtrackerApplication).database.consumeDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            category = it.getString("category").toString()
            categoryId = it.getInt("categoryId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductUpdateFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recyclerView = binding.recyclerView
        //recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val button: Button = view.findViewById(R.id.newproductOpslaan)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val unit = view.findViewById<TextView>(R.id.unitInputEditText)
                val product = view.findViewById<TextView>(R.id.productnaamInputEditText)
                viewModel.insertProduct(product = Product(id = 0, product.text.toString(), categoryId, category, unit.text.toString()))
                view.findNavController().navigateUp()
            }
        })
        val cancelButton: Button = view.findViewById(R.id.newproductCancel)
        cancelButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Code here executes on main thread after user presses button
                //finish()
                view.findNavController().navigateUp()
            }
        })
        val productAdapter = ProductAdapter({})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
