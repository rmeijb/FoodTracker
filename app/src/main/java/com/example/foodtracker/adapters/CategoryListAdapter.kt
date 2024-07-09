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

import androidx.recyclerview.widget.ListAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.database.category.Category
import com.example.foodtracker.databinding.ListItemBinding

class CategoryAdapter(
    private val onItemClicked: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback) {
    //private val context: Context,
    //private val dataset: List<Category>
    //) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }

    class CategoryViewHolder(
        private var binding: ListItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        //private val context: Context, view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //val textView: TextView = binding.itemTitle
        val textView: TextView = binding.itemTitle.findViewById(R.id.item_title)

        //val imageView: ImageView = binding.itemImage
        val imageView: ImageView = binding.itemImage.findViewById(R.id.item_image)


        //@SuppressLint("SimpleDateFormat")
        fun bind(category: Category) {
            binding.itemTitle.text = category.category
            val bmp = BitmapFactory.decodeByteArray(category.image, 0, category.image.size)
            //binding.itemImage.setImageBitmap(Bitmap.createScaledBitmap(bmp,binding.itemImage.width, binding.itemImage.height, false))
            binding.itemImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300, 300, false))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewHolder = CategoryViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        //create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        //return CategoryViewHolder(parent.context, adapterLayout)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        //val item = dataset[position]
        //holder.textView.text = item.category
        //holder.imageView.setImageResource(item.image.)
        //val bmp = BitmapFactory.decodeByteArray(item.image, 0, item.image.size)
        //holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, holder.imageView.width, holder.imageView.height, false))
    }

    //override fun getItemCount() = dataset.size
}
