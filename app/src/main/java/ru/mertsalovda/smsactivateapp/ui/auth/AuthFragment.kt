package ru.mertsalovda.smsactivateapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.mertsalovda.smsactivateapp.R
import ru.mertsalovda.smsactivateapp.databinding.FrAuthBinding
import ru.mertsalovda.smsactivateapp.di.AppComponent
import ru.mertsalovda.smsactivateapp.storage.ActivateDataBase
import ru.mertsalovda.smsactivateapp.storage.dto.Profile
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AuthFragment : Fragment() {

    private var _binding: FrAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var activateDataBase: ActivateDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrAuthBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.apiKeyEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    checkAndNavigate()
                    true
                }
                else -> false
            }
        }

        binding.btnOk.setOnClickListener { checkAndNavigate() }
    }

    private fun checkAndNavigate() {
        if (isValidApiKey()) {
            findNavController().navigate(R.id.action_navigation_auth_to_bottomNavFragment)
        }
    }

    private fun isValidApiKey(): Boolean {
        val apiKey = binding.apiKeyEditText.text.toString()
        return if (apiKey.isNotEmpty()) {
            lifecycleScope.launch {
                activateDataBase.getActivateDao().insertApiKey(Profile(apiKey = apiKey))
            }
            true
        } else {
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}