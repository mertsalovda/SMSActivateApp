package ru.mertsalovda.smsactivateapp.ui.activateflow.country

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrCountryBinding
import ru.mertsalovda.smsactivateapp.ui.activateflow.PayViewMode

class CountryFragment : Fragment() {

    private lateinit var viewModel: PayViewMode
    private lateinit var adapter: CountriesAdapter

    private var _binding: FrCountryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrCountryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PayViewMode::class.java)
        viewModel.setApiKey()

        setHasOptionsMenu(true)
        viewModel.clearSearchQuery()

        adapter = CountriesAdapter{
            viewModel.setSelectedCountry(it)
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
        viewModel.getCountryByQuery().observe(viewLifecycleOwner){
            it?.let { items -> adapter.setData(items) }
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.refresher.isRefreshing = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите название страны"
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