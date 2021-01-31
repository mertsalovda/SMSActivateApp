package ru.mertsalovda.smsactivateapp.ui.activateflow.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.ItemServicesBinding

class ServicesAdapter(private val clickListener: (ServiceItem) -> Unit) :
    RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    private var items = listOf<ServiceItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
        val binding = ItemServicesBinding.bind(view)
        return ServicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<ServiceItem>) {
        items = data
        notifyDataSetChanged()
    }

    class ServicesViewHolder(private val binding: ItemServicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ServiceItem,
            clickListener: (ServiceItem) -> Unit
        ) {
            binding.name.text = item.displayName
            binding.count.text = "${item.count} шт."
            binding.amount.text = "${item.cost} Р"
            itemView.setOnClickListener { clickListener.invoke(item) }

            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.icon)
        }

    }
}