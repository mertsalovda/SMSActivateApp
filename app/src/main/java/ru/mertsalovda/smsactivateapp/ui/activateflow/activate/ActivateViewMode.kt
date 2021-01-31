package ru.mertsalovda.smsactivateapp.ui.activateflow.activate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mertsalovda.smsactivateapp.di.AppComponent
import ru.mertsalovda.smsactivateapp.network.SMSActivateRepository
import ru.mertsalovda.smsactivateapp.storage.ActivateDataBase
import ru.mertsalovda.smsactivateapp.storage.dto.ActivateNumber
import javax.inject.Inject

class ActivateViewMode : ViewModel() {

    @Inject
    lateinit var activateDataBase: ActivateDataBase

    @Inject
    lateinit var repository: SMSActivateRepository

    private val _activates = MutableLiveData<List<ActivateNumber>?>(null)
    val activates: LiveData<List<ActivateNumber>?> = _activates

    init {
        AppComponent.create().inject(this)
    }

    fun loadActivateNumbers() {
        viewModelScope.launch {
            try {
                val data = activateDataBase.getActivateDao().getAllActivateNumbers()
                _activates.postValue(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
