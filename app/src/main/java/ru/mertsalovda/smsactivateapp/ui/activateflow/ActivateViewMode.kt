package ru.mertsalovda.smsactivateapp.ui.activateflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mertsalovda.smsactivateapp.AppComponent
import ru.mertsalovda.smsactivateapp.network.SMSActivateRepository
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServiceItem
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo
import javax.inject.Inject

class ActivateViewMode : ViewModel() {

    @Inject
    lateinit var repository: SMSActivateRepository

    init {
        AppComponent.create().inject(this)
        repository.updateApiKey("A97b657d8b31e8cc5106458cc7422A4c")
    }

    private val _countries = MutableLiveData<List<SMSActivateCountryInfo>?>(listOf())
    val countries: LiveData<List<SMSActivateCountryInfo>?> = _countries

    private val _countrySelected = MutableLiveData<SMSActivateCountryInfo?>(null)
    val countrySelected: LiveData<SMSActivateCountryInfo?> = _countrySelected

    private val _services = MutableLiveData<List<ServiceItem>?>(listOf())
    val services: LiveData<List<ServiceItem>?> = _services

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadCountry() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = repository.getCountries()
                _countries.postValue(data)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                e.cause?.printStackTrace()
                _isLoading.postValue(false)
            }
        }
    }

    fun setSelectedCountry(smsActivateCountryInfo: SMSActivateCountryInfo) {
        _countrySelected.postValue(smsActivateCountryInfo)
    }

    fun loadServicesForCountry() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = countrySelected.value?.id?.let { repository.getServicesByCountryId(it) }
                _services.postValue(data)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                e.cause?.printStackTrace()
                _isLoading.postValue(false)
            }
        }
    }

}
