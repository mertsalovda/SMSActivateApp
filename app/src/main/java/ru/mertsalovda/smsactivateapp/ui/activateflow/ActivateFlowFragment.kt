package ru.mertsalovda.smsactivateapp.ui.activateflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.mertsalovda.smsactivateapp.databinding.FrActivateFlowBinding


class ActivateFlowFragment : Fragment() {

    private var _binding: FrActivateFlowBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FrActivateFlowBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}