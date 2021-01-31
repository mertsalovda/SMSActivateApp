package ru.mertsalovda.smsactivateapp.ui.activateflow.services

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrServicesBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.PayViewMode

class ServicesFragment : Fragment() {

    private lateinit var viewModel: PayViewMode
    private lateinit var adapter: ServicesAdapter

    private var _binding: FrServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrServicesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PayViewMode::class.java)

        setHasOptionsMenu(true)
        viewModel.clearSearchQuery()

        adapter = ServicesAdapter {
            viewModel.setSelectedService(it)
            findNavController().navigate(R.id.action_servicesFragment_to_payFragment)
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

        viewModel.getServicesByQuery().observe(viewLifecycleOwner) {
            it?.let { items -> adapter.setData(items) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите название сервиса"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}