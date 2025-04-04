package com.example.appcorte3.core.network.model

data class APIResponse<T> (
    val success: Boolean,
    val data: T,
    val message: String
)