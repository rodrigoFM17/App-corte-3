package com.example.appcorte3.core.data.local.backup

import android.content.Context
import android.net.Uri
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object DatabaseBackupHelper {

    fun exportDbToZip(uri: Uri, ctx: Context) {

        val resolver = ctx.contentResolver

        resolver.openOutputStream(uri)?.use { outputStream ->
            ZipOutputStream(BufferedOutputStream(outputStream)).use { zip ->
                DatabaseProvider.databaseFiles(ctx).forEach { file ->
                    val entry = ZipEntry(file.name)
                    zip.putNextEntry(entry)

                    FileInputStream(file).use { input ->
                        input.copyTo(zip)
                    }
                    zip.closeEntry()
                }
            }
        }

    }

    fun restoreDbFromZip(uri: Uri, ctx: Context) {
        val resolver = ctx.contentResolver
        val databaseDir = DatabaseProvider.getDatabaseDir(ctx)

        resolver.openInputStream(uri)?.use { inputStream ->
            ZipInputStream(BufferedInputStream(inputStream)).use { zip ->
                var entry = zip.nextEntry
                while(entry != null) {
                    val file = File(databaseDir, entry.name)

                    FileOutputStream(file).use { output ->
                        zip.copyTo(output)
                    }
                    zip.closeEntry()
                    entry = zip.nextEntry
                }
            }
        }
    }

}