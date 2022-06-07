package com.github.mazar1ni.tasktracker.core.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.ui.setupWithNavController
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.core.util.preferences.ApplicationPreferences
import com.github.mazar1ni.tasktracker.core.util.preferences.PreferencesType
import com.github.mazar1ni.tasktracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigationUtil: NavigationUtil

    @Inject
    lateinit var applicationPreferences: ApplicationPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = navigationUtil.init(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_tasks -> navView.visibility = View.VISIBLE
                R.id.navigation_notes -> navView.visibility = View.VISIBLE
                R.id.navigation_settings -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }

        navView.setupWithNavController(navController)

        if (applicationPreferences.contains(PreferencesType.AccessToken))
            navController.navigate(R.id.action_fragment_login_to_navigation_tasks)
    }
}