package ru.mertsalovda.smsactivateapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.model.ServiceItem
import ru.mertsalovda.smsactivateapp.utils.toServiceItem
import ru.sms_activate.SMSActivateApi
import ru.sms_activate.response.api_activation.SMSActivateActivation
import ru.sms_activate.response.api_activation.enums.SMSActivateGetStatusActivation
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SMSActivateRepository @Inject constructor() {

    private var smsActivateApi: SMSActivateApi? = null

    fun updateApiKey(apiKey: String) {
        smsActivateApi = SMSActivateApi(apiKey)
    }

    fun apiIsNotNull() = smsActivateApi != null

    suspend fun getBalance(): BigDecimal? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let { it.balance }
        }

    suspend fun getCountries(): List<SMSActivateCountryInfo>? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let {
                it.countries.smsActivateGetCountryInfoList
            }
        }

    suspend fun getCountry(idCountry: Int): SMSActivateCountryInfo? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let { it.countries.get(idCountry) }
        }

    suspend fun getServicesByCountryId(idCountry: Int): List<ServiceItem>? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let {
                it.getPricesAllServicesByCountryId(idCountry)
                    .getSmsActivateGetPriceMap(idCountry).toServiceItem()
            }
        }

    suspend fun getNumber(
        idCountry: Int,
        serviceCode: String,
        forward: Boolean = false,
        operatorSet: MutableSet<String>? = mutableSetOf("any"),
        phoneExceptionSet: MutableSet<String>? = null
    ): SMSActivateActivation? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let {
                it.getNumber(
                    idCountry,
                    serviceCode,
                    forward,
                    operatorSet,
                    phoneExceptionSet
                )
            }
        }

    suspend fun getStatus(id: Int): SMSActivateGetStatusActivation? =
        withContext(Dispatchers.IO) {
            smsActivateApi?.let { it.getStatus(id).smsActivateGetStatus }
        }
}
