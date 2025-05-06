package com.example.appcorte3.Orders.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.Orders.data.repository.OrderProductRepository

class GeneralOrderViewModel(
    context: Context
) : ViewModel() {

    val orderProductRepository = OrderProductRepository(context)

    val _date = MutableLiveData<Long>()
    val _productsToBuy = MutableLiveData<List<ProductToBuy>>(emptyList())

    val productsToBuy : LiveData<List<ProductToBuy>> = _productsToBuy
    val date : LiveData<Long> = _date


    suspend fun onChangeDate(value: Long){
        Log.d("DATE", value.toString())
        _date.value = value
        _productsToBuy.value = orderProductRepository.getProductsToBuyByDate(value)
    }



}