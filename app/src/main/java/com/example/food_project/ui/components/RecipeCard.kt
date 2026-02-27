package com.example.food_project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_project.data.Restaurant

@Composable
fun RecipeCard(resto: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(MaterialTheme.colorScheme.outlineVariant), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ShoppingCart, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(40.dp))
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(resto.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${resto.category} • ${resto.deliveryTime}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}