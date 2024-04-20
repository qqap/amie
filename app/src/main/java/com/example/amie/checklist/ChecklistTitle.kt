package com.example.amie.checklist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.amie.icons.PieChart

@Composable
fun ChecklistTitle(
    percentage: Float,
    totalNumber: Int,
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        PieChart(
            Color(0xFFe1b53e), percentage,
            canvasSize = 35.dp.value,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            "Things to do",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            totalNumber.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(0.15f))
    }
}

@Preview(showBackground = true)
@Composable
fun ChecklistTitlePreview(){
    ChecklistTitle(
        percentage = 0.4f,
        totalNumber = 6,
    )
}