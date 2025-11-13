package com.lazytravel.presentation

import com.lazytravel.domain.model.BlogPost
import com.lazytravel.domain.usecase.GetBlogPostsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Blog ViewModel - Quản lý blog posts
 */
class BlogViewModel(
    private val getBlogPostsUseCase: GetBlogPostsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _uiState = MutableStateFlow<BlogUiState>(BlogUiState.Loading)
    val uiState: StateFlow<BlogUiState> = _uiState.asStateFlow()

    sealed class BlogUiState {
        object Loading : BlogUiState()
        data class Success(val posts: List<BlogPost>) : BlogUiState()
        data class Error(val message: String) : BlogUiState()
    }

    /**
     * Load blog posts
     */
    fun loadBlogPosts() {
        viewModelScope.launch {
            _uiState.value = BlogUiState.Loading

            getBlogPostsUseCase().fold(
                onSuccess = { posts ->
                    _uiState.value = BlogUiState.Success(posts)
                },
                onFailure = { error ->
                    _uiState.value = BlogUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
