package dev.inteiintel.teduhserviceapp.utils.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Onboarding: Screen("onboarding")
    object Auth: Screen("auth")
    object Main: Screen("main")

    // Navigation Bottom Object
    object Home: Screen("home")
    object Queues: Screen("queues")
    object Notifications: Screen("notifications")
    object Settings: Screen("settings")

    object TestScreen: Screen("test")
    object HorizontalPager: Screen("pager")
}