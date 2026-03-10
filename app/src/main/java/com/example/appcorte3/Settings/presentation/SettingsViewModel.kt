package com.example.appcorte3.Settings.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider
import com.example.appcorte3.core.data.local.backup.DatabaseBackupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel (
) : ViewModel() {


    fun exportDB(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseBackupHelper.exportDbToZip(uri ,context)
        }
    }

    fun restoreDBFromZip(uri: Uri, ctx: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            DatabaseProvider.destroyDatabase()
            DatabaseBackupHelper.restoreDbFromZip(uri, ctx)
            DatabaseProvider.getDatabase(ctx)
        }
    }
}