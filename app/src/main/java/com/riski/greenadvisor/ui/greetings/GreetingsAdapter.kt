package com.riski.greenadvisor.ui.greetings

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.greetings.GreetingsData
import com.riski.greenadvisor.databinding.ItemGreetingsBinding

class GreetingsAdapter(private val greetingsViewModel: GreetingsViewModel) : RecyclerView.Adapter<GreetingsAdapter.GreetingsViewHolder>() {

    inner class GreetingsViewHolder(private val binding: ItemGreetingsBinding) : RecyclerView.ViewHolder(binding.root) {
             fun bind(greetingsItem: GreetingsData) {
                 binding.itemGreetingsImg.setImageResource(greetingsItem.imageRes)
                 binding.itemGreetingsText.text = greetingsItem.text.toString()
                 binding.itemGreetingsBtn.setOnClickListener {
                     greetingsViewModel.onJoinButtonClick(binding.root.context)
                 }
             }
         }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GreetingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGreetingsBinding.inflate(inflater, parent, false)
        return GreetingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GreetingsViewHolder, position: Int) {
        val greetingsItem = greetingsViewModel.imageIndex.value?.get(position)
        greetingsItem?.let {
            holder.bind(it)
            val text = holder.itemView.context.getString(it.text)
            val textView = holder.itemView.findViewById<TextView>(R.id.item_greetings_text)
            textView.text = text
            val textSize = holder.itemView.resources.getDimensionPixelSize(it.textSize)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            val btnJoin = holder.itemView.findViewById<Button>(R.id.item_greetings_btn)
            if (position == itemCount - 1) {
                btnJoin.visibility = View.VISIBLE
            } else {
                btnJoin.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return greetingsViewModel.imageIndex.value?.size ?: 0
    }
}