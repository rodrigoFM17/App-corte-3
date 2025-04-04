package com.example.appcorte3.core.data.local.appDatabase

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var appDatabase: AppDataBase? = null

    fun getDatabase(ctx: Context): AppDataBase{
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                ctx.applicationContext,
                AppDataBase::class.java,
                "app_database",
            ).fallbackToDestructiveMigration()
                .build()
        }
        return appDatabase!!
    }

    fun destroyDatabase() {
        appDatabase = null
    }
}