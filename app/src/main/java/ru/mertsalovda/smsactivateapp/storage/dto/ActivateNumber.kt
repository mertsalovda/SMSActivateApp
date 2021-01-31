package ru.mertsalovda.smsactivateapp.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activate_name")
data class ActivateNumber(
    @PrimaryKey
    val id: Int,
    val service: String,
    val phone: Long
)

