package com.hmn.testaicode.ui.screens.personalInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.SimpleResponseHandler
import com.hmn.testaicode.data.remote.model.CreateUserModelRequest
import com.hmn.testaicode.domain.CreateUserAndWalletUc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val createUserAndWalletUc: CreateUserAndWalletUc
): ViewModel(){
    private val _createUserState = MutableStateFlow<CreateUserState>(CreateUserState.Loading)

    val createUserState: StateFlow<CreateUserState> =  _createUserState.asStateFlow()



    fun createUserAndWallet(request:CreateUserModelRequest){
        viewModelScope.launch {
          when(val response =   createUserAndWalletUc.createUser(request)){
                is SimpleResponseHandler.Error ->{
                    _createUserState.value = CreateUserState.Error(response.exception.localizedMessage ?: "")
                }
                is SimpleResponseHandler.Success -> {
                    _createUserState.value = CreateUserState.Success
                }
            }
        }
    }

}

sealed interface CreateUserState{
    data object Loading: CreateUserState
    data object Success:CreateUserState

    data class Error (val message: String):CreateUserState

}