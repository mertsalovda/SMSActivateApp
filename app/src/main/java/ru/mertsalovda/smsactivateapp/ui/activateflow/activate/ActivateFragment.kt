package ru.mertsalovda.smsactivateapp.ui.activateflow.activate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrActivateBinding

class ActivateFragment : Fragment() {

    private lateinit var viewModel: ActivateViewMode
    private lateinit var adapter: ActivateAdapter

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

        adapter = ActivateAdapter {
            showToast(it.toString())
        }
        binding.recycler.adapter = adapter

        setListeners()
        setObservers()

        viewModel.loadActivateNumbers()
    }

    private fun setListeners() {
        binding.refresher.setOnRefreshListener {
            viewModel.loadActivateNumbers()
            binding.refresher.isRefreshing = false
        }
        binding.btnAddActivate.setOnClickListener {
            findNavController().navigate(R.id.action_activateFragment_to_navigation_country)
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.btnAddActivate.isShown)
                    binding.btnAddActivate.hide()
                else
                    binding.btnAddActivate.show()
            }
        })
    }

    private fun setObservers() {
        viewModel.activates.observe(viewLifecycleOwner) {
            it?.let {
                    items -> adapter.setData(items)
            }
            binding.placeHolder.visibility = if (it == null || it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}