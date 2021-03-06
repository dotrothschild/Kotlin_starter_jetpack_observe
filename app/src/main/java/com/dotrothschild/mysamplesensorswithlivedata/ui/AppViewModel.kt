package com.dotrothschild.mysamplesensorswithlivedata.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import com.dotrothschild.mysamplesensorswithlivedata.model.data.OtherRepository
import com.dotrothschild.mysamplesensorswithlivedata.model.data.RankRepository

class AppViewModel
    (
    val otherRepository: OtherRepository,
    val rankRepository: RankRepository,
    applicationContext: Context
) : ViewModel() {
    lateinit var rank: Rank
}

class AppViewModelFactory(
    private val otherRepository: OtherRepository,
    private val rankRepository: RankRepository,
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(
                otherRepository,
                rankRepository,
                applicationContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}