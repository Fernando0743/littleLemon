package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
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

@Composable
fun navigationComposable(navController: NavHostController, isUserLoggedIn: Boolean, context: Context){
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
                onProfileClick = {navController.navigate(ProfileDestination.route)}
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
                    //Guardamos en SharedPreferences que el usuario ya no esta loggeado
                    val sharedPreferences = context.getSharedPreferences("LittleLemonPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .putBoolean("isLoggedIn", false)
                        .apply()

                    //Navegarmos a OnboardingScreen y limpiamos el stack de navegacion
                    navController.navigate(OnBoardingDestination.route){
                        popUpTo(0) //Clean stack

                    }

                }
            )
        }

    }
}