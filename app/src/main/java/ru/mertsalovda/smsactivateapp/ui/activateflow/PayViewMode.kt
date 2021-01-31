package ru.mertsalovda.smsactivateapp.ui.activateflow

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.mertsalovda.smsactivateapp.di.AppComponent
import ru.mertsalovda.smsactivateapp.network.SMSActivateRepository
import ru.mertsalovda.smsactivateapp.storage.ActivateDataBase
import ru.mertsalovda.smsactivateapp.storage.dto.ActivateNumber
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.model.ServiceItem
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo
import java.math.BigDecimal
import javax.inject.Inject

class PayViewMode : ViewModel() {

    @Inject
    lateinit var repository: SMSActivateRepository

    @Inject
    lateinit var activateDataBase: ActivateDataBase

    init {
        AppComponent.create().inject(this)
    }

    private val _balance = MutableLiveData<BigDecimal?>(null)
    val balance: LiveData<BigDecimal?> = _balance

    private val _countries = MutableLiveData<List<SMSActivateCountryInfo>?>(listOf())
    val countries: LiveData<List<SMSActivateCountryInfo>?> = _countries

    private val _countrySelected = MutableLiveData<SMSActivateCountryInfo?>(null)
    val countrySelected: LiveData<SMSActivateCountryInfo?> = _countrySelected

    private val _serviceSelected = MutableLiveData<ServiceItem?>(null)
    val serviceSelected: LiveData<ServiceItem?> = _serviceSelected

    private val _services = MutableLiveData<List<ServiceItem>?>(listOf())
    val services: LiveData<List<ServiceItem>?> = _services

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    // Поисковый запрос пользователей
    private val query = MutableLiveData<String>()

    fun getServicesByQuery(): LiveData<List<ServiceItem>> {
        val result = MediatorLiveData<List<ServiceItem>>()

        // Функция в соответствии с запросом query
        val filterFun = {
            val queryStr = query.value
            val services = services.value

            result.value = if (queryStr.isNullOrEmpty()) services
            else services!!.filter {
                it.displayName.contains(queryStr, true)
            }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(services) { filterFun.invoke() }
        result.addSource(query) { filterFun.invoke() }

        return result
    }

    fun getCountryByQuery(): LiveData<List<SMSActivateCountryInfo>> {
        val result = MediatorLiveData<List<SMSActivateCountryInfo>>()

        // Функция в соответствии с запросом query
        val filterFun = {
            val queryStr = query.value
            val countries = countries.value

            result.value = if (queryStr.isNullOrEmpty()) countries
            else countries!!.filter {
                it.russianName.contains(queryStr, true)
            }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(countries) { filterFun.invoke() }
        result.addSource(query) { filterFun.invoke() }

        return result
    }

    fun setSearchQuery(query: String) {
        this.query.value = query
    }

    fun clearSearchQuery() {
        this.query.value = ""
    }

    fun setSelectedCountry(smsActivateCountryInfo: SMSActivateCountryInfo) {
        _countrySelected.postValue(smsActivateCountryInfo)
    }

    fun setSelectedService(serviceItem: ServiceItem) {
        _serviceSelected.postValue(serviceItem)
    }

    fun setApiKey() {
        viewModelScope.launch {
            val apiKey = activateDataBase.getActivateDao().getProfile().apiKey
            repository.updateApiKey(apiKey)
            loadCountry()
        }
    }

    fun loadCountry() {
        if (!repository.apiIsNotNull()) return
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

    fun loadServicesForCountry() {
        if (!repository.apiIsNotNull()) return
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

    fun loadBalance() {
        if (!repository.apiIsNotNull()) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = repository.getBalance()
                _balance.postValue(data)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                e.cause?.printStackTrace()
                _isLoading.postValue(false)
            }
        }
    }

    fun payNumber() {
        if (!repository.apiIsNotNull()) return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data =  if (countrySelected.value != null && serviceSelected.value != null)
                repository.getNumber(
                    idCountry = countrySelected.value!!.id,
                    serviceCode = serviceSelected.value!!.codeName
                ) else {
                    null
                }
                data?.let {
                    val activateNumber = ActivateNumber(id = it.id, service = it.shortName, phone = it.number)
                    activateDataBase.getActivateDao().insert(activateNumber)
                }
                _isLoading.postValue(false)
                _isSuccess.postValue(true)
            } catch (e: Exception) {
                e.cause?.printStackTrace()
                e.printStackTrace()
                _isLoading.postValue(false)
                _isSuccess.postValue(false)
            }
        }
    }

}
