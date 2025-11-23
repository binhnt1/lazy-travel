package com.lazytravel.ui.components.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazytravel.data.models.BlogPost
import com.lazytravel.ui.components.cards.BlogCard
import com.lazytravel.ui.theme.AppColors
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.data.base.BaseRepository
import kotlinx.coroutines.launch

@Composable
fun BlogSection(
    modifier: Modifier = Modifier,
    onBlogClick: (BlogPost) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val blogRepo = remember { BaseRepository<BlogPost>() }
    var blogPosts by remember { mutableStateOf<List<BlogPost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            blogRepo.getRecords<BlogPost>().fold(
                onSuccess = { fetchedBlogs ->
                    blogPosts = fetchedBlogs.take(5) // Limit to 5 for section display
                    isLoading = false
                },
                onFailure = { _ ->
                    isLoading = false
                }
            )
        }
    }

    // Section container with border and background
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = localizedString("blog_tips_tag"),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = localizedString("blog_section_title"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = localizedString("blog_section_subtitle"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.Primary)
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(blogPosts) { blog ->
                        BlogCard(
                            blog = blog,
                            onClick = { onBlogClick(blog) },
                            modifier = Modifier.width(290.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // View All button
                Button(
                    onClick = onViewAllClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = localizedString("view_all"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Primary
                    )
                }
            }
        }
    }
}