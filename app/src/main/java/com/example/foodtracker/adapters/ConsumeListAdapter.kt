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


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.databinding.ConsumeItemBinding
import com.example.foodtracker.databinding.ListConsumeItemBinding

class ConsumeAdapter(
    private val onItemClicked: (Consume) -> Unit
) : ListAdapter<Consume, ConsumeAdapter.ConsumeViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Consume>() {
            override fun areItemsTheSame(oldItem: Consume, newItem: Consume): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Consume, newItem: Consume): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ConsumeViewHolder(
        private var binding: ConsumeItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(consume: Consume) {
            binding.productNameTextView.text = consume.productName
            binding.quantityTextView.text = consume.quantity.toString()
            binding.unitTextView.text = consume.unit
            binding.dateTextView.text = consume.datetime
        }

        init {
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this)
        }
        // Handles the row being being clicked
        override fun onClick(v: View?) {
            //val position = absoluteAdapterPosition // gets item position
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                //val user = users[position]
                // We can access the data within the views
                //Toast.makeText(context, textView.text, Toast.LENGTH_SHORT).show()

                // val intent = Intent(context, com.example.foodtracker.ProductActivity::class.java)
                // intent.putExtra(com.example.foodtracker.ProductActivity.CATEGORY, textView.text)
                // context.startActivity(intent)
                //onItemClicked(getItem(position))
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumeViewHolder {
        val viewHolder = ConsumeViewHolder(
            ConsumeItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        //create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_consume_item, parent, false)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ConsumeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
