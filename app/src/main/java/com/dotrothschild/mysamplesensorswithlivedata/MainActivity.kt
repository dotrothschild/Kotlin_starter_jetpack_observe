package com.dotrothschild.mysamplesensorswithlivedata

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dotrothschild.mysamplesensorswithlivedata.databinding.ActivityMainBinding
import com.dotrothschild.mysamplesensorswithlivedata.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}