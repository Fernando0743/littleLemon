package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlin.contracts.contract


//Data class para los productos extra

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onMenuItemClick: (MenuItemEntity) -> Unit,
    db: AppDatabase
) {
    HomeContent(
        onProfileClick = onProfileClick,
        db = db,
        onMenuItemClick = onMenuItemClick
    )
}



@Composable
fun HomeContent(
    onProfileClick: () -> Unit,
    db: AppDatabase,
    onMenuItemClick: (MenuItemEntity) -> Unit
) {
    // Observa cambios en la base de datos y mantiene la lista actualizada reactivamente
    val menuItems by db.menuItemDao()
        .getAllMenuItems()
        .collectAsState(initial = emptyList())

    // Estados locales para el filtrado y búsqueda
    var searchPhrase by rememberSaveable { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    //Lógica de filtrado reactiva que se recalcula cuando cambian las dependencias
    val filteredItems = remember(menuItems, searchPhrase, selectedCategory) {
        // Primer filtro: búsqueda por texto en título y descripción
        var filtered = if (searchPhrase.isBlank()) {
            menuItems // Si no hay búsqueda, muestra todos los items
        } else {
            val q = searchPhrase.trim()
            menuItems.filter { item ->
                // Búsqueda case-insensitive en título y descripció
                item.title.contains(q, ignoreCase = true) ||
                        item.description.contains(q, ignoreCase = true)
            }
        }

        // Segundo filtro: categoría (se aplica sobre el resultado del filtro de búsqueda)
        if (selectedCategory.isNotEmpty()) {
            filtered = filtered.filter { it.category == selectedCategory }
        }

        filtered // Retorna la lista filtrada final
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
            MenuItemCard(item = item,
                onItemClick = onMenuItemClick)
        }
    }
}

@Composable
fun TopBar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp) ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Espacio vacío para centrar el logo
        Spacer(modifier = Modifier.width(40.dp))

        // Logo centrado
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Logo
            Image(
                painter = painterResource(id = R.drawable.little_lemon_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(50.dp),
                contentScale = ContentScale.Crop
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
            .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end= 16.dp)
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
            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp) ,
    ) {
        Text(
            text = "ORDER FOR DELIVERY!",
            fontSize = 20.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,

        )
    }
}

