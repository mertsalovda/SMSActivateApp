package ru.mertsalovda.smsactivateapp.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import ru.mertsalovda.smsactivateapp.App
import ru.mertsalovda.smsactivateapp.storage.ActivateDataBase
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideActivateDataBase(): ActivateDataBase {
        return Room.databaseBuilder(
            App.context!!,
            ActivateDataBase::class.java,
            "activate_database"
        ).addMigrations(migration_1_2)
            .build()
    }

    private val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE profile(id BIGINT DEFAULT 1 NOT NULL, apiKey VARCHAR NOT NULL, PRIMARY KEY (id))")
        }

    }
}