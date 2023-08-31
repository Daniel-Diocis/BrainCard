package com.example.braincard

import android.content.ClipData
import com.example.braincard.database.BrainCardDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.braincard.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView

import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = Room.databaseBuilder(this, BrainCardDatabase::class.java, "BrainCard").build()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //Serve ad evitare il backstack del fragment
        findViewById<BottomNavigationItemView>(R.id.navigation_home).setOnClickListener{
            navController.navigate(R.id.navigation_home)
        }
        findViewById<BottomNavigationItemView>(R.id.navigation_dashboard).setOnClickListener{
            navController.navigate(R.id.navigation_dashboard)
        }
        findViewById<BottomNavigationItemView>(R.id.navigation_home).setOnClickListener{
            navController.navigate(R.id.navigation_home)
        }
    }
}