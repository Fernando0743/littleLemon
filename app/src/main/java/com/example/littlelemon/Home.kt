package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun HomeScreen(onProfileClick: () -> Unit, db: AppDatabase) {
    val menuItems by db.menuItemDao()
        .getAllMenuItems()
        .collectAsState(initial = emptyList())

    var searchPhrase by rememberSaveable { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    // Filtrado
    val filteredItems = remember(menuItems, searchPhrase, selectedCategory) {
        var filtered = if (searchPhrase.isBlank()) {
            menuItems
        } else {
            val q = searchPhrase.trim()
            menuItems.filter { item ->
                item.title.contains(q, ignoreCase = true) ||
                        item.description.contains(q, ignoreCase = true)
            }
        }

        if (selectedCategory.isNotEmpty()) {
            filtered = filtered.filter { it.category == selectedCategory }
        }

        filtered
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header con logo y perfil
        item {
            TopBar(onProfileClick = onProfileClick)
        }

        // Hero Section mejorado
        item {
            HeroSection(
                searchPhrase = searchPhrase,
                onSearchChange = { searchPhrase = it }
            )
        }

        // Sección "ORDER FOR DELIVERY!"
        item {
            DeliveryOrderSection()
        }

        // Filtro de categoría
        item {
            CategoryFilter(
                categories = menuItems.map { it.category }.distinct(),
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = if (selectedCategory == category) "" else category
                }
            )
        }

        // Divisor
        item {
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.LightGray
            )
        }

        // Lista de menú mejorada
        items(filteredItems) { item ->
            MenuItemCard(item = item)
        }
    }
}

@Composable
fun TopBar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Espacio vacío para centrar el logo
        Spacer(modifier = Modifier.width(40.dp))

        // Logo centrado
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🍋",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LITTLE LEMON",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF495E57)
            )
        }

        // Foto de perfil
        Button(
            onClick = onProfileClick,
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text(
                text = "👤",
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun HeroSection(
    searchPhrase: String,
    onSearchChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF495E57))
            .padding(16.dp)
    ) {
        // Título principal
        Text(
            text = "Little Lemon",
            fontSize = 64.sp,
            fontFamily = MarkaziFont,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFF4CE14),
            modifier = Modifier.fillMaxWidth()
        )

        // Subtítulo
        Text(
            text = "Chicago",
            fontSize = 40.sp,
            fontFamily = MarkaziFont,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Descripción
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                    fontSize = 16.sp,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de búsqueda
                TextField(
                    value = searchPhrase,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = { Text("Enter search phrase") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            // Imagen del hero
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Hero Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun DeliveryOrderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "ORDER FOR DELIVERY!",
            fontSize = 20.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF495E57),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFEDEFEE),
                    labelColor = Color(0xFF495E57)
                )
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItemCard(item: MenuItemEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontFamily = KarlaFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF495E57),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${item.price}",
                    fontSize = 16.sp,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF495E57)
                )
            }

            // Imagen del producto
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                GlideImage(
                    model = item.image,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}