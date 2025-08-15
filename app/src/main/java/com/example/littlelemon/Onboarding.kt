package com.example.littlelemon


import android.content.Context
import android.os.Build
import android.widget.Toast
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
    Font(R.font.karla_extrabold, FontWeight.ExtraBold)
)

val MarkaziFont = FontFamily(
    Font(R.font.markazitext_bold, FontWeight.Bold),
    Font(R.font.markazitext_semibold, FontWeight.SemiBold),
    Font(R.font.markazitext_medium, FontWeight.Medium),
    Font(R.font.markazitext_regular, FontWeight.Normal),
)

@Composable
fun Onboarding(navController: NavHostController){
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

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)

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