@Composable
fun CategoryFilter(
    categories: List<String>, // Lista de categorías disponibles extraídas de los datos
    selectedCategory: String, // Categoría actualmente seleccionada
    onCategorySelected: (String) -> Unit // Callback para manejar cambios de categoría
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) }, // Ejecuta callback con la categoría seleccionada
                label = {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = selectedCategory == category, // Determina si este chip está activo
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
fun MenuItemCard(
    item: MenuItemEntity,  // Datos del producto desde la base de datos
    onItemClick: (MenuItemEntity) -> Unit) // Callback que se ejecuta al hacer clic en el card
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable{ onItemClick(item) }, // Hace clickeable el card y ejecuta el callback
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailScreen(
    menuItem: MenuItemEntity,
    onBackClick: () -> Unit,
    onAddToCart: (MenuItemEntity, List<ProductExtra>, Int) -> Unit
){
    var quantity by remember { mutableStateOf(1) }
    var selectedExtras by remember { mutableStateOf(listOf<ProductExtra>()) }

    //Define extras hardcodeado
    val availableExtras = remember(menuItem.title) {
        when (menuItem.title.lowercase()) {
            "bruschetta" -> listOf(
                ProductExtra("Eta", 1.0),
                ProductExtra("Parmesan", 1.0),
                ProductExtra("Dressing", 1.0)
            )
            "pasta" -> listOf(
                ProductExtra("Bacon", 1.0),
                ProductExtra("Parmesan", 1.0)
            )
            else -> listOf(
                ProductExtra("Extra sauce", 1.0),
                ProductExtra("Cheese", 1.0)
            )
        }
    }

    val deliveryTime = "30 minutes"

    val basePrice = menuItem.price.replace("$", "").toDoubleOrNull() ?: 0.0
    val extrasPrice = selectedExtras.sumOf { it.price }
    val totalPrice = (basePrice + extrasPrice) * quantity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //Header con boton de regreso y logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp, bottom = 10.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape)
            )
            {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                //Logo
                Image(
                    painter = painterResource(id = R.drawable.little_lemon_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(50.dp),
                    contentScale = ContentScale.Crop
                )
            }

            //Foto de perfil
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            )
            {
                Text(text = "👤", fontSize = 20.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        )
        {
            item{
                //Imagen del alimento
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(top = 16.dp, bottom = 5.dp)
                )
                {
                    GlideImage(
                        model = menuItem.image,
                        contentDescription = menuItem.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            //Informacion del producto
            item {
                Column(
                    modifier = Modifier.padding(16.dp)
                )
                {
                    Text(
                        text = menuItem.title,
                        fontSize = 24.sp,
                        fontFamily = KarlaFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = menuItem.description,
                        fontSize = 16.sp,
                        fontFamily = KarlaFont,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF495E57),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Icon(
                            painter = painterResource(id= android.R.drawable.ic_menu_recent_history),
                            contentDescription = "Delivery Time",
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFF495E57)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delivery time: $deliveryTime",
                            fontFamily = KarlaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 1.sp,
                            color = Color.Black
                        )
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor =  Color(0xFF495E57)
                            ),
                            modifier = Modifier.padding(start=4.dp)
                        )
                        {
                            Text(
                                text = "Change",
                                fontSize = 16.sp,
                                fontFamily = KarlaFont,
                                fontWeight = FontWeight.Medium,
                                color = Color(color= 0xFF495E57)
                            )
                        }
                    }
                }
            }

            //Seccion de extras
            if (availableExtras.isNotEmpty())
            {
                item {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    )
                    {
                        Text(
                            text = "Add",
                            fontSize = 18.sp,
                            fontFamily = KarlaFont,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        // Itera sobre cada extra disponible para crear la interfaz de selección
                        availableExtras.forEach { extra->
                            val isSelected = selectedExtras.contains(extra)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable{
                                        // Lógica de toggle: si está seleccionado lo quita, sino lo agrega
                                        selectedExtras = if (isSelected){
                                            selectedExtras - extra // Remueve del listado
                                        } else{
                                            selectedExtras + extra // Agrega al listado
                                        }
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    text = extra.name,
                                    fontSize = 16.sp,
                                    fontFamily = KarlaFont,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                )
                                {
                                    Text(
                                        "$${String.format("%.2f", extra.price)}",
                                        fontSize = 16.sp,
                                        fontFamily = KarlaFont,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )

                                    Checkbox(
                                        checked = isSelected,

                                        onCheckedChange =  { checked ->
                                            selectedExtras = if (checked){
                                                selectedExtras + extra
                                            } else {
                                                selectedExtras - extra
                                            }
                                        },

                                    )
                                }
                            }

                            if(extra != availableExtras.last()){
                                Divider(color= Color.LightGray)
                            }
                        }
                    }
                }
            }
        }

        //Bottom Section con cantidad y boton para agregar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            //Selector de cantidad
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.LightGray, CircleShape)
                )
                {
                    Text(
                        text = "-",
                        fontSize = 30.sp,
                        fontFamily = KarlaFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = quantity.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KarlaFont,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                IconButton(
                    onClick = { quantity++ },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.LightGray, CircleShape)
                )
                {
                    Text(
                        text = "+",
                        fontSize = 30.sp,
                        fontFamily = KarlaFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Boton para agregar al carrito
            Button(
                onClick = { onAddToCart(menuItem, selectedExtras, quantity)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14)),
                shape = RoundedCornerShape(12.dp)
            )
            {
                Text(
                    text = "Add for $${String.format("%.2f", totalPrice)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }


        }
    }
}


