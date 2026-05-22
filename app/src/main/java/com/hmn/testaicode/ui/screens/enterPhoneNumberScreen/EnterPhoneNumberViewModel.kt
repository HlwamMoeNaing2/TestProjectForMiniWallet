package com.hmn.testaicode.ui.screens.enterPhoneNumberScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.SimpleResponseHandler
import com.hmn.testaicode.data.WalletUserRepo
import com.hmn.testaicode.data.remote.model.User
import com.hmn.testaicode.data.remote.model.UserDetail
import com.hmn.testaicode.ui.screens.home_screen.UserDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EnterPhoneNumberViewModel @Inject constructor(
    private val walletUserRepo: WalletUserRepo
): ViewModel(){

    private val _userDetailState = MutableStateFlow<UserDetailState>(UserDetailState.Loading)

    val userDetailState: StateFlow<UserDetailState> =  _userDetailState.asStateFlow()

    fun getUserDetail(phone:String){
        viewModelScope.launch {
            val data = walletUserRepo.getUserDetail(phone)
            when(data){
                is SimpleResponseHandler.Error -> {
                    Log.d("#MMLog", "ViewModel: Error ")
                    val error = data.exception.localizedMessage
                    _userDetailState.value = UserDetailState.Error( error ?: "Something went wrong")

                }
                is SimpleResponseHandler.Success ->{
                    Log.d("#MMLog", "Success: Error ")
                    val userDetail = data.data
                    _userDetailState.value = UserDetailState.Success(userDetail)

                }
            }
        }
    }
}

sealed interface UserDetailState{
    data object Loading: UserDetailState
    data class Success(val user: UserDetail):UserDetailState

    data class Error (val message: String):UserDetailState

}


