package ru.mertsalovda.smsactivateapp.ui.activateflow.pay

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrPayBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.PayViewMode
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.model.ServiceItem
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServicesAdapter
import ru.mertsalovda.smsactivateapp.utils.getCountryImageUrl
import ru.mertsalovda.smsactivateapp.utils.getServiceImageUrl
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo

class PayFragment : Fragment() {

    private lateinit var viewModel: PayViewMode
    private lateinit var adapter: ServicesAdapter

    private var _binding: FrPayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrPayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PayViewMode::class.java)

        viewModel.loadBalance()

        adapter = ServicesAdapter {

        }

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.btnPay.setOnClickListener {
            viewModel.payNumber()
        }
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
        }

        viewModel.countrySelected.observe(viewLifecycleOwner) {
            it?.let { country ->
                displayCountryInfo(country)
            }
        }

        viewModel.serviceSelected.observe(viewLifecycleOwner) {
            it?.let { service ->
                displayServiceInfo(service)
            }
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            it?.let { balance ->
                binding.balance.text = "${balance.toString()} ₽"
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            binding.btnPay.isEnabled = !it
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.to_root)
            }
        }

    }

    private fun displayServiceInfo(service: ServiceItem) {
        binding.serviceName.text = service.displayName
        binding.amount.text = "${service.cost} ₽"
        Glide.with(requireContext())
            .load(service.codeName.getServiceImageUrl())
            .into(binding.icon)
    }

    private fun displayCountryInfo(country: SMSActivateCountryInfo) {
        binding.countryName.text = country.russianName
        Glide.with(requireContext())
            .load(country.id.getCountryImageUrl())
            .into(binding.flag)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}