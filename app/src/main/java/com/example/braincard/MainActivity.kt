package com.example.braincard

import android.content.ClipData
import android.content.pm.PackageManager
import com.example.braincard.database.BrainCardDatabase
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.braincard.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            if(auth.currentUser!=null) {
                navController.navigate(R.id.navigation_dashboard)}
            else {
                navController.navigate(R.id.loginFragment)
            }
        }
        findViewById<BottomNavigationItemView>(R.id.navigation_notifications).setOnClickListener{
            Log.e("CONTROLLO",(ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED).toString())
                if (auth.currentUser != null) navController.navigate(R.id.navigation_notifications)
                else {
                    navController.navigate(R.id.loginFragment)
                }
        }
        this.actionBar?.setHomeButtonEnabled(true)
        this.supportActionBar?.setHomeButtonEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
               when (findNavController(R.id.nav_host_fragment_activity_main).currentDestination?.id){
                   R.id.gruppoFragment->{
                       findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_gruppoFragment_to_navigation_home)
                   }
                   else -> {
                       // Se nessuna corrispondenza, esegue il comportamento predefinito (naviga all'indietro)
                       findNavController(R.id.nav_host_fragment_activity_main).popBackStack()
                   }
               }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}