package com.github.mazar1ni.tasktracker.core.util

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.github.mazar1ni.tasktracker.R

class NavigationUtil {

    private lateinit var navController: NavController

    fun init(activity: Activity): NavController {
        navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
        return navController
    }

    fun navigate(navDirections: NavDirections) {
        navController.navigate(navDirections)
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}