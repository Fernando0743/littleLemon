package com.example.littlelemon


import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.R


val KarlaFont = FontFamily(
    Font(R.font.karla_extralight, FontWeight.ExtraLight),
    Font(R.font.karla_regular, FontWeight.Normal),
    Font(R.font.karla_bold, FontWeight.Bold),
    Font(R.font.karla_medium, FontWeight.Medium),
    Font(R.font.karla_extrabold, FontWeight.ExtraBold),
    Font(R.font.karla_italic, FontWeight.Thin)
)

val MarkaziFont = FontFamily(
    Font(R.font.markazitext_bold, FontWeight.Bold),
    Font(R.font.markazitext_semibold, FontWeight.SemiBold),
    Font(R.font.markazitext_medium, FontWeight.Medium),
    Font(R.font.markazitext_regular, FontWeight.Normal),
)

@Composable
fun Onboarding(navController: NavHostController){
    //Var to control what screen to show
    var showWelcomeScreen by remember { mutableStateOf(true) }

    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)

    //Determine when user clicks login or signup button we show registration form.

    if (showWelcomeScreen)
    {
        WelcomeScreen(
            onLoginClick = { showWelcomeScreen = false},
            onSignUpClick = {showWelcomeScreen = false }
        )
    }
    else
    {
        RegistrationScreen(
            navController = navController,
            firstName = firstName,
            lastName = lastName,
            email = email,
            onFirstNameChange = { firstName = it},
            onLastNameChange = {lastName = it},
            onEmailChange = {email = it},
            context = context,
            sharedPreferences = sharedPreferences

        )
    }



}

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Logo
        Image(
            painter = painterResource(id = R.drawable.little_lemon_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp, start = 16.dp, end = 16.dp)
                .height(50.dp),
            contentScale = ContentScale.Crop
        )
        //Hero Section
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
        Spacer(modifier = Modifier.height(40.dp))
        //Review Card Section
        //Stars
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        )
        {
            repeat(5){
                Text(
                    text = "★",
                    color = Color(0xFFFFD700),
                    fontSize = 20.sp
                )
            }
        }
        //Review Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        )
        {
            Column(
                modifier = Modifier.padding(16.dp)
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                {
                    //User avatar
                    Box(
                       modifier = Modifier
                           .size(40.dp)
                           .background(Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text("SL", color = Color.White, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Sara Lopez",
                            fontFamily = KarlaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Sara72",
                            fontFamily = KarlaFont,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = "\"Seriously cannot stop thinking about the Turkish Mac n' Cheese!!\"",
                    fontFamily = KarlaFont,
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Thin
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        //Seccion are you hungry?
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFD700))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = "Are you Hungry?",
                    fontFamily = KarlaFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                //3 PUNTOS
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Black, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Gray, CircleShape)
                    )
                }


                //Botones Log in y SignUp
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495E57)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                    {
                        Text(
                            text = "Log in",
                            fontFamily = KarlaFont,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = onSignUpClick,
                        border = BorderStroke(2.dp, Color(0xFF495E57)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        )
                    {
                        Text(
                            text = "Sign Up",
                            fontFamily = KarlaFont,
                            fontSize = 16.sp,
                            color = Color.Black,

                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RegistrationScreen(
    navController: NavHostController,
    firstName: TextFieldValue,
    lastName: TextFieldValue,
    email: TextFieldValue,
    onFirstNameChange: (TextFieldValue) -> Unit,
    onLastNameChange: (TextFieldValue) -> Unit,
    onEmailChange: (TextFieldValue) -> Unit,
    context: Context,
    sharedPreferences: android.content.SharedPreferences
)
{
    /*
        Remember ayuda a que el valor del estado persista durante la recomposicion
        (Reconstruccion del UI cuando los datos cambian) del composable.

        MutableStateOf crea el estado para que cuando un valor cambie (en este caso la informacion que agrega el usuario),
        la UI se actualice

        TextField Value es el valor inicial que se le dara al Text Field

        SharedPreferences es una forma simple de almacenar pares clave–valor persistentes (pequeños datos)
        en el almacenamiento interno de la app.

    Persiste entre ejecuciones de la app (se guarda en un XML en /data/data/tu.paquete/shared_prefs/).
    Ideal para flags, configuraciones y datos pequeños (booleans, strings, ints, floats, longs).
    No es adecuado para grandes cantidades de datos ni para datos sensibles sin cifrado.

    La funcion GetSharedPreferences nos permite  acceder a estos datos desde cualquier parte de la app, asi como asignar
    un nombre personalizado al archivo donde se guardaran estos datos, ocupamos el contexto de la aplicación para que android
    sepa desde donde acceder a los recursos y al almacenamiento. Similar a this o self en python.
    Mode nos indica como se abrira este archivo de preferencias, en este caso el modo privado nos dice que olo nuestra app puede
    leerlo y escribirlo
     */

    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    var message by remember { mutableStateOf("") }

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
            onClick = {
                if(firstName.text.isBlank() || lastName.text.isBlank() || email.text.isBlank())
                {
                    message = "Registration unsuccesful. Please enter all data"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
                else
                {
                    //Save that user is now logged in on Shared PrEFERENCES and user information
                    //apply() guarda de forma asíncrona (no bloquea el hilo UI). commit() guarda sincrónicamente y devuelve Boolean
                    sharedPreferences.edit().apply {
                        putBoolean("isLoggedIn", true)
                        putString("firstName", firstName.text)
                        putString("lastName", lastName.text)
                        putString("email", email.text)
                            .apply()
                    }

                    //Go to home screen
                    message = "Registration succesful!"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    navController.navigate(HomeDestination.route)
                }
            },
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
    //Simulamos NavController
    val navController = rememberNavController()
    Onboarding(navController)
}




