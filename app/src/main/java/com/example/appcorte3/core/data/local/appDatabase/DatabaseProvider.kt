package com.example.appcorte3.core.data.local.appDatabase

import android.content.Context
import androidx.room.Room
import com.example.appcorte3.core.data.local.migrations.MIGRATION_DB_VERSION_2_TO_3
import java.io.File

object DatabaseProvider {

    private var appDatabase: AppDataBase? = null
    val dbName = "app_database"

    fun getDatabase(ctx: Context): AppDataBase{
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                ctx.applicationContext,
                AppDataBase::class.java,
                dbName,
            ).addMigrations(
                MIGRATION_DB_VERSION_2_TO_3
            ).build()
        }
        return appDatabase!!
    }

    fun getDatabaseDir(ctx: Context): File {
        return ctx.getDatabasePath(dbName).parentFile!!
    }

    fun databaseFiles(ctx: Context): List<File> {
        val parentDir = getDatabaseDir(ctx)
        return parentDir.listFiles()?.filter {
            it.name.startsWith(dbName)
        } ?: emptyList()
    }

    fun destroyDatabase() {
        appDatabase?.close()
        appDatabase = null
    }
}