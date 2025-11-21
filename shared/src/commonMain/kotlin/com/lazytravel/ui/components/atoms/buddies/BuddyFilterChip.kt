package com.lazytravel.ui.components.atoms.buddies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuddyFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // TODO: Implement filter chip
    Box(modifier = Modifier.size(80.dp, 32.dp))
}