package ru.mertsalovda.smsactivateapp.ui.activateflow.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.ItemCountryBinding
import ru.mertsalovda.smsactivateapp.utils.getCountryImageUrl
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo

class CountriesAdapter(private val clickListener: (SMSActivateCountryInfo) -> Unit) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private var items = listOf<SMSActivateCountryInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        val binding = ItemCountryBinding.bind(view)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    override fun getItemCount(): Int = items.size
    fun setData(data: List<SMSActivateCountryInfo>) {
        items = data
        notifyDataSetChanged()
    }

    class CountryViewHolder(private val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SMSActivateCountryInfo, clickListener: (SMSActivateCountryInfo) -> Unit) {

            if (item.isVisible) {
                binding.name.text = item.russianName
                itemView.setOnClickListener { clickListener.invoke(item) }

                Glide.with(itemView.context)
                    .load(item.id.getCountryImageUrl())
                    .into(binding.flag)
            }
        }

    }
}