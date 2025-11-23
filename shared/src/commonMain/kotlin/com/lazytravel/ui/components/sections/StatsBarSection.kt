package com.lazytravel.ui.components.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.molecules.StatItem
import com.lazytravel.data.models.Stat
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.base.BaseRepository
import kotlinx.coroutines.launch

@Composable
fun StatsBarSection(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val repository = remember { BaseRepository<Stat>() }
    var isLoading by remember { mutableStateOf(true) }
    var stats by remember { mutableStateOf<List<Stat>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            repository.getRecords<Stat>().fold(
                onSuccess = { fetchedStats ->
                    stats = fetchedStats.sortedBy { it.order }
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (isLoading) {
            Text(
                text = localizedString("loading"),
                color = Color.Gray
            )
        } else {
            stats.take(4).forEach { stat ->
                StatItem(
                    number = stat.number,
                    label = stat.label
                )
            }
        }
    }
}
