package com.example.littlelemon


/*
Destinations file, este archivo contiene los objectos de cada uno de los destinos (Screens)
de nuestra aplicación, se suele declarar en una interfaz cuyo unico atributo es la ruta de la
pantalla, y cada objeto contiene la ruta.

Una interfaz a diferenca de una clase, es un contraro que define que metodos debe de tener una clase
 */

interface Destination{
    val route: String
}

object OnBoardingDestination : Destination{
    override val route = "Onboarding"
}

object HomeDestination : Destination{
    override val route = "Home"
}

object ProfileDestination : Destination{
    override val route = "Profile"
}

object ProductDetailDestination: Destination{
    override val route = "ProductDetail"
    const val menuItemIdArg = "menuItemId"
    val routeWithArgs = "$route/{$menuItemIdArg}"
}

object CheckoutDestination: Destination{
    override val route = "Checkout"
}