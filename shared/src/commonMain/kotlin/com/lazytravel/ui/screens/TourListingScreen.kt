package com.lazytravel.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazytravel.data.models.InsurancePackage
import com.lazytravel.data.models.Tour
import com.lazytravel.ui.components.sections.providers.InsuranceProviderSection
import com.lazytravel.ui.components.sections.providers.VisaProviderSection
import com.lazytravel.ui.components.sections.tours.ServicesGridSection
import com.lazytravel.ui.components.sections.tours.TourFeaturedSection
import com.lazytravel.ui.components.sections.tours.TourFilterSection
import com.lazytravel.ui.components.sections.tours.TourHeaderSection
import com.lazytravel.ui.components.sections.tours.TourLuxurySection
import com.lazytravel.ui.components.sections.tours.TourNormalSection

/**
 * Tour Listing Screen
 *
 * Main screen for browsing tours with sections:
 * - Header with search
 * - Services grid (8 services)
 * - Filter controls
 * - Featured tours (yellow background)
 * - Insurance provider section
 * - Luxury tours (navy background)
 * - Visa provider section
 * - Normal tours
 *
 * Architecture follows BuddyScreen pattern with sections/cards separation
 */
@Composable
fun TourListingScreen(
    onTourClick: (Tour) -> Unit = {},
    onServiceClick: (String) -> Unit = {},
    onInsurancePackageClick: (InsurancePackage) -> Unit = {},
    onVisaCountryClick: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Header Section - Search + Icons
        item {
            TourHeaderSection(
                onSearchChanged = { query ->
                    // TODO: Implement search functionality
                },
                onNotificationClick = onNotificationClick,
                onMessageClick = onMessageClick,
                onFilterClick = {
                    // TODO: Scroll to filter section or show filter dialog
                }
            )
        }

        // Services Grid Section - 8 services
        item {
            ServicesGridSection(
                onServiceClick = onServiceClick
            )
        }

        // Filter Section
        item {
            TourFilterSection(
                onFilterChanged = { filters ->
                    // TODO: Apply filters to tour listings
                }
            )
        }

        // Featured Tours Section - Yellow background
        item {
            TourFeaturedSection(
                onTourClick = onTourClick,
                onViewAllClick = {
                    // TODO: Navigate to all featured tours
                }
            )
        }

        // Insurance Provider Section
        item {
            InsuranceProviderSection(
                onPackageClick = onInsurancePackageClick,
                onBuyClick = {
                    // TODO: Navigate to insurance purchase flow
                }
            )
        }

        // Luxury Tours Section - Navy background
        item {
            TourLuxurySection(
                onTourClick = onTourClick,
                onViewAllClick = {
                    // TODO: Navigate to all luxury tours
                }
            )
        }

        // Visa Provider Section
        item {
            VisaProviderSection(
                onCountryClick = onVisaCountryClick,
                onApplyClick = {
                    // TODO: Navigate to visa application flow
                }
            )
        }

        // Normal Tours Section
        item {
            TourNormalSection(
                onTourClick = onTourClick,
                onViewAllClick = {
                    // TODO: Navigate to all tours
                }
            )
        }
    }
}
