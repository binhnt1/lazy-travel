package com.lazytravel.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerDialog(
    images: List<String>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { images.size }
    )

    // Store zoom state for each page
    val pageZoomStates = remember { mutableStateMapOf<Int, Float>() }

    // Calculate if swipe should be enabled based on current page zoom
    val canSwipe by remember {
        derivedStateOf {
            val currentZoom = pageZoomStates[pagerState.currentPage] ?: 1f
            currentZoom <= 1.01f // Small threshold for floating point precision
        }
    }

    val currentPageZoom = pageZoomStates[pagerState.currentPage] ?: 1f
    val scope = rememberCoroutineScope()

    // Debug log
    LaunchedEffect(canSwipe, pagerState.currentPage, currentPageZoom) {
        println("🎯 Current page: ${pagerState.currentPage}, zoom: $currentPageZoom, canSwipe: $canSwipe")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Image Pager - Enable swipe only when not zoomed
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = canSwipe
            ) { page ->
                var scale by remember { mutableStateOf(1f) }
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }

                // Update zoom state for this page
                LaunchedEffect(scale) {
                    pageZoomStates[page] = scale
                    println("📸 Page $page zoom updated: $scale, canSwipe: ${scale <= 1.01f}")
                }

                // Reset zoom when page changes
                LaunchedEffect(pagerState.currentPage) {
                    if (page != pagerState.currentPage) {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                        pageZoomStates[page] = 1f
                        println("🔄 Reset zoom for page $page")
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = images[page],
                        contentDescription = "Image ${page + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(page) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    // Apply zoom
                                    scale = (scale * zoom).coerceIn(1f, 5f)

                                    // Only allow pan when zoomed in
                                    if (scale > 1f) {
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    } else {
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            }
                            .pointerInput(page) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        if (scale > 1f) {
                                            // Reset zoom
                                            scale = 1f
                                            offsetX = 0f
                                            offsetY = 0f
                                        } else {
                                            // Zoom to 2x
                                            scale = 2f
                                        }
                                    }
                                )
                            }
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY
                            )
                    )
                }
            }

            // Top Bar with Close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            // Bottom indicator showing current image number
            if (images.size > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${images.size}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Zoom indicator for current page
            if (currentPageZoom > 1f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${(currentPageZoom * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Navigation buttons (show only when multiple images)
            if (images.size > 1) {
                // Previous button
                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Next button
                if (pagerState.currentPage < images.size - 1) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

