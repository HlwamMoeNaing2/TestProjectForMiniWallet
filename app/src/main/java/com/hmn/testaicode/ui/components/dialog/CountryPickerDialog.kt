package com.hmn.testaicode.ui.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmn.testaicode.data.CountryModel
import com.hmn.testaicode.data.countryList
import com.hmn.testaicode.ui.theme.TestAICodeTheme

@Composable
fun CountryPickerDialog(
    countries: List<CountryModel>,
    selected: CountryModel,
    onDismiss: () -> Unit,
    onSelect: (CountryModel) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select country", fontWeight = FontWeight.SemiBold) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(countries) { country ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = country == selected,
                                role = Role.RadioButton,
                                onClick = { onSelect(country) }
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = country.image),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).padding(end = 12.dp)

                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = country.countryCode,
                            fontWeight = if (country == selected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                    HorizontalDivider(color = Color(0xFFEAEAEA))
                }
            }
        },
        confirmButton = {
           // Button(onClick = onDismiss) { Text("Close") }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EnterPhoneNumberScreenPreview() {
    TestAICodeTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CountryPickerDialog(countryList, countryList.first(), onDismiss = {}, onSelect = {})
        }
    }
}
