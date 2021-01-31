package ru.mertsalovda.smsactivateapp.ui.activateflow.activate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.ItemActivateBinding
import ru.mertsalovda.smsactivateapp.storage.dto.ActivateNumber
import ru.mertsalovda.smsactivateapp.utils.getServiceImageUrl

class ActivateAdapter(private val clickListener: (ActivateNumber) -> Unit) :
    RecyclerView.Adapter<ActivateAdapter.ActivateViewHolder>() {

    private var items = listOf<ActivateNumber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activate, parent, false)
        val binding = ItemActivateBinding.bind(view)
        return ActivateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivateViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<ActivateNumber>) {
        items = data
        notifyDataSetChanged()
    }

    class ActivateViewHolder(private val binding: ItemActivateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ActivateNumber, clickListener: (ActivateNumber) -> Unit) {

            binding.labelId.text = "ID: ${item.id}"
            binding.number.text = item.phone.toString()

            Glide.with(itemView.context)
                .load(item.service.getServiceImageUrl())
                .into(binding.icon)
        }
    }
}