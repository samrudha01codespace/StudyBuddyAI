package com.example.studybuddyai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studybuddyai.ui.dashboard.DashboardFragment
import com.example.studybuddyai.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.nav_view)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container , HomeFragment())
            .commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            when(menuItem.itemId){

                R.id.navigation_home -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container , HomeFragment())
                        .commit()

                }
                R.id.navigation_dashboard -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container , DashboardFragment())
                        .commit()

                }

            }

            return@setOnNavigationItemSelectedListener true

        }

    }
}