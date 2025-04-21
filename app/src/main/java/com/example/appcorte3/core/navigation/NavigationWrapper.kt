package com.example.appcorte3.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcorte3.Clients.presentation.AddClientScreen
import com.example.appcorte3.Clients.presentation.ClientsScreen
import com.example.appcorte3.Clients.presentation.ClientsViewModel
import com.example.appcorte3.Orders.presentation.AddOrderScreen
import com.example.appcorte3.Orders.presentation.OrdersScreen
import com.example.appcorte3.Orders.presentation.OrdersViewModel
import com.example.appcorte3.Orders.presentation.ParticularOrderScreen
import com.example.appcorte3.Products.presentation.AddProductScreen
import com.example.appcorte3.Products.presentation.ParticularProductScreen
import com.example.appcorte3.Products.presentation.ProductsScreen
import com.example.appcorte3.Products.presentation.ProductsViewModel
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.services.SyncService
import com.example.appcorte3.core.storage.StorageManager
import com.example.appcorte3.layouts.BottomNavigationBar
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationWrapper(activity: Activity) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val clientStorage = StorageManager<ClientEntity>(context = context, storageName = "client", object : TypeToken<ClientEntity>() {}.type)

    val productsViewModel = ProductsViewModel(
        context = context,
        navigateToAddProduct = {navController.navigate(AddProduct)},
        navigateToParticularProduct = {navController.navigate(ParticularProduct)}
    )

    val ordersViewModel = OrdersViewModel(
        context = context,
        {navController.navigate(AddOrder)},
        {navController.navigate(ParticularOrder)},
        activity = activity
    )

    val clientsViewModel = ClientsViewModel(
        context = context,
        clientStorage = clientStorage,
        navigateToAddClient = {navController.navigate(AddClient)},
        navigateToParticularClient = {}
    )

    val intent = Intent(context, SyncService::class.java)
    context.startService(intent)

    Scaffold(
        bottomBar = {BottomNavigationBar(navController)}
    ) {
        NavHost(navController = navController, startDestination = Products) {

            composable<Products> {
                ProductsScreen(productsViewModel)
            }
            composable<Clients> {
                ClientsScreen(clientsViewModel)
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

            composable<AddClient> {
                AddClientScreen(clientsViewModel)
            }

            composable<ParticularOrder> {
                ParticularOrderScreen(ordersViewModel)
            }

            composable<ParticularProduct> {
                ParticularProductScreen(productsViewModel)
            }
        }
    }

}