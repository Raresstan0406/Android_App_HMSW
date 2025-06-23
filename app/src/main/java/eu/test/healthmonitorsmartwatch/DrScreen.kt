package eu.test.healthmonitorsmartwatch

import androidx.annotation.DrawableRes
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel

sealed class DrScreen(val title: String, val route:String) {

    sealed class DrawerScreen(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int)
        : DrScreen(dTitle, dRoute){
            object Home: DrawerScreen(
                "Home",
                "home",
                R.drawable.baseline_home_24
            )
            object HealthValues: DrawerScreen(
                "Health Values",
                "healt values",
                R.drawable.baseline_health_and_safety_24
            )
            object Search: DrawerScreen(
                "Search",
                "search",
                R.drawable.baseline_person_add_24
            )
            object ChatRooms: DrawerScreen(
                "Chatrooms",
                "chatrooms",
                R.drawable.baseline_chat_24
            )
        }
}
fun getScreensInDrawer(authViewModel: AuthViewModel): List<DrScreen.DrawerScreen> {
    val isPacient = authViewModel.currentUserAccountType.value == "Pacient"

    val doctorScreens = listOf(
        DrScreen.DrawerScreen.Search,
        DrScreen.DrawerScreen.ChatRooms
    )

    val pacientScreens = if (isPacient) listOf(DrScreen.DrawerScreen.Home,
        DrScreen.DrawerScreen.HealthValues) else emptyList()

    return pacientScreens + doctorScreens
}