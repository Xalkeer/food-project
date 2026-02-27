package com.example.food_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.food_project.data.Restaurant
import com.example.food_project.ui.components.DetailInfoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(resto: Restaurant, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            Scaffold(topBar = { TopAppBar(title = { Text(resto.name, fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null) } }) }) { padding ->
                Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(MaterialTheme.colorScheme.outlineVariant), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(resto.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(resto.category, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            DetailInfoItem(Icons.Default.Info, "Temps", resto.deliveryTime)
                            DetailInfoItem(Icons.Default.Notifications, "Frais", resto.deliveryFee)
                        }
                    }
                }
            }
        }
    }
}