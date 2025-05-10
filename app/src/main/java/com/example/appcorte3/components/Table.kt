package com.example.appcorte3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TableColumn (
    val text: String,
    val weight: Float,
    val onClick: (() -> Unit)? = null
)

data class TableRow(
    val columns: List<TableColumn>,
    val onClick: (() -> Unit)? = null
)

@Composable
fun Table(
    height: Dp,
    tableContent: List<TableRow>
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF353535))
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier
            .background(Color(0xFF7AB317))
            .height(10.dp)
            .fillMaxWidth()
        )

        for ((i, tableRow) in tableContent.withIndex()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                    .padding(10.dp)
                    .clickable {
                        tableRow.onClick?.let { it() }
                    }
            ) {
                for(column in tableRow.columns) {
                    Text( text = column.text, modifier = Modifier.weight(column.weight))
                }
            }
        }

    }
}