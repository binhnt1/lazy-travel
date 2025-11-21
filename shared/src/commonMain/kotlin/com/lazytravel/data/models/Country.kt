package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val name: String = "",
    val code: String = "",
    val emoji: String = "",
    val continent: String = "",
    val currency: String = "",
    val language: String = "",
    val timezone: String = "",
    val phoneCode: String = "",
    val population: Long = 0,
    val area: Double = 0.0,
    val capital: String = "",
    val region: String = ""
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as Country)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 100 }
        text("code") { required = true; max = 3 }
        text("emoji") { required = false; max = 10 }
        text("continent") { required = false; max = 50 }
        text("currency") { required = false; max = 10 }
        text("language") { required = false; max = 100 }
        text("timezone") { required = false; max = 50 }
        text("phoneCode") { required = false; max = 10 }
        number("population") { required = false; onlyInt = true }
        number("area") { required = false }
        text("capital") { required = false; max = 100 }
        text("region") { required = false; max = 100 }
    }

    override suspend fun getSeedData(): List<Country> = listOf(
        Country(
            name = "Vietnam",
            code = "VN",
            emoji = "🇻🇳",
            continent = "Asia",
            currency = "VND",
            language = "Vietnamese",
            timezone = "UTC+7",
            phoneCode = "+84",
            population = 97000000,
            area = 331212.0,
            capital = "Hanoi",
            region = "Southeast Asia"
        ),
        Country(
            name = "Thailand",
            code = "TH",
            emoji = "🇹🇭",
            continent = "Asia",
            currency = "THB",
            language = "Thai",
            timezone = "UTC+7",
            phoneCode = "+66",
            population = 70000000,
            area = 513120.0,
            capital = "Bangkok",
            region = "Southeast Asia"
        ),
        Country(
            name = "Japan",
            code = "JP",
            emoji = "🇯🇵",
            continent = "Asia",
            currency = "JPY",
            language = "Japanese",
            timezone = "UTC+9",
            phoneCode = "+81",
            population = 125000000,
            area = 377975.0,
            capital = "Tokyo",
            region = "East Asia"
        ),
        Country(
            name = "South Korea",
            code = "KR",
            emoji = "🇰🇷",
            continent = "Asia",
            currency = "KRW",
            language = "Korean",
            timezone = "UTC+9",
            phoneCode = "+82",
            population = 52000000,
            area = 100210.0,
            capital = "Seoul",
            region = "East Asia"
        ),
        Country(
            name = "Singapore",
            code = "SG",
            emoji = "🇸🇬",
            continent = "Asia",
            currency = "SGD",
            language = "English, Malay, Mandarin, Tamil",
            timezone = "UTC+8",
            phoneCode = "+65",
            population = 6000000,
            area = 728.0,
            capital = "Singapore",
            region = "Southeast Asia"
        ),
        Country(
            name = "Malaysia",
            code = "MY",
            emoji = "🇲🇾",
            continent = "Asia",
            currency = "MYR",
            language = "Malay",
            timezone = "UTC+8",
            phoneCode = "+60",
            population = 33000000,
            area = 330803.0,
            capital = "Kuala Lumpur",
            region = "Southeast Asia"
        ),
        Country(
            name = "Indonesia",
            code = "ID",
            emoji = "🇮🇩",
            continent = "Asia",
            currency = "IDR",
            language = "Indonesian",
            timezone = "UTC+7 to UTC+9",
            phoneCode = "+62",
            population = 273000000,
            area = 1904569.0,
            capital = "Jakarta",
            region = "Southeast Asia"
        ),
        Country(
            name = "Philippines",
            code = "PH",
            emoji = "🇵🇭",
            continent = "Asia",
            currency = "PHP",
            language = "Filipino, English",
            timezone = "UTC+8",
            phoneCode = "+63",
            population = 110000000,
            area = 300000.0,
            capital = "Manila",
            region = "Southeast Asia"
        ),
        Country(
            name = "Cambodia",
            code = "KH",
            emoji = "🇰🇭",
            continent = "Asia",
            currency = "KHR",
            language = "Khmer",
            timezone = "UTC+7",
            phoneCode = "+855",
            population = 17000000,
            area = 181035.0,
            capital = "Phnom Penh",
            region = "Southeast Asia"
        ),
        Country(
            name = "Laos",
            code = "LA",
            emoji = "🇱🇦",
            continent = "Asia",
            currency = "LAK",
            language = "Lao",
            timezone = "UTC+7",
            phoneCode = "+856",
            population = 7000000,
            area = 236800.0,
            capital = "Vientiane",
            region = "Southeast Asia"
        ),
        Country(
            name = "United States",
            code = "US",
            emoji = "🇺🇸",
            continent = "North America",
            currency = "USD",
            language = "English",
            timezone = "UTC-5 to UTC-10",
            phoneCode = "+1",
            population = 331000000,
            area = 9833520.0,
            capital = "Washington D.C.",
            region = "North America"
        ),
        Country(
            name = "United Kingdom",
            code = "GB",
            emoji = "🇬🇧",
            continent = "Europe",
            currency = "GBP",
            language = "English",
            timezone = "UTC+0",
            phoneCode = "+44",
            population = 67000000,
            area = 242495.0,
            capital = "London",
            region = "Western Europe"
        ),
        Country(
            name = "France",
            code = "FR",
            emoji = "🇫🇷",
            continent = "Europe",
            currency = "EUR",
            language = "French",
            timezone = "UTC+1",
            phoneCode = "+33",
            population = 67000000,
            area = 551695.0,
            capital = "Paris",
            region = "Western Europe"
        ),
        Country(
            name = "Germany",
            code = "DE",
            emoji = "🇩🇪",
            continent = "Europe",
            currency = "EUR",
            language = "German",
            timezone = "UTC+1",
            phoneCode = "+49",
            population = 83000000,
            area = 357022.0,
            capital = "Berlin",
            region = "Western Europe"
        ),
        Country(
            name = "Australia",
            code = "AU",
            emoji = "🇦🇺",
            continent = "Oceania",
            currency = "AUD",
            language = "English",
            timezone = "UTC+8 to UTC+10",
            phoneCode = "+61",
            population = 26000000,
            area = 7692024.0,
            capital = "Canberra",
            region = "Oceania"
        )
    )
}

