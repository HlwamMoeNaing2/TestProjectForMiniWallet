package com.hmn.testaicode.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.AuthSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthSessionViewModel @Inject constructor(
    private val authSessionRepository: AuthSessionRepository,
) : ViewModel() {

    fun markLoggedIn() {
        viewModelScope.launch {
            authSessionRepository.setLoggedIn(true)
        }
    }

    fun markLoggedOut() {
        viewModelScope.launch {
            authSessionRepository.setLoggedIn(false)
        }
    }


    suspend fun doBackgroundWork(){
        withContext(Dispatchers.IO){
            //do work
        }
    }
}

