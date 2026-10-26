package com.example.coffeemenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeemenu.databinding.ItemCoffeeBinding

class CoffeeAdapter(
    private val onAddClick: (CoffeeItem) -> Unit,
) : ListAdapter<CoffeeItem, CoffeeAdapter.CoffeeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val binding = ItemCoffeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoffeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoffeeViewHolder(
        private val binding: ItemCoffeeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoffeeItem) {
            binding.imageIcon.setImageResource(item.icon)
            binding.textName.text = item.name
            binding.textDescription.text = item.description
            binding.textPrice.text = itemView.context.getString(R.string.price_format, item.price)
            binding.root.setOnClickListener { onAddClick(item) }
            binding.buttonAdd.setOnClickListener { onAddClick(item) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<CoffeeItem>() {
        override fun areItemsTheSame(oldItem: CoffeeItem, newItem: CoffeeItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CoffeeItem, newItem: CoffeeItem) = oldItem == newItem
    }
}
