package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    db: AppDatabase,
    onBackClick: () -> Unit,
    onMenuItemClick: (MenuItemEntity) -> Unit,
    onProfileClick: () -> Unit
){
    var showSuccessDialog by remember { mutableStateOf(false) }


    //Observa todos los menu items para mostrar en "Add more to your order"
    val allMenuItems by db.menuItemDao()
        .getAllMenuItems()
        .collectAsState(initial = emptyList())

    //Cálculo de precios
    val subtotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = 2.00
    val serviceFee = 1.00
    val total = subtotal + deliveryFee + serviceFee

    if (showSuccessDialog)
    {
        SuccessDialog(
            cartItems = cartItems,
            onTrackOrderClick = {
                //Handle track order navigation
                showSuccessDialog = false
            }
        )
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        //Header
        item{
            CheckoutTopBar(
                onBackClick = onBackClick,
                onProfileClick = onProfileClick
            )
        }

        //DeliveryTimeSection
        item{
            DeliveryTimeSection()
        }

        //Cutlery Section
        item {
            CutlerySection()
        }

        //Order Summary
        item{
            OrderSummarySection(cartItems = cartItems)
        }

        //Add More to your Order Section
        item{
            AddMoreSection()
        }

        //Menu items grid/list
        items(allMenuItems.chunked(2)) { rowItems ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                rowItems.forEach { item->
                    Box(modifier = Modifier.weight(1f))
                    {
                        AddMoreMenuItem(
                            item = item,
                            onItemClick = onMenuItemClick
                        )
                    }
                }

                //Rellenar espacio si numero impar de items
                if(rowItems.size == 1)
                {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        //Price breakdown
        item{
            PriceBreakdownSection(
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                serviceFee = serviceFee,
                total = total
            )
        }

        //Checkout button
        item{
            CheckoutButton(
                onCheckoutClick = {showSuccessDialog = true}
            )
        }

    }
}

@Composable
fun SuccessDialog(
    cartItems: List<CartItem>,
    onTrackOrderClick: () -> Unit
)
{
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        )
        {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //Success Icon and text
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color(0xFFE8F5E8),
                            shape =CircleShape
                        ),
                    contentAlignment = Alignment.Center
                )
                {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_send),
                        contentDescription = "Success",
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Success!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = KarlaFont,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your order\nwill be with you shortly.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                //Order summary in dialog
                Column(
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text(
                        text = "Items",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = KarlaFont,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    cartItems.forEach { cartItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(
                                text = "${cartItem.quantity} x ${cartItem.menuItem.title}",
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = KarlaFont,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "$${String.format("%.2f", cartItem.totalPrice)}",
                                fontSize = 14.sp,
                                fontFamily = KarlaFont,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                //Track Order Button
                Button(
                    onClick = onTrackOrderClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14)),
                    shape =RoundedCornerShape(8.dp)
                )
                {
                    Text(
                        text = "Track Order",
                        fontSize = 16.sp,
                        fontFamily = KarlaFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }





            }
        }
    }
}


@Composable
fun CheckoutTopBar(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=30.dp, start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        //Back Button
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

        //Little Lemon Logo
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = painterResource(id= R.drawable.little_lemon_logo),
                contentDescription = "Logo",
                modifier = Modifier.height(50.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(
            onClick = onProfileClick,
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text(text = "👤", fontSize = 20.sp)
        }
    }
}

@Composable
fun DeliveryTimeSection(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
            contentDescription = "Delivery Time",
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Delivery time: 20 minutes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = KarlaFont,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color(0xFF495E57)
            )
        )
        {
            Text(
                text = "Change",
                fontFamily = KarlaFont,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun CutlerySection()
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
        Text(
            text = "Cutlery",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = KarlaFont,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Help reduce plastic waste, only",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "ask if you need it",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Normal
                )
            }
            RadioButton(
                selected = true,
                onClick = {}
            )
        }
    }
}

@Composable
fun OrderSummarySection(cartItems: List<CartItem>)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
        Text(
            text = "Order Summary",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = KarlaFont,
            color = Color.Black,
            modifier =Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(0.dp)
        )
        {
            Column(
                modifier = Modifier.padding(16.dp)
            )
            {
                Text(
                    text = "Items",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = KarlaFont,
                    color = Color.Black,
                    modifier =Modifier.padding(bottom = 8.dp)
                )

                cartItems.forEach { cartItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Column(
                            modifier = Modifier.weight(1f)
                        )
                        {
                            Text(
                                text = "${cartItem.quantity} x ${cartItem.menuItem.title}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = KarlaFont,
                                color = Color.Black,
                            )
                            if(cartItem.extrasText.isNotEmpty())
                            {
                                Text(
                                    text = cartItem.extrasText,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontFamily =  KarlaFont,
                                    fontWeight = FontWeight.Thin
                                )
                            }
                        }
                        Text(
                            text = "$${String.format("%.2f", cartItem.totalPrice)}",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily =  KarlaFont,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMoreSection()
{
    Text(
        text = "Add More To Your Order!",
        fontSize = 18.sp,
        color = Color.Black,
        fontFamily =  KarlaFont,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddMoreMenuItem(
    item: MenuItemEntity,
    onItemClick: (MenuItemEntity) -> Unit
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable{ onItemClick(item) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    )
    {
        Column(
            modifier = Modifier.padding(8.dp)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            {
                GlideImage(
                    model = item.image,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                fontFamily = KarlaFont,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = item.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                maxLines = 2,
                fontFamily = KarlaFont,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "$${item.price}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = KarlaFont,
                color = Color(0xFF495E57),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun PriceBreakdownSection(
    subtotal: Double,
    deliveryFee: Double,
    serviceFee: Double,
    total: Double
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
        PriceRow("Subtotal", subtotal)
        PriceRow("Delivery", deliveryFee)
        PriceRow("Service", serviceFee)

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                text = "Total",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = KarlaFont,
                color = Color.Black
            )

            Text(
                text = "$${String.format("%.2f", total)}",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = KarlaFont,
                color = Color(0xFF495E57)
            )

        }
    }
}



@Composable
fun PriceRow(label: String, amount: Double)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )

        Text(
            text = "$${String.format("%.2f", amount)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = KarlaFont,
            color = Color.Black
        )
    }
}

@Composable
fun CheckoutButton(
    onCheckoutClick: () -> Unit
)
{
    Button(
        onClick = { onCheckoutClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14)),
        shape = RoundedCornerShape(12.dp)
    )
    {
        Text(
            text = "Checkout",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}