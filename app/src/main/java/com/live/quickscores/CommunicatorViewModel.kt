package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunicatorViewModel: ViewModel() {
    private val itemSelected = MutableLiveData<Response>()
    val selectedMatch: LiveData<Response> get() = itemSelected

    fun selectMatch(match:Response){
        itemSelected.value=match

    }
}