package com.hmn.testaicode.data

import com.hmn.testaicode.R

data class CountryModel(
    val image: Int,
    val countryCode: String
)


val countryList = listOf(

    CountryModel(

        image = R.drawable.mm,

        countryCode = "+95"

    ),

    CountryModel(

        image = R.drawable.us,

        countryCode = "+1"

    ),

    CountryModel(

        image = R.drawable.thai,

        countryCode = "+66"

    )

)