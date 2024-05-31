package com.embark.notes.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.embark.notes.databinding.ItemViewCheckboxBinding

class CheckboxesAdapter(
    private var list: List<String>
) : RecyclerView.Adapter<CheckboxesAdapter.CheckboxesViewHolder>() {

    inner class CheckboxesViewHolder(private val binding: ItemViewCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: String) {
            binding.apply {
                etContent.setText(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewCheckboxBinding.inflate(layoutInflater)
        return CheckboxesViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CheckboxesViewHolder, position: Int) {
        holder.bind(list[position])
    }
}