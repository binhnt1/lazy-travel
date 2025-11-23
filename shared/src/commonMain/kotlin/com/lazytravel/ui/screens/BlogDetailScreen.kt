package com.lazytravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lazytravel.data.models.BlogPost
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.ui.utils.HtmlContent
import com.lazytravel.ui.utils.parseHexColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(
    blog: BlogPost,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bài viết",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = AppColors.TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.Background)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                if (blog.thumbnail.isNotEmpty()) {
                    AsyncImage(
                        model = blog.thumbnail,
                        contentDescription = blog.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(parseHexColor(blog.categoryColor) ?: AppColors.Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = blog.thumbnailEmoji,
                            fontSize = 80.sp
                        )
                    }
                }

                // Dark overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                // Category badge overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.95f)
                ) {
                    Text(
                        text = blog.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = blog.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )

                // Author info card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F5F5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Author avatar
                        if (blog.authorAvatar.isNotEmpty()) {
                            AsyncImage(
                                model = blog.authorAvatar,
                                contentDescription = blog.authorName,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        parseHexColor(blog.categoryColor) ?: AppColors.Primary
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = blog.authorName.take(2).uppercase(),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontSize = 14.sp
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = blog.authorName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.TextPrimary
                            )
                            Text(
                                text = blog.publishedAt,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666)
                            )
                        }

                        // Read time badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AppColors.Primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${blog.readTime} phút",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE3F2FD),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBDEFB))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "👁️", fontSize = 18.sp)
                            Text(
                                text = "${blog.views} lượt xem",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF1976D2),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFCE4EC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF8BBD0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "❤️", fontSize = 18.sp)
                            Text(
                                text = "${blog.likes} lượt thích",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFC2185B),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Divider
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )

                // HTML Content
                HtmlContent(
                    html = blog.content,
                    modifier = Modifier.fillMaxWidth()
                )

                // Spacer for bottom padding
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
