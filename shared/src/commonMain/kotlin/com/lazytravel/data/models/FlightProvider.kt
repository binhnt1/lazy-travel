package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class FlightProvider(
    @EncodeDefault val name: String = "",           // "Vietnam Airlines", "VietJet Air"
    @EncodeDefault val code: String = "",           // "VN", "VJ", "QH"
    @EncodeDefault val slug: String = "",           // "vietnam-airlines", "vietjet-air"
    @EncodeDefault val logo: String = "",           // Logo URL
    @EncodeDefault val description: String = "",

    // Relation
    @EncodeDefault val countryId: String = "",      // → Country relation

    // Type: FULL_SERVICE, LOW_COST, CHARTER
    @EncodeDefault val type: String = "",

    // Rating & Reviews
    @EncodeDefault val rating: Double = 0.0,
    @EncodeDefault val reviewCount: Int = 0,

    // Company info
    @EncodeDefault val fleetSize: Int = 0,          // Number of aircraft
    @EncodeDefault val foundedYear: Int = 0,        // 1993

    // Contact
    @EncodeDefault val website: String = "",
    @EncodeDefault val hotline: String = "",
    @EncodeDefault val email: String = "",

    // Status
    @EncodeDefault val isVerified: Boolean = false,
    @EncodeDefault val isActive: Boolean = true,
) : BaseModel() {

    // Expanded relation
    @kotlinx.serialization.Transient
    var expandedCountry: Country? = null

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as FlightProvider)
    }

    fun populateExpandedData() {
        val expandData = expand ?: return

        expandData["countryId"]?.let { countryJson ->
            try {
                expandedCountry = json.decodeFromJsonElement(Country.serializer(), countryJson)
            } catch (_: Exception) {}
        }
    }

    override fun getSchema() = baseCollection(collectionName()) {
        text("name") { required = true; max = 200 }
        text("code") { required = true; max = 10 }
        text("slug") { required = true; max = 200 }
        text("logo") { required = false; max = 500 }
        text("description") { required = false; max = 2000 }

        relation("countryId") {
            required = true
            collectionId = Country().collectionName()
            cascadeDelete = false
            maxSelect = 1
        }

        text("type") { required = false; max = 50 }
        number("rating") { required = false; min = 0.0; max = 5.0 }
        number("reviewCount") { required = false; min = 0.0; onlyInt = true }
        number("fleetSize") { required = false; min = 0.0; onlyInt = true }
        number("foundedYear") { required = false; min = 1900.0; max = 2100.0; onlyInt = true }

        text("website") { required = false; max = 500 }
        text("hotline") { required = false; max = 50 }
        text("email") { required = false; max = 200 }

        bool("isVerified") { required = false }
        bool("isActive") { required = false }
    }

    override suspend fun getSeedData(): List<FlightProvider> {
        // Get countries first
        val countryRepo = BaseRepository<Country>()
        val countries = countryRepo.getRecords<Country>().getOrNull() ?: emptyList()
        val countryMap = countries.associateBy { it.name }

        return listOf(
            FlightProvider(
                name = "Vietnam Airlines",
                code = "VN",
                slug = "vietnam-airlines",
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Vietnam_Airlines_logo.svg/200px-Vietnam_Airlines_logo.svg.png",
                description = "Hãng hàng không quốc gia Việt Nam, thành viên SkyTeam",
                countryId = countryMap["Vietnam"]?.id ?: "",
                type = "FULL_SERVICE",
                rating = 4.5,
                reviewCount = 12450,
                fleetSize = 100,
                foundedYear = 1993,
                website = "https://www.vietnamairlines.com",
                hotline = "1900 1100",
                email = "contact@vietnamairlines.com",
                isVerified = true,
                isActive = true
            ),
            FlightProvider(
                name = "VietJet Air",
                code = "VJ",
                slug = "vietjet-air",
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/VietJet_Air_logo.svg/200px-VietJet_Air_logo.svg.png",
                description = "Hãng hàng không giá rẻ hàng đầu Việt Nam",
                countryId = countryMap["Vietnam"]?.id ?: "",
                type = "LOW_COST",
                rating = 4.2,
                reviewCount = 18920,
                fleetSize = 80,
                foundedYear = 2011,
                website = "https://www.vietjetair.com",
                hotline = "1900 1886",
                email = "hotro@vietjetair.com",
                isVerified = true,
                isActive = true
            ),
            FlightProvider(
                name = "Bamboo Airways",
                code = "QH",
                slug = "bamboo-airways",
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Bamboo_Airways_logo.svg/200px-Bamboo_Airways_logo.svg.png",
                description = "Hãng hàng không 5 sao đầu tiên của Việt Nam",
                countryId = countryMap["Vietnam"]?.id ?: "",
                type = "FULL_SERVICE",
                rating = 4.6,
                reviewCount = 8340,
                fleetSize = 30,
                foundedYear = 2017,
                website = "https://www.bambooairways.com",
                hotline = "1900 1166",
                email = "contact@bambooairways.com",
                isVerified = true,
                isActive = true
            ),
            FlightProvider(
                name = "Singapore Airlines",
                code = "SQ",
                slug = "singapore-airlines",
                logo = "https://upload.wikimedia.org/wikipedia/en/thumb/6/6b/Singapore_Airlines_Logo_2.svg/200px-Singapore_Airlines_Logo_2.svg.png",
                description = "Hãng hàng không 5 sao của Singapore",
                countryId = countryMap["Singapore"]?.id ?: "",
                type = "FULL_SERVICE",
                rating = 4.8,
                reviewCount = 45600,
                fleetSize = 200,
                foundedYear = 1972,
                website = "https://www.singaporeair.com",
                hotline = "+65 6223 8888",
                email = "singaporeair@singaporeair.com.sg",
                isVerified = true,
                isActive = true
            ),
            FlightProvider(
                name = "Thai Airways",
                code = "TG",
                slug = "thai-airways",
                logo = "https://upload.wikimedia.org/wikipedia/en/thumb/9/94/Thai_Airways_Logo.svg/200px-Thai_Airways_Logo.svg.png",
                description = "Hãng hàng không quốc gia Thái Lan",
                countryId = countryMap["Thailand"]?.id ?: "",
                type = "FULL_SERVICE",
                rating = 4.3,
                reviewCount = 23100,
                fleetSize = 80,
                foundedYear = 1960,
                website = "https://www.thaiairways.com",
                hotline = "+66 2 356 1111",
                email = "contact@thaiairways.com",
                isVerified = true,
                isActive = true
            ),
            FlightProvider(
                name = "AirAsia",
                code = "AK",
                slug = "airasia",
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/AirAsia_New_Logo.svg/200px-AirAsia_New_Logo.svg.png",
                description = "Hãng hàng không giá rẻ lớn nhất châu Á",
                countryId = countryMap["Malaysia"]?.id ?: "",
                type = "LOW_COST",
                rating = 4.0,
                reviewCount = 34200,
                fleetSize = 250,
                foundedYear = 1993,
                website = "https://www.airasia.com",
                hotline = "+60 3 8775 4000",
                email = "support@airasia.com",
                isVerified = true,
                isActive = true
            )
        )
    }

    companion object {
        // Type constants
        const val TYPE_FULL_SERVICE = "FULL_SERVICE"
        const val TYPE_LOW_COST = "LOW_COST"
        const val TYPE_CHARTER = "CHARTER"
    }
}
