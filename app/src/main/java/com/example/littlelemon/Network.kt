package com.example.littlelemon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
The first thing that must be recapped is that most mobile apps communicate with the back-end to retrieve data.
Many mobile apps depend on data that is hosted on a remote server.
To load that data in your applications, you will have to make an HTTP GET request to fetch it from the server.
This can be achieved by using one of the popular networking libraries such as Ktor.
The Ktor library can make network requests and handle network responses,
meaning you can retrieve the data from a remote server.
For example, your Little Lemon food ordering app will download the restaurant menu from an external endpoint.

Once the data is retrieved, it can then be parsed and processed by the app.
Ktor can parse JSON data into Kotlin objects.
To parse the received JSON data the kotlinx serialization needs to be configured.
 A data class will need to have a @Serializable annotation as well as the @SerialName annotation for each field.

 EXAMPLE OF DATA FETCHED FROM SERVER FOR LITTLE LEMON APP:
 {
  "menu": [
    {
      "id": 1,
      "title": "Greek Salad",
      "description": "The famous greek salad of crispy lettuce, peppers, olives, our Chicago.",
      "price": "10",
      "image": "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/greekSalad.jpg?raw=true",
      "category": "starters"
    },
    {
      "id": 2,
      "title": "Lemon Desert",
      "description": "Traditional homemade Italian Lemon Ricotta Cake.",
      "price": "10",
      "image": "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/lemonDessert%202.jpg?raw=true",
      "category": "desserts"
    },
 }

 Serializable y SerialName permite que Kotlin puede convertir el JSON a clases Kotlin

 Se usa data class porque la data class esta pensada para representar meramente datos sin ninguna logica interna,
 es decir que permite almacenar y transportar datos, tambien es facil serializar.

 . These classes contain data classes that are used to decode the object received from the server
 */


//Class represents whole response from the server as seen above
@Serializable
data class MenuNetworkData(
    @SerialName("menu")
    val menu: List<MenuItemNetwork>
)




//Represents each dish inside menu array
@Serializable
data class MenuItemNetwork(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: String,
    @SerialName("image") val image: String,
    @SerialName("category") val category: String
)


