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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.databinding.ComplaintFragmentBinding


class ComplaintAdapter(
    private val onItemClicked: (Complaint) -> Unit
) : ListAdapter<Complaint, ComplaintAdapter.ComplaintViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Complaint>() {
            override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val viewHolder = ComplaintViewHolder(
            ComplaintFragmentBinding.inflate(
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

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ComplaintViewHolder(
        private var binding: ComplaintFragmentBinding
    ): RecyclerView.ViewHolder(binding.root) {
       // @SuppressLint("SimpleDateFormat")
        fun bind(complaint: Complaint) {
            binding.startdateEdittext.setText(complaint.startdatetime)
        }
    }
}
