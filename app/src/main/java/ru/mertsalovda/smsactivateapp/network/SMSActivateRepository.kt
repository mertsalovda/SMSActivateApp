package ru.mertsalovda.smsactivateapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServiceItem
import ru.mertsalovda.smsactivateapp.utils.toServiceItem
import ru.mertsalovda.smsactivateapp.utils.toServiceName
import ru.sms_activate.SMSActivateApi
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo
import ru.sms_activate.response.api_activation.extra.SMSActivateGetPriceInfo
import java.math.BigDecimal
import javax.inject.Inject

class SMSActivateRepository @Inject constructor() {

    private var smsActivateApi: SMSActivateApi? = null

    fun updateApiKey(apiKey: String) {
        smsActivateApi = SMSActivateApi(apiKey)
    }

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
}
