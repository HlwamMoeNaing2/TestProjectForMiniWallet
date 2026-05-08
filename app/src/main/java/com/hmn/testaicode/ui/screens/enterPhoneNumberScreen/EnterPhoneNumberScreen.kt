package com.hmn.testaicode.ui.screens.enterPhoneNumberScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.data.countryList
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.PhoneNumberField
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.TermsFooter
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.WalletBrandIcon
import com.hmn.testaicode.ui.components.dialog.CountryPickerDialog
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.extension.isValidPhone

 val ScreenGradientTop = Color(0xFFF3F0FF)
 val ScreenGradientBottom = Color(0xFFFFFBFE)
 val SubtitleColor = Color(0xFF757575)
 val LabelColor = Color(0xFF9E9E9E)

@Composable
fun EnterPhoneNumberScreen(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit = {},
    /*countryViewModel: CountryViewModel = viewModel(),*/
) {

    /*
    val countries by countryViewModel.countries.collectAsState()
    val selectedCountry by countryViewModel.selectedCountry.collectAsState()
    val showCountryPicker by countryViewModel.showPicker.collectAsState()
     */


    var phoneNumber by remember { mutableStateOf("") }

    var showCountryPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ScreenGradientTop, ScreenGradientBottom)
                )
            )
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(36.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp)
                ) {
                    WalletBrandIcon()
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Welcome to Lumen",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Enter your phone number to securely sign in or create your wallet.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = SubtitleColor,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = "PHONE NUMBER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp,
                        color = LabelColor
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PhoneNumberField(
                        country = countryList.first(),
                        onCountryClick = { showCountryPicker = true /*countryViewModel.openPicker()*/ },
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = "555 000 1234"
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    GradientContinueButton(
                        enabled = phoneNumber.isValidPhone(),
                        onClick = {
                            Toast.makeText(context, "Continue", Toast.LENGTH_SHORT).show()
                            onContinue()
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TermsFooter()
                }
            }
        }

        if (showCountryPicker) {
            CountryPickerDialog(
                countries = countryList,
                selected = countryList.first(),
                onDismiss = {
                /*countryViewModel.closePicker() */
                    showCountryPicker = false
                },
                onSelect = {
                    showCountryPicker = false
                /*countryViewModel.selectCountry(it)*/
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EnterPhoneNumberScreenPreview() {
    TestAICodeTheme {
        EnterPhoneNumberScreen()
    }
}
