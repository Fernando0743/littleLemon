package com.example.littlelemon

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    //Client HTTP Ktor con soporte JSON. Ktor se usa porque permite hacer requests
    /*
    Ktor es una librería para hacer peticiones HTTP en Kotlin (similar a Retrofit, pero más flexible y ligera).

    ContentNegotiation: Este plugin se encarga de convertir automáticamente la respuesta del servidor
    (en JSON, XML, etc.) a tus clases Kotlin (data class).

    json(contentType = ContentType("text", "plain")):
    Normalmente el servidor enviaría application/json, pero en este caso el JSON de Meta está con text/plain,
    así que debemos forzar a que lo interprete como JSON igual.
     */
    private val client = HttpClient(Android) {
        install(ContentNegotiation){
            json(contentType = ContentType("text", "plain"))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("🚀 MainActivity onCreate iniciado")


        //Construimos la base de datos de nuestra app
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "little_lemon_db"
        ).fallbackToDestructiveMigration()
            .build()

        println("📱 Base de datos creada")



        //Get shared Preferences to check if user is logged in
        val sharedPreferences = getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        println("👤 Usuario loggeado: $isLoggedIn")


        // Lanzar fetch del menú en un hilo de IO
        /*
        Por qué CoroutineScope: Las peticiones HTTP deben hacerse fuera del hilo principal para no congelar la UI.

        Dispatchers.IO: Se usa para operaciones de I/O (Input/Output), como llamadas a red o base de datos.

        launch: Lanza una coroutine sin bloquear el flujo del onCreate.

        En lugar de CoroutineScope(Dispatchers.IO) podrías usar lifecycleScope.launch(Dispatchers.IO)
        para que la coroutine se cancele automáticamente si la Activity se destruye.
         */
        lifecycleScope.launch(Dispatchers.IO) {
            println("🌐 Iniciando descarga del menú...")

            fetchMenu()
        }

        setContent {
            //We only define the navigation composable as it will help us through the whole workflow of the app
            val navController = rememberNavController()
            navigationComposable(navController, isLoggedIn, this, db)
        }
    }


    /*
    suspend significa que esta función puede pausarse y reanudar su ejecución, necesaria para operaciones asíncronas.

    client.get(url).body<MenuNetworkData>():

    Hace una petición GET a la URL.

    Convierte la respuesta JSON directamente en un objeto MenuNetworkData gracias al plugin ContentNegotiation.
     */
    private suspend fun fetchMenu(){
        try{
            println("📡 Haciendo petición HTTP...")

            val url = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"


            //Decodificamos el Json con KTOR y serialization
            val menuData: MenuNetworkData = client.get(url).body()
            println("📋 Datos recibidos: ${menuData.menu.size} items")


            println(menuData)


            //Mapeamos cada MenuItemNetwork (extraido de url) a MenuItemEntity (Una fila de nuestra base de datos local)
            val menuEntities = menuData.menu.map {
                println("🍽️ Procesando: ${it.title}")
                MenuItemEntity(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    image = it.image,
                    category = it.category
                )
            }

            db.menuItemDao().insertAll(menuEntities)
            println("💾 ${menuEntities.size} items guardados en base de datos")



        } catch (e: Exception){
            println("❌ ERROR en fetchMenu: ${e.message}")
            e.printStackTrace()
        }
    }
}

