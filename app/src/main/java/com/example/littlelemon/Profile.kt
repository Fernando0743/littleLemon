package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


    //Singel column layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //IMAGE
        Image(
            painter = painterResource(id = R.drawable.little_lemon_logo),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier
                .padding(top=30.dp)
                .height(50.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(60.dp))

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
        Spacer(modifier = Modifier.height(30.dp))

        //User Information
        ProfileTextField(label = "First Name", value = firstName)
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(label = "Last Name", value = lastName)
        Spacer(modifier = Modifier.height(30.dp))
        ProfileTextField(label = "Email", email)

        Spacer(
            modifier = Modifier
                .height(70.dp)
                .weight(1f)
        )

        //Logout Button
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom =  16.dp),
            border = BorderStroke(2.dp, Color(0xFFEE9972)),
        ) {
            Text("Log out", fontFamily = KarlaFont, fontSize = 20.sp,
                fontWeight = FontWeight.Normal, color = Color.Black)
        }





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