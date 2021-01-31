package ru.mertsalovda.smsactivateapp.ui.activateflow.services

import java.math.BigDecimal

data class ServiceItem(
    var image: String? = null,
    var codeName: String,
    var displayName: String,
    var count: Int,
    var cost: BigDecimal
)
