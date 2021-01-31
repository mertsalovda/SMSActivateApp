package ru.mertsalovda.smsactivateapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mertsalovda.smsactivateapp.ui.activateflow.services.ServiceItem
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

    private fun MutableMap<String, SMSActivateGetPriceInfo>.toServiceItem(): List<ServiceItem> {
        val result = mutableListOf<ServiceItem>()
        for ((key, value) in this) {
            if (!key.endsWith("_1")) {
                result.add(
                    ServiceItem(
                        image = "https://sms-activate.ru/assets/ico/${key}0.png",
                        codeName = key,
                        displayName = key.toServiceName() ?: "",
                        count = value.countPhoneNumbers,
                        cost = value.cost
                    )
                )
            }
        }
        return result
    }
}
