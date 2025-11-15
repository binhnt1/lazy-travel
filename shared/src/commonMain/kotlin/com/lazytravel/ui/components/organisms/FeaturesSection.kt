package com.lazytravel.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.models.Feature
import com.lazytravel.data.repositories.FeaturesRepository
import com.lazytravel.ui.components.molecules.FeatureCard
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

/**
 * Features Section - Organism
 *
 * Displays app features from PocketBase "features" collection
 * Horizontal scroll layout (like HTML version)
 * Features contain translation keys, translations loaded from LocalizationManager
 */
@Composable
fun FeaturesSection(
    modifier: Modifier = Modifier
) {
    val repository = remember { FeaturesRepository() }
    var features by remember { mutableStateOf<List<Feature>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()

    // Fetch features from PocketBase on first composition
    LaunchedEffect(Unit) {
        scope.launch {
            repository.getFeatures().fold(
                onSuccess = { fetchedFeatures ->
                    features = fetchedFeatures
                    isLoading = false
                },
                onFailure = { error ->
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section Tag
            Text(
                text = "✨ ${localizedString("features_tag")}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35),
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF3E0),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )

            // Section Title
            Text(
                text = localizedString("features_title"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            // Section Subtitle
            Text(
                text = localizedString("features_subtitle"),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Features Horizontal Scroll
        if (isLoading) {
            Text(
                text = "Loading features...",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                features.forEach { feature ->
                    // Use translation keys from DB
                    FeatureCard(
                        icon = feature.icon,
                        title = feature.title,  // "feature_voting" → translated
                        description = feature.description  // "feature_voting_desc" → translated
                    )
                }
            }
        }
    }
}