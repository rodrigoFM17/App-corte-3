package com.example.appcorte3.core.utilities.infrastructure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appcorte3.core.utilities.models.ILoader

class Loader() {

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val isLoading : LiveData<Boolean> = _isLoading

    fun onStartLoadingAction(action: () -> Unit) {
        action()
        _isLoading.value = false
    }
}