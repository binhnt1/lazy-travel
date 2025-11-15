package com.lazytravel.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
 * Shows 4 features in 2x2 grid layout
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
                    println("✅ Loaded ${features.size} features from PocketBase")
                },
                onFailure = { error ->
                    println("❌ Failed to load features: ${error.message}")
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.Background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Title
        Text(
            text = localizedString("features_title"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        // Features Grid (2x2)
        if (isLoading) {
            Text("Loading features...", color = Color.Gray)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(features) { feature ->
                    // Use translation keys from DB
                    FeatureCard(
                        icon = feature.icon,
                        title = localizedString(feature.title),  // "feature_voting" → translated
                        description = localizedString(feature.description)  // "feature_voting_desc" → translated
                    )
                }
            }
        }
    }
}
