package com.dotrothschild.mysamplesensorswithlivedata.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dotrothschild.mysamplesensorswithlivedata.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
   private val appViewModel by lazy {
       ViewModelProvider(this)[MainViewModel::class.java]
   }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val textMessage:  TextView = binding.message
        appViewModel.azimuthLiveData.observe(viewLifecycleOwner) {
            textMessage.text = it.toString()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}