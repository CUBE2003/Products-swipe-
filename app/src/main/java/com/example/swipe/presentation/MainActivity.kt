package com.example.swipe.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.swipe.R
import com.example.swipe.data.remote.NetworkHelper
import com.example.swipe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!NetworkHelper(this).isNetworkConnected()){
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show()
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.fab.visibility = View.VISIBLE

        // Initialize navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController


        // Set up FAB click listener
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_productFragment_to_createProductFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productFragment -> binding.fab.visibility = View.VISIBLE
                R.id.createProductFragment -> binding.fab.visibility = View.GONE
                else -> binding.fab.visibility = View.GONE
            }
        }
    }


}

