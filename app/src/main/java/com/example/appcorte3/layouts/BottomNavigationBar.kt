package com.example.appcorte3.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.core.navigation.Clients
import com.example.appcorte3.core.navigation.Orders
import com.example.appcorte3.core.navigation.Products

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFF353535))
    ) {

        ButtonComponent(
            icon = Icons.Default.Motorcycle,
            text = "",
            onClick = {navController.navigate(Orders)},
            modifier = Modifier.weight(1f),
            negative = true
        )

        ButtonComponent(
            icon = Icons.Default.ShoppingCart,
            text = "",
            onClick = {navController.navigate(Products)},
            modifier = Modifier.weight(1f),
            negative = true
        )

        ButtonComponent(
            icon = Icons.Default.AccountCircle,
            text = "",
            onClick = {navController.navigate(Clients)},
            modifier = Modifier.weight(1f),
            negative = true
        )

    }

}