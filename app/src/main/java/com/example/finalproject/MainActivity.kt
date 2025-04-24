package com.example.finalproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.finalproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkUserLoggedIn()

        viewModel.isUserLoggedIn.observe(this@MainActivity) { isLoggedIn ->
            if (isLoggedIn) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.HomePageFragment)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment,
                R.id.signUpFragment,
//                R.id.fullScreenFragment
                -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.HomePageFragment)
                    true
                }
                R.id.action_profile -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.ProfileFragment)
                    true
                }
                R.id.action_search -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.SearchBookAPIFragment)
                    true
                }
                R.id.action_add -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.AddingPostFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
