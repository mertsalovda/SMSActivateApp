package ru.mertsalovda.smsactivateapp.ui.activateflow.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.mertsalovda.smsactivateapp.databinding.FrServicesBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.ActivateViewMode

class ServicesFragment : Fragment() {

    private lateinit var viewModel: ActivateViewMode
    private lateinit var adapter: ServicesAdapter

    private var _binding: FrServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrServicesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ActivateViewMode::class.java)

        adapter = ServicesAdapter {
            showToast(it.toString())
        }
        binding.recycler.adapter = adapter

        setListeners()
        setObservers()

        viewModel.loadServicesForCountry()
    }

    private fun setListeners() {
        binding.refresher.setOnRefreshListener {
            viewModel.loadServicesForCountry()
        }
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.refresher.isRefreshing = it
        }

        viewModel.services.observe(viewLifecycleOwner) {
            it?.let { items -> adapter.setData(items) }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}