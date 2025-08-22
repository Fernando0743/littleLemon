package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/*
NavController is responsible for navigating between destinations while keeping track and manipulating the back stack.

NavGraph represents the collection of navigable destinations and navigation routes.
It is provided to NavHost for building the navigation graph.

NavHost serves as a container that takes the NavController as an input parameter and associates it with the NavGraph,
which contains the destinations that can be navigated.

Destination represents a navigation destination. Navigating to a destination is done using a NavController.

The startDestination is the first screen that the user will encounter.
The above components will allow navigation between various navigation destinations called screens
 and enable back-button navigation.

 */

//Data class para los extras de cada producto
data class ProductExtra(
    val name: String,
    val price: Double
)

//Data class para items en el carrito
data class CartItem(
    val menuItem: MenuItemEntity,
    val extras: List<ProductExtra>,
    val quantity: Int
) {
    //Funcion para calcular el precio total
    val totalPrice: Double
        get() {
            val basePrice = menuItem.price.replace("$", "").toDoubleOrNull() ?: 0.0
            val extrasPrice = extras.sumOf { it.price }
            return (basePrice + extrasPrice) * quantity
        }

    val extrasText: String
        get() = if(extras.isNotEmpty()){
            extras.joinToString(", ") { it.name }
        } else ""
}

//Singleton para manejar el estado del carrito globalmente
object CartManager{
    private var _cartItems = mutableListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems.toList()

    fun addtoCart(cartItem : CartItem){
        _cartItems.add(cartItem)
    }

    fun clearCart(){
        _cartItems.clear()
    }
}

@Composable
fun navigationComposable(navController: NavHostController, isUserLoggedIn: Boolean, context: Context, db: AppDatabase){
    // Observa cambios en la base de datos y mantiene la lista actualizada reactivamente
    val menuItems by db.menuItemDao()
        .getAllMenuItems()
        .collectAsState(initial = emptyList())

    //Create NavHost
    NavHost (
        navController = navController,
        startDestination = if (isUserLoggedIn) HomeDestination.route else OnBoardingDestination.route
    ){
        //Define Destinations for navGraph
        composable(OnBoardingDestination.route)
        {
            Onboarding(navController)
        }

        composable(HomeDestination.route)
        {
            //When user is on Home Screen and clicks Profile, we take them to profile screen
            HomeScreen(
                onProfileClick = { navController.navigate(ProfileDestination.route) },
                onMenuItemClick = { menuItem ->
                    navController.navigate("${ProductDetailDestination.route}/${menuItem.id}")
                },
                db = db
            )
        }

        composable(ProfileDestination.route)
        {
            ProfileScreen(
                //When user presses back button on the profile screen we take them to home screen
                onBackClick ={ navController.navigate(HomeDestination.route) },

                //When a user presses the Logout button on the Profile destination
                // the app should navigate from the Profile destination to the Onboarding destination.
                onLogoutClick = {
                    //Limpiamos toda la informacion en shared preferences, la informacion del usuario y el boolean de logged int
                    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .clear()
                        .apply()

                    CartManager.clearCart()

                    //Navegarmos a OnboardingScreen y limpiamos el stack de navegacion
                    navController.navigate(OnBoardingDestination.route){
                        popUpTo(0) //Clean stack

                    }
                }
            )
        }

        //Nueva ruta para product Detail
        composable(ProductDetailDestination.routeWithArgs) { backStackEntry ->
            val menuItemId =
                backStackEntry.arguments?.getString(ProductDetailDestination.menuItemIdArg)
                    ?.toIntOrNull()
            val menuItem = menuItems.find { it.id == menuItemId }

            if (menuItem != null) {
                println("Going to new menuitem")
                ProductDetailScreen(
                    menuItem = menuItem,
                    onBackClick = { navController.popBackStack() },
                    onAddToCart = { item, extras, quantity ->
                        val cartItem = CartItem(item, extras, quantity)
                        CartManager.addtoCart(cartItem)
                        navController.navigate(CheckoutDestination.route)
                    }
                )
            }
            println("Menu ITEM IS NULL")
        }

        composable(CheckoutDestination.route){
            CheckoutScreen(
                cartItems = CartManager.cartItems,
                db = db,
                onBackClick = { navController.popBackStack() },
                onMenuItemClick = { menuItem ->
                    navController.navigate("${ProductDetailDestination.route}/${menuItem.id}")
                },
                onProfileClick = { navController.navigate(ProfileDestination.route)}
            )
        }
    }
}