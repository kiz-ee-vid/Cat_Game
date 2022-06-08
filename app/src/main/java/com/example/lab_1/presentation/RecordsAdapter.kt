package com.example.lab_1.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_1.data.Pair
import com.example.lab_1.databinding.RecordItemBinding

class RecordsAdapter(private val pairs: List<Pair>) :
    RecyclerView.Adapter<RecordsAdapter.RecordHolder>() {

    inner class RecordHolder(val binding: RecordItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        val binding = RecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        with(holder.binding) {
            date.text = pairs[position].date
            score.text = pairs[position].points.toString()
        }
    }

    override fun getItemCount(): Int {
        return pairs.size
    }

}