package ru.mertsalovda.smsactivateapp.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mertsalovda.smsactivateapp.storage.dto.ActivateNumber
import ru.mertsalovda.smsactivateapp.storage.dto.Profile

@Dao
interface ActivateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiKey(profile: Profile)

    @Query("SELECT * FROM profile WHERE id = 1")
    suspend fun getProfile(): Profile

    @Query("DELETE FROM profile WHERE id = 1")
    suspend fun deleteProfile()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activate: ActivateNumber)

    @Query("DELETE FROM activate_name")
    suspend fun deleteAll()

    @Query("DELETE FROM activate_name WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM activate_name")
    suspend fun getAllActivateNumbers(): List<ActivateNumber>

    @Query("SELECT * FROM activate_name WHERE id = :id")
    suspend fun getActivateNumberById(id: Int): ActivateNumber
}