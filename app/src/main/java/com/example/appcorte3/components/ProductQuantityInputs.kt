package com.example.appcorte3.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.Orders.presentation.viewModels.FRACC_OPTIONS
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT

@Composable
fun ProductQuantityInputs(
    incrementQuantity: () -> Unit,
    decrementQuantity: () -> Unit,
    setQuantity: (FRACC_OPTIONS) -> Unit,
    quantity : Int,
    fracc: FRACC_OPTIONS,
    selectedProduct: ProductEntity?,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ButtonComponent(
            icon = Icons.Default.Add,
            onClick = incrementQuantity,
            modifier = Modifier.weight(1f),
            negative = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(2f)
        ) {
            Text(
                text = quantity.toString(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            if(selectedProduct?.unit == UNIT.FRACC) {
                Spacer(modifier = Modifier.width(10.dp))
                DropdownMenuComponent(
                    negative = true,
                    padding = 5.dp,
                    placeholder = fracc.label,
                    fontSize = 15.sp,
                    menuItems = listOf(
                        MenuItem(FRACC_OPTIONS.NONE.label, { setQuantity(FRACC_OPTIONS.NONE)}),
                        MenuItem(FRACC_OPTIONS.QUARTER.label, { setQuantity(FRACC_OPTIONS.QUARTER)}),
                        MenuItem(FRACC_OPTIONS.HALF.label, { setQuantity(FRACC_OPTIONS.HALF)}),
                        MenuItem(FRACC_OPTIONS.THREE_QUARTERS.label, { setQuantity(FRACC_OPTIONS.THREE_QUARTERS)}),
                    )
                )
            }
        }

        ButtonComponent(
            icon = Icons.Default.Remove,
            onClick = decrementQuantity,
            modifier = Modifier.weight(1f),
            negative = true
        )
    }
}