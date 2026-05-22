package com.hmn.testaicode.ui.screens.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.SimpleResponseHandler
import com.hmn.testaicode.data.UserRepository
import com.hmn.testaicode.data.remote.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _userStat = MutableStateFlow<UserDataState>(UserDataState.Loading)

    val userStat: StateFlow<UserDataState> =  _userStat.asStateFlow()


    init {
        getUseForUI()
    }
    fun getUseForUI() {
        viewModelScope.launch {
          when(val data =   repo.getUsers()){
                is SimpleResponseHandler.Error -> {
                    val exception = data.exception
                    _userStat.value = UserDataState.Error("Fucking wrong ${exception.localizedMessage}")
                }
                is SimpleResponseHandler.Success -> {
                    val data = data.data
                    _userStat.value = UserDataState.Success(data.first())
                }
            }
        }
    }

}


sealed interface UserDataState{
    data object Loading: UserDataState
    data class Success(val user: User):UserDataState

    data class Error (val message: String):UserDataState

}


