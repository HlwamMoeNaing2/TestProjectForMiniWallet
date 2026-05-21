package com.hmn.testaicode

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hmn.testaicode.R
import com.hmn.testaicode.data.CountryModel
import com.hmn.testaicode.data.countryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class CountryViewModel @Inject constructor() : ViewModel() {
    private val _countries = MutableStateFlow(countryList)
    val countries: StateFlow<List<CountryModel>> = _countries.asStateFlow()

    private val _selectedCountry = MutableStateFlow(
        countryList.firstOrNull { it.image == R.drawable.mm } ?: countryList.first()
    )
    val selectedCountry: StateFlow<CountryModel> = _selectedCountry.asStateFlow()

    private val _showPicker = MutableStateFlow(false)
    val showPicker: StateFlow<Boolean> = _showPicker.asStateFlow()

    fun openPicker() {
        _showPicker.value = true
    }

    fun closePicker() {
        _showPicker.value = false
    }

    fun selectCountry(country: CountryModel) {
        Log.d("4LL", "selectCountry: $country ")
        _selectedCountry.value = country
        _showPicker.value = false
    }
}