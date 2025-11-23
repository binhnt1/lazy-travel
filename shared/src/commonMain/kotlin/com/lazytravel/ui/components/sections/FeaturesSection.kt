package com.lazytravel.ui.components.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Feature
import com.lazytravel.ui.components.cards.FeatureCard
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun FeaturesSection(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val repository = remember { BaseRepository<Feature>() }
    var isLoading by remember { mutableStateOf(true) }
    var features by remember { mutableStateOf<List<Feature>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            repository.getRecords<Feature>().fold(
                onSuccess = { fetchedFeatures ->
                    features = fetchedFeatures
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 20.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35),
                text = "✨ ${localizedString("features_tag")}",
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )

            Text(
                text = localizedString("features_title"),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Text(
                text = localizedString("features_subtitle"),
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 21.sp
            )
        }

        if (isLoading) {
            Text(
                text = "Loading features...",
                color = Color.Gray
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                features.forEach { feature ->
                    FeatureCard(
                        icon = feature.icon,
                        title = feature.title,
                        description = feature.description
                    )
                }
            }
        }
    }
}