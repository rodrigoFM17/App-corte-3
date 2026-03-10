package com.example.appcorte3.core.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_DB_VERSION_2_TO_3 = object : Migration(2, 3) {
override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE OrderProducts ADD COLUMN bought INTEGER NOT NULL DEFAULT 0"
        )
    }
}