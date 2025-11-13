package com.lazytravel.presentation

import com.lazytravel.domain.model.Post
import com.lazytravel.domain.usecase.GetPostsUseCase
import com.lazytravel.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Posts ViewModel - Quản lý Social Feed
 */
class PostsViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val createPostUseCase: CreatePostUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _postsState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val postsState: StateFlow<PostsUiState> = _postsState.asStateFlow()

    private val _createPostState = MutableStateFlow<CreatePostUiState>(CreatePostUiState.Idle)
    val createPostState: StateFlow<CreatePostUiState> = _createPostState.asStateFlow()

    sealed class PostsUiState {
        object Loading : PostsUiState()
        data class Success(val posts: List<Post>) : PostsUiState()
        data class Error(val message: String) : PostsUiState()
    }

    sealed class CreatePostUiState {
        object Idle : CreatePostUiState()
        object Loading : CreatePostUiState()
        data class Success(val post: Post) : CreatePostUiState()
        data class Error(val message: String) : CreatePostUiState()
    }

    /**
     * Load danh sách posts
     */
    fun loadPosts() {
        viewModelScope.launch {
            _postsState.value = PostsUiState.Loading

            getPostsUseCase().fold(
                onSuccess = { posts ->
                    _postsState.value = PostsUiState.Success(posts)
                },
                onFailure = { error ->
                    _postsState.value = PostsUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    /**
     * Tạo post mới
     */
    fun createPost(post: Post) {
        viewModelScope.launch {
            _createPostState.value = CreatePostUiState.Loading

            createPostUseCase(post).fold(
                onSuccess = { createdPost ->
                    _createPostState.value = CreatePostUiState.Success(createdPost)
                    // Reload posts to show new post
                    loadPosts()
                },
                onFailure = { error ->
                    _createPostState.value = CreatePostUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun resetCreatePostState() {
        _createPostState.value = CreatePostUiState.Idle
    }
}
