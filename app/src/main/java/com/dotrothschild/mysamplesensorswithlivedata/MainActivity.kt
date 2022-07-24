package com.dotrothschild.mysamplesensorswithlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dotrothschild.mysamplesensorswithlivedata.databinding.ActivityMainBinding
import com.dotrothschild.mysamplesensorswithlivedata.model.data.RankRepository
import com.dotrothschild.mysamplesensorswithlivedata.ui.AppViewModel
import com.dotrothschild.mysamplesensorswithlivedata.ui.AppViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var appViewModel: AppViewModel
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    lateinit var bottomMenuFragment: GetBottomMenuFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(
                RankRepository,
                applicationContext
            )
        )[AppViewModel::class.java]

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomMenuFragment = GetBottomMenuFragment.Home
        setupNavController()

    }

    private fun setupNavController() {
        // https://stackoverflow.com/questions/58703451/fragmentcontainerview-as-navhostfragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        // end bug fix work around

        NavigationUI.setupWithNavController(
            binding.navView,
            navController
        )
    }
}