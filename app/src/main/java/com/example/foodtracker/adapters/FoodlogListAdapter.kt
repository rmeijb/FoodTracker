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
package com.example.foodtracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.databinding.ListConsumeItemBinding
import com.example.foodtracker.database.foodlog.Foodlog
import com.example.foodtracker.database.foodlog.Type

class FoodlogAdapter(
    private val onItemClicked: (Foodlog) -> Unit
) : ListAdapter<Foodlog, FoodlogAdapter.FoodlogViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Foodlog>() {
            override fun areItemsTheSame(oldItem: Foodlog, newItem: Foodlog): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Foodlog, newItem: Foodlog): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodlogViewHolder {
        val viewHolder = FoodlogViewHolder(
            ListConsumeItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FoodlogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodlogViewHolder(
        private var binding: ListConsumeItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
       // @SuppressLint("SimpleDateFormat")
        fun bind(foodlog: Foodlog) {
           // Pills label and values are not visible by default. Only make them visible when a pill is taken
           binding.pills.visibility = View.INVISIBLE
           binding.pillLabel.visibility = View.INVISIBLE
           if (foodlog.type == Type.Consume) {
               binding.imageView.setImageResource(R.drawable.cutlerygreen)
               if (foodlog.pill>0) {
                   binding.pills.setText(foodlog.pill.toString())
                   binding.pills.visibility = View.VISIBLE
                   binding.pillLabel.visibility = View.VISIBLE
               }
           } else if (foodlog.type == Type.Complaint) {
               binding.imageView.setImageResource(R.drawable.error)
           } else if (foodlog.type == Type.Ontlasting) {
               binding.imageView.setImageResource(R.drawable.ontlasting)
           } else if (foodlog.type == Type.Period) {
               binding.imageView.setImageResource(R.drawable.period)
           }
           binding.itemTitle.setText(foodlog.item)
           if (foodlog.startdate == foodlog.enddate) {
               binding.itemDate.setText(foodlog.startdate)
           } else {
               binding.itemDate.setText(foodlog.startdate+" - "+foodlog.enddate)
           }
        }
    }
}
