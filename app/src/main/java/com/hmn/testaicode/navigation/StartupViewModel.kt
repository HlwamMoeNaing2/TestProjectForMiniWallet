package com.hmn.testaicode.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.AuthSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface StartupUiState {
    data object Loading : StartupUiState
    data class Ready(val isLoggedIn: Boolean) : StartupUiState
}

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val authSessionRepository: AuthSessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StartupUiState>(StartupUiState.Loading)
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(350)
            val isLoggedIn = authSessionRepository.isLoggedIn.first()
            _uiState.value = StartupUiState.Ready(isLoggedIn)
        }
    }
}
