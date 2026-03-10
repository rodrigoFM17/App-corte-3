package com.example.appcorte3.Orders.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Orders.data.model.GeneralProductToBuy
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.Orders.data.repository.OrderProductRepository


class GeneralOrderViewModel(
    context: Context
) : ViewModel() {

    val orderProductRepository = OrderProductRepository(context)

    val _date = MutableLiveData<Long>()
    val _productsToBuy = MutableLiveData<List<ProductToBuy>>(emptyList())
    val _generalProducts = MutableLiveData<List<GeneralProductToBuy>>(emptyList())

    val productsToBuy : LiveData<List<ProductToBuy>> = _productsToBuy
    val generalProductToBuy : LiveData<List<GeneralProductToBuy>> = _generalProducts
    val date : LiveData<Long> = _date


    suspend fun onChangeDate(value: Long){
        Log.d("DATE", value.toString())
        _date.value = value
        _generalProducts.value = orderProductRepository.getProductsToBuyByDate(value)


    }

    suspend fun changeBoughtStatus(orderProductId: String, bought: Boolean) {
        Log.d("STATUS", bought.toString())
        orderProductRepository.changeBoughtStatus(orderProductId, if (bought) 0 else 1)
        _generalProducts.value = orderProductRepository.getProductsToBuyByDate(_date.value!!)
    }

    fun resetInputs() {
        _productsToBuy.value = mutableListOf()
        _generalProducts.value = mutableListOf()
        _date.value = 0
    }



}