package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LeagueIdSharedViewModel:ViewModel() {
    private val _selectedLeagueId = MutableLiveData<String>()
    val selectedLeagueId:LiveData<String> =_selectedLeagueId


    fun setSelectedLeagueId(id: String) {
        _selectedLeagueId.value = id
    }
}