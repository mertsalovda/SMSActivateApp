package ru.mertsalovda.smsactivateapp.ui.activateflow.activate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.mertsalovda.smsactivateapp.databinding.FrActivateBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.ActivateViewMode
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServiceItem
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServicesAdapter
import ru.mertsalovda.smsactivateapp.utils.getCountryImageUrl
import ru.mertsalovda.smsactivateapp.utils.getServiceImageUrl
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo

class ActivateFragment : Fragment() {

    private lateinit var viewModel: ActivateViewMode
    private lateinit var adapter: ServicesAdapter

    private var _binding: FrActivateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrActivateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ActivateViewMode::class.java)

        viewModel.loadBalance()

        adapter = ServicesAdapter {

        }

        setListeners()
        setObservers()
    }

    private fun setListeners() {
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