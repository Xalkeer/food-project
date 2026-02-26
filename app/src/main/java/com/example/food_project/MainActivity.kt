package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.food_project.ui.theme.FoodprojectTheme

data class Restaurant(
    val name: String,
    val category: String,
    val deliveryTime: String,
    val deliveryFee: String,
    val isAvailable: Boolean
)

sealed class BottomScreen(val title: String, val icon: ImageVector) {
    object Home : BottomScreen("Accueil", Icons.Default.Home)
    object Profile : BottomScreen("Profil", Icons.Default.Person)
    object Login : BottomScreen("Connexion", Icons.Default.Lock)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodprojectTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf<BottomScreen>(BottomScreen.Login) }
    var isLoggedIn by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF06C167)
    val lightGreenBg = Color(0xFFE8F9F1)
    val profileTab = if (isLoggedIn) BottomScreen.Profile else BottomScreen.Login

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F8F8),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.height(80.dp)
            ) {
                val items = listOf(BottomScreen.Home, profileTab)
                items.forEach { screen ->
                    val isSelected = selectedScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedScreen = screen },
                        label = null,
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (isSelected) primaryGreen else Color.Gray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = lightGreenBg)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                BottomScreen.Home -> HomeScreen()
                BottomScreen.Profile -> PlaceholderScreen("Votre Profil", Icons.Default.Person)
                BottomScreen.Login -> {
                    if (isLoggedIn) PlaceholderScreen("Votre Profil", Icons.Default.Person)
                    else LoginForm { _, _ ->
                        isLoggedIn = true
                        selectedScreen = BottomScreen.Home
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val categories = listOf("Tout", "Pizza", "Sushi", "Burger", "Tacos", "Salade")
    var selectedCategory by remember { mutableStateOf("Tout") }
    var searchText by remember { mutableStateOf("") }
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val restaurants = remember { mutableStateListOf(
        Restaurant("Pizza Hut", "Pizza", "20-30 min", "0.99€", true),
        Restaurant("Sushi Shop", "Sushi", "30-45 min", "2.99€", true),
        Restaurant("Burger King", "Burger", "15-25 min", "Gratuit", false),
        Restaurant("O'Tacos", "Tacos", "20-35 min", "1.50€", true),
        Restaurant("Jour", "Salade", "15-20 min", "2.00€", false),
        Restaurant("Domino's", "Pizza", "25-35 min", "0.99€", true)
    ) }

    val filteredRestaurants = restaurants.filter {
        (selectedCategory == "Tout" || it.category == selectedCategory) &&
                (it.name.contains(searchText, ignoreCase = true))
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF06C167),
                contentColor = Color.White
            ) { Icon(Icons.Default.Add, "Ajouter") }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Rechercher...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF06C167))
            )

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFF06C167) else Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Text(category, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item { Text("Restaurants à proximité", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                items(filteredRestaurants) { resto ->
                    RestaurantCard(resto = resto, onClick = { selectedRestaurant = resto })
                }
            }
        }
    }

    selectedRestaurant?.let { resto ->
        RestaurantDetailScreen(resto = resto, onDismiss = { selectedRestaurant = null })
    }

    if (showAddDialog) {
        AddRestaurantDialog(categories, onDismiss = { showAddDialog = false }, onAdd = { restaurants.add(0, it) })
    }
}

@Composable
fun RestaurantCard(resto: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color(0xFFEEEEEE)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ShoppingCart, null, tint = Color.LightGray, modifier = Modifier.size(40.dp))
                if (!resto.isAvailable) {
                    Surface(color = Color.Black.copy(0.7f), shape = RoundedCornerShape(8.dp)) {
                        Text("Indisponible", color = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(resto.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${resto.category} • ${resto.deliveryTime}", color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(resto: Restaurant, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Scaffold(
                containerColor = Color.White,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = resto.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Retour",
                                    tint = Color.Black
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color(0xFFF0F0F0)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                    }
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(resto.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(resto.category, color = Color(0xFF06C167), fontSize = 18.sp, fontWeight = FontWeight.Medium)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFEEEEEE))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            DetailInfoItem(Icons.Default.Info, "Temps", resto.deliveryTime)
                            DetailInfoItem(Icons.Default.Notifications, "Frais", resto.deliveryFee)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("À propos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(
                            text = "Découvrez les spécialités de ${resto.name}. Nous garantissons une livraison rapide et des produits de qualité.",
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C167)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Voir la carte", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Color.Gray)
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantDialog(categories: List<String>, onDismiss: () -> Unit, onAdd: (Restaurant) -> Unit) {
    var name by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf(categories[1]) }
    var time by remember { mutableStateOf("20-30 min") }
    var fee by remember { mutableStateOf("0.99€") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouveau Restaurant", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nom") })
                Box {
                    OutlinedTextField(
                        value = cat, onValueChange = {}, label = { Text("Catégorie") },
                        readOnly = true, trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier.clickable { expanded = true }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.filter { it != "Tout" }.forEach { c ->
                            DropdownMenuItem(text = { Text(c) }, onClick = { cat = c; expanded = false })
                        }
                    }
                }
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Temps de livraison") })
                OutlinedTextField(value = fee, onValueChange = { fee = it }, label = { Text("Frais de livraison") })
            }
        },
        confirmButton = {
            Button(
                onClick = { if(name.isNotBlank()) onAdd(Restaurant(name, cat, time, fee, true)); onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C167))
            ) { Text("Ajouter") }
        }
    )
}

@Composable
fun LoginForm(onLogin: (String, String) -> Unit) {
    var u by remember { mutableStateOf("") }
    var p by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Connexion", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = u, onValueChange = { u = it }, label = { Text("Utilisateur") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = p, onValueChange = { p = it }, label = { Text("Mot de passe") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onLogin(u, p) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06C167))) {
            Text("Se connecter", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Text(title, color = Color.Gray, fontSize = 20.sp)
    }
}