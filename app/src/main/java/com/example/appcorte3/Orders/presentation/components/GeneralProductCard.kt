package com.example.appcorte3.Orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.Orders.data.model.GeneralProductToBuy
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.components.StatusIndicator


@Composable
fun GeneralProductCard(
    generalProduct: GeneralProductToBuy,
    onClickProduct: (String, Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color(if(generalProduct.bought) 0xFF252525 else 0xFF353535))
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
//                .background(Color(if(generalProduct.bought) 0xFFFF0000 else 0xFF00FF00))
                .padding(bottom = 10.dp)
                .clickable {
                    generalProduct.products.forEach{
                        onClickProduct(it.orderProductId, generalProduct.bought)
                    }
//                    productToBuy.bought = !productToBuy.bought
//                    bought = !bought
                }
        ) {
            StatusIndicator(generalProduct.bought, positiveStatusText = "Comprado", negativeStatusText = "Pendiente", changeStatus = {})
            Text(
                text = generalProduct.name,
                fontWeight = FontWeight.Bold,
                textDecoration = if(generalProduct.bought) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                text = "total: " + generalProduct.quantity.toString()
            )
        }

        generalProduct.products.forEach{
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(if(!it.bought) 0xFF353535 else 0xFF252525))
                    .padding(5.dp)
                    .clickable {
                        onClickProduct(it.orderProductId, it.bought)
                    }
            ) {
                Text(
                    text = it.name,
                    fontSize = 12.sp,
                    textDecoration = if(it.bought) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = it.quantity.toString(),
                    fontSize = 12.sp
                )
            }
        }




    }

}