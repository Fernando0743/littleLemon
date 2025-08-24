package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,

) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)
    val firstName = sharedPreferences.getString("firstName", "") ?: "" //Try to get user information, if not we display it as empty
    val lastName = sharedPreferences.getString("lastName", "") ?: "" //Try to get user information, if not we display it as empty
    val email = sharedPreferences.getString("email", "") ?: "" //Try to get user information, if not we display it as empty

    var orderStatusNotifications by remember {mutableStateOf<Boolean>(sharedPreferences.getBoolean("orderStatusNotifications", true)) }
    var passwordChangeNotifications by remember { mutableStateOf<Boolean>(sharedPreferences.getBoolean("passwordChangeNotifications", true)) }
    var specialOffersNotifications by remember { mutableStateOf<Boolean>(sharedPreferences.getBoolean("specialOffersNotifications", true)) }



    //Singel column layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        ProfileHeader(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        //PERSONAL INFORMATION
        Text(
            text = "Personal Information",
            fontSize = 20.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        //Avatar Section
        AvatarSection()

        Spacer(modifier = Modifier.height(24.dp))


        //User Information
        ProfileTextField(label = "First Name", value = firstName)
        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(label = "Last Name", value = lastName)
        Spacer(modifier = Modifier.height(16.dp))


        ProfileTextField(label = "Email", email)
        Spacer(modifier = Modifier.height(32.dp))

        //Email Notifications Section
        EmailNotificationsSection(
            orderStatusChecked = orderStatusNotifications,
            onOrderStatusChanged = {
                orderStatusNotifications = it
                // Save to SharedPreferences when changed
                sharedPreferences.edit().putBoolean("orderStatusNotifications", it).apply()
            },
            passwordChangeChecked = passwordChangeNotifications,
            onPasswordChangeChanged = {
                passwordChangeNotifications = it
                // Save to SharedPreferences when changed
                sharedPreferences.edit().putBoolean("passwordChangeNotifications", it).apply()
            },
            specialOffersChecked = specialOffersNotifications,
            onSpecialOffersChanged = {
                specialOffersNotifications = it
                // Save to SharedPreferences when changed
                sharedPreferences.edit().putBoolean("specialOffersNotifications", it).apply()
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        //Logout Button
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom =  16.dp),
            shape = RoundedCornerShape(8.dp)

        )
        {
            Text(
                text ="Log out",
                fontFamily = KarlaFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ProfileHeader(onBackClick: () -> Unit)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        // Little Lemon Logo
        Image(
            painter = painterResource(id = R.drawable.little_lemon_logo),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier.height(50.dp),
            contentScale = ContentScale.Crop
        )

        // Profile Icon
        Button(
            onClick = { },
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
fun AvatarSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Avatar",
            fontSize = 14.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar Image - using placeholder person icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Change Button
            OutlinedButton(
                onClick = { /* Handle avatar change */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFF495E57),
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFF495E57)),
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "Change",
                    fontSize = 14.sp,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Remove Button
            OutlinedButton(
                onClick = { /* Handle avatar removal */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Gray
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "Remove",
                    fontSize = 14.sp,
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun EmailNotificationsSection(
    orderStatusChecked: Boolean,
    onOrderStatusChanged: (Boolean) -> Unit,
    passwordChangeChecked: Boolean,
    onPasswordChangeChanged: (Boolean) -> Unit,
    specialOffersChecked: Boolean,
    onSpecialOffersChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Email notifications",
            fontSize = 18.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Order statuses checkbox
        NotificationCheckboxItem(
            text = "Order statuses",
            checked = orderStatusChecked,
            onCheckedChange = onOrderStatusChanged
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password changes checkbox
        NotificationCheckboxItem(
            text = "Password changes",
            checked = passwordChangeChecked,
            onCheckedChange = onPasswordChangeChanged
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Special offers checkbox
        NotificationCheckboxItem(
            text = "Special offers",
            checked = specialOffersChecked,
            onCheckedChange = onSpecialOffersChanged
        )
    }
}

@Composable
fun NotificationCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF495E57),
                uncheckedColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}


@Composable
fun ProfileTextField(label: String, value: String){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 20.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen({}, {})
}