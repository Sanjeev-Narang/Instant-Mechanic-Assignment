package com.narang.instantmechanic

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.narang.instantmechanic.navigation.HomeDestination
import com.narang.instantmechanic.navigation.Navigator
import com.narang.instantmechanic.navigation.ProfileDestination
import com.narang.instantmechanic.navigation.RequestsDestination
import com.narang.instantmechanic.ui.HomeFragment
import com.narang.instantmechanic.ui.MechanicDetailsFragment
import com.narang.instantmechanic.ui.ProfileFragment
import com.narang.instantmechanic.ui.RequestsFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Bars are transparent via theme; enforce no contrast scrim so content
        // scrolls visibly behind them.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
            window.isStatusBarContrastEnforced = false
        }
        // Light theme (bg is light) -> dark icons. Night theme handles
        // light icons via values-night (windowLightStatusBar=false).
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    openTopLevelScreen(HomeFragment::class.java, HomeDestination)
                    true
                }
                R.id.navigation_requests -> {
                    openTopLevelScreen(RequestsFragment::class.java, RequestsDestination)
                    true
                }
                R.id.navigation_profile -> {
                    openTopLevelScreen(ProfileFragment::class.java, ProfileDestination)
                    true
                }
                else -> {
                    Timber.w("MainActivity unknown menuItem %s", menuItem.itemId)
                    false
                }
            }
        }

        if (savedInstanceState == null) {
            Navigator.navigate(supportFragmentManager, HomeDestination, addToBackStack = false)
            supportFragmentManager.executePendingTransactions()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateBottomNavigationState()
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fragmentManager: FragmentManager,
                    fragment: Fragment,
                    view: View,
                    savedInstanceState: Bundle?,
                ) {
                    updateBottomNavigationState()
                }
            },
            false
        )

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })

        updateBottomNavigationState()
    }

    private fun openTopLevelScreen(
        fragmentClass: Class<out Fragment>,
        destination: com.narang.instantmechanic.navigation.Destination,
    ) {
        val currentFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container)

        if (!fragmentClass.isInstance(currentFragment)) {
            Navigator.navigateToRoot(supportFragmentManager, destination)
        }
    }

    private fun updateBottomNavigationState() {
        val currentFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container)

        bottomNavigationView.visibility =
            if (currentFragment is MechanicDetailsFragment) View.GONE else View.VISIBLE

        val menuItemId = when (currentFragment) {
            is HomeFragment -> R.id.navigation_home
            is RequestsFragment -> R.id.navigation_requests
            is ProfileFragment -> R.id.navigation_profile
            else -> {
                return
            }
        }

        if (bottomNavigationView.selectedItemId != menuItemId) {
            bottomNavigationView.selectedItemId = menuItemId
        }
    }
}
