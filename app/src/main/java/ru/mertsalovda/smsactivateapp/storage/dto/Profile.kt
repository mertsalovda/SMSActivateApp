package ru.mertsalovda.smsactivateapp.storage.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey
    val id: Long = 1,
    val apiKey: String
)
