package com.example.littlelemon


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemon.R


val KarlaFont = FontFamily(
    Font(R.font.karla_extralight, FontWeight.ExtraLight),
    Font(R.font.karla_regular, FontWeight.Normal),
    Font(R.font.karla_bold, FontWeight.Bold),
    Font(R.font.karla_medium, FontWeight.Medium),
)


@Composable
fun Onboarding(){
    /*
        Remember ayuda a que el valor del estado persista durante la recomposicion
        (Reconstruccion del UI cuando los datos cambian) del composable.

        MutableStateOf crea el estado para que cuando un valor cambie (en este caso la informacion que agrega el usuario),
        la UI se actualice

        TextField Value es el valor inicial que se le dara al Text Field
     */
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    //Single column layout
    Column(
        //Arguments for the column class
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.little_lemon_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp, start = 16.dp, end = 16.dp)
                .height(50.dp),
            contentScale = ContentScale.Crop
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF495E57)),

            contentAlignment = Alignment.Center
        ){
            Text(
                text="Let's get to know you",
                fontFamily = KarlaFont,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.White)
        }
        Spacer(modifier = Modifier.height(60.dp))



        Text(
            text="Personal information",
            fontSize = 20.sp,
            fontFamily = KarlaFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = firstName,
            //Update firstName var everytime the user inputs text
            onValueChange = { firstName = it },
            label = { Text("First Name", fontFamily = KarlaFont,
                fontWeight = FontWeight.Normal) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = {lastName = it},
            label = { Text("Last Name", fontFamily = KarlaFont,
                fontWeight = FontWeight.Normal)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp)
        )
        Spacer(modifier = Modifier.height(35.dp))


        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Email", fontFamily = KarlaFont,
                fontWeight = FontWeight.Normal)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp)
        )
        Spacer(
            modifier = Modifier
                .height(70.dp)
                .weight(1f)
        )

        OutlinedButton (
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom =  16.dp, start = 16.dp, end = 16.dp),
            border = BorderStroke(2.dp, Color(0xFFEE9972)),
        ) {
            Text("Register", fontFamily = KarlaFont, fontSize = 20.sp,
                fontWeight = FontWeight.Normal, color = Color.Black)
        }



    }

}


@Preview(showBackground = false)
@Composable
fun OnboardingPreview(){
    Onboarding()
}




