package com.hmn.testaicode.data

data class CountryModel(
    val image: String,
    val countryCode: String
)


val countryList = listOf<CountryModel>(
    CountryModel(
        "mm Image Path",
        "+95"
    ),

    CountryModel(
        "us Image Path",
        "+1"
    ),


    CountryModel(
        "thai Image Path",
        "+66"
    )

)