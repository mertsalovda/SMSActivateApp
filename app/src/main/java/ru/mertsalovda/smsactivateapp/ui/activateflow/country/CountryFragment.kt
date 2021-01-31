package ru.mertsalovda.smsactivateapp.ui.activateflow.country

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrHomeBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.ActivateViewMode

class CountryFragment : Fragment() {

    private lateinit var viewModel: ActivateViewMode
    private lateinit var adapter: CountriesAdapter

    private var _binding: FrHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ActivateViewMode::class.java)

        adapter = CountriesAdapter{
            viewModel.setSelectedCountry(it)
            showToast(it.toString())
            findNavController().navigate(R.id.action_navigation_country_to_servicesFragment)
        }
        binding.recycler.adapter = adapter

        setListeners()
        setObservers()

        viewModel.loadCountry()
    }

    private fun setListeners() {
        binding.refresher.setOnRefreshListener { viewModel.loadCountry() }
    }

    private fun setObservers() {
        viewModel.countries.observe(viewLifecycleOwner){
            it?.let { items -> adapter.setData(items) }
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.refresher.isRefreshing = it
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}