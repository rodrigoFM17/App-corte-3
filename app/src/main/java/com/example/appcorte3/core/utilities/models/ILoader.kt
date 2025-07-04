package com.example.appcorte3.core.utilities.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ILoader {
    val _isLoading : MutableLiveData<Boolean>
    val isLoading : LiveData<Boolean>

    fun onStartLoadingAction(action: () -> Unit) {
        action()
        _isLoading.value = false
    }

    fun onChangeLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}