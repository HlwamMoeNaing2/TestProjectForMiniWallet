package com.hmn.testaicode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.data.CountryModel
import com.hmn.testaicode.ui.theme.LumenCountryChipBg
import com.hmn.testaicode.ui.theme.LumenPlaceholder

@Composable
fun PhoneNumberField(
    country: CountryModel,
    onCountryClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountryPickerChip(
            country = country,
            onClick = onCountryClick
        )
        BasicTextField(
            value = value,
            onValueChange = {
                val digitsOnly = it.filter { ch -> ch.isDigit() }.take(13)
                onValueChange(digitsOnly)
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF212121)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            cursorBrush = SolidColor(Color(0xFF7E57C2)),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontSize = 17.sp,
                                color = LumenPlaceholder
                            )
                        )
                    }
                    inner()
                }
            }
        )
    }
}


@Composable
fun CountryPickerChip(
    country: CountryModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp))
            .background(LumenCountryChipBg)
            .selectable(
                selected = false,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = country.image),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = country.countryCode,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242)
        )
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF757575),
            modifier = Modifier.size(20.dp)
        )
    }
}