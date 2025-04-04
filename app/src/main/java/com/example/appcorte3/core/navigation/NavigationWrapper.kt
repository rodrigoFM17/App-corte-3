package com.example.appcorte3.core.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcorte3.Clients.presentation.ClientsScreen
import com.example.appcorte3.Clients.presentation.ClientsViewModel
import com.example.appcorte3.Orders.presentation.AddOrderScreen
import com.example.appcorte3.Orders.presentation.OrdersScreen
import com.example.appcorte3.Orders.presentation.OrdersViewModel
import com.example.appcorte3.Products.presentation.AddProductScreen
import com.example.appcorte3.Products.presentation.ProductsScreen
import com.example.appcorte3.Products.presentation.ProductsViewModel
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.storage.StorageManager
import com.example.appcorte3.layouts.BottomNavigationBar
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val clientStorage = StorageManager<ClientEntity>(context = context, storageName = "client", object : TypeToken<ClientEntity>() {}.type)

    val productsViewModel = ProductsViewModel(
        context = context,
        navigateToAddProduct = {navController.navigate(AddProduct)}
    )

    val ordersViewModel = OrdersViewModel(
        context = context,
        {navController.navigate(AddOrder)}
    )

    Scaffold(
        bottomBar = {BottomNavigationBar(navController)}
    ) {
        NavHost(navController = navController, startDestination = Products) {

            composable<Products> {
                ProductsScreen(productsViewModel)
            }
            composable<Clients> {
                ClientsScreen(
                    ClientsViewModel(
                        context,
                        navigateToParticularClient = {},
                        navigateToAddClient = {},
                        clientStorage = clientStorage
                    )
                )
            }
            composable<Orders> {
                OrdersScreen(ordersViewModel)
            }

            composable<AddProduct> {
                AddProductScreen(productsViewModel)
            }

            composable<AddOrder> {
                AddOrderScreen(ordersViewModel)
            }
        }
    }

}