package com.hmn.testaicode.ui.screens.personalInfo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.screens.personalInfo.components.LabeledField
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun PersonalInfoSubmitScreen(
    modifier: Modifier = Modifier,
    onSubmit: (
        firstName: String,
        lastName: String,
        email: String,
        address: String,
        city: String,
        zip: String,
    ) -> Unit = { _, _, _, _, _, _ -> },
) {
    val scheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars)
            .imePadding()
           .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding( vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(scheme.primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = scheme.primary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Complete Your Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = scheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fill in your personal details",
                    fontSize = 14.sp,
                    color = scheme.onSurface.copy(alpha = 0.62f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                LabeledField(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it },
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledField(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it },
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledField(
                    label = "Address",
                    value = address,
                    onValueChange = { address = it },
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        LabeledField(
                            label = "City",
                            value = city,
                            onValueChange = { city = it },
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        LabeledField(
                            label = "ZIP Code",
                            value = zip,
                            onValueChange = { zip = it },
                            keyboardType = KeyboardType.Number
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))


                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            GradientContinueButton(
                text = "Submit",
                enabled = true,
                onClick = {
                    Toast.makeText(context, "Continue", Toast.LENGTH_SHORT).show()
                    onSubmit(firstName, lastName, email, address, city, zip)
                }
            )


        }

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PersonalInfoSubmitScreenPreview() {
    TestAICodeTheme {
        PersonalInfoSubmitScreen()
    }
}