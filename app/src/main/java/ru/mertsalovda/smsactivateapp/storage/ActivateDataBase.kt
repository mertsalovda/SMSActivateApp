package ru.mertsalovda.smsactivateapp.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mertsalovda.smsactivateapp.storage.dto.ActivateNumber
import ru.mertsalovda.smsactivateapp.storage.dto.Profile

@Database(entities = [ActivateNumber::class, Profile::class], version = 2)
abstract class ActivateDataBase : RoomDatabase() {

    abstract fun getActivateDao(): ActivateDao
}