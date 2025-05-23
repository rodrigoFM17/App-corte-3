package com.example.appcorte3.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcorte3.Clients.presentation.AddClientScreen
import com.example.appcorte3.Clients.presentation.ClientsScreen
import com.example.appcorte3.Clients.presentation.ClientsViewModel
import com.example.appcorte3.Clients.presentation.ParticularClientScreen
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.Orders.presentation.AddOrderScreen
import com.example.appcorte3.Orders.presentation.OrdersScreen
import com.example.appcorte3.Orders.presentation.viewModels.OrdersViewModel
import com.example.appcorte3.Orders.presentation.ParticularOrderScreen
import com.example.appcorte3.Orders.presentation.viewModels.GeneralOrderViewModel
import com.example.appcorte3.Products.presentation.AddProductScreen
import com.example.appcorte3.Orders.presentation.GeneralOrderScreen
import com.example.appcorte3.Orders.presentation.viewModels.ParticularOrderViewModel
import com.example.appcorte3.Products.presentation.ParticularProductScreen
import com.example.appcorte3.Products.presentation.ProductsScreen
import com.example.appcorte3.Products.presentation.ProductsViewModel
import com.example.appcorte3.core.storage.StorageManager
import com.example.appcorte3.layouts.BottomNavigationBar
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationWrapper(activity: Activity) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val orderStorage = StorageManager<OrderDetail>(context, "orderStorage", object : TypeToken<OrderDetail>() {}.type)

    val productsViewModel = ProductsViewModel(
        context = context,
        navigateToAddProduct = {navController.navigate(AddProduct)},
        navigateToParticularProduct = {navController.navigate(ParticularProduct)},
        navigateToProducts = {navController.popBackStack()},
        navigateBack = {navController.popBackStack()}
    )

    val ordersViewModel = OrdersViewModel(
        context = context,
        {navController.navigate(AddOrder)},
        {navController.navigate(ParticularOrder)},
        navigateBack = {navController.popBackStack()},
        navigateToGeneralOrder = {navController.navigate(GeneralOrder)},
        activity = activity,
        orderStorage = orderStorage
    )

    val clientsViewModel = ClientsViewModel(
        context = context,
        navigateToAddClient = {navController.navigate(AddClient)},
        navigateToParticularClient = {navController.navigate(ParticularClient)},
        navigateBack = {navController.popBackStack()}
    )

    val generalOrderViewModel = GeneralOrderViewModel(
        context = context
    )

    val particularOrderViewModel = ParticularOrderViewModel(
        activity = activity,
        orderStorageManager = orderStorage,
        navigateBack = {navController.popBackStack()},
        context = context
    )


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
                ParticularOrderScreen(particularOrderViewModel)
            }

            composable<ParticularProduct> {
                ParticularProductScreen(productsViewModel)
            }

            composable<ParticularClient> {
                ParticularClientScreen(clientsViewModel)
            }

            composable<GeneralOrder> {
                GeneralOrderScreen(generalOrderViewModel)
            }
        }
    }

}