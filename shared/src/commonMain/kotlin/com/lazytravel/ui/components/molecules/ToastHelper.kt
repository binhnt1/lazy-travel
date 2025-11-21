package com.lazytravel.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Toast Helper - Utility for showing toast messages
 *
 * Types:
 * - Error (red)
 * - Success (green)
 * - Info (blue)
 * - Warning (orange)
 */

enum class ToastType {
    ERROR,
    SUCCESS,
    INFO,
    WARNING
}

object ToastHelper {
    /**
     * Show a toast message with the specified type
     */
    suspend fun show(
        snackbarHostState: SnackbarHostState,
        message: String,
        type: ToastType = ToastType.INFO,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        val prefix = when (type) {
            ToastType.ERROR -> "❌ "
            ToastType.SUCCESS -> "✅ "
            ToastType.INFO -> "ℹ️ "
            ToastType.WARNING -> "⚠️ "
        }
        snackbarHostState.showSnackbar(
            message = "$prefix$message",
            duration = duration
        )
    }

    /**
     * Show error toast
     */
    suspend fun showError(
        snackbarHostState: SnackbarHostState,
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(snackbarHostState, message, ToastType.ERROR, duration)
    }

    /**
     * Show success toast
     */
    suspend fun showSuccess(
        snackbarHostState: SnackbarHostState,
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(snackbarHostState, message, ToastType.SUCCESS, duration)
    }

    /**
     * Show info toast
     */
    suspend fun showInfo(
        snackbarHostState: SnackbarHostState,
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(snackbarHostState, message, ToastType.INFO, duration)
    }

    /**
     * Show warning toast
     */
    suspend fun showWarning(
        snackbarHostState: SnackbarHostState,
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(snackbarHostState, message, ToastType.WARNING, duration)
    }
}

/**
 * Extension functions for easier usage with CoroutineScope
 */
fun SnackbarHostState.showError(
    scope: CoroutineScope,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        ToastHelper.showError(this@showError, message, duration)
    }
}

fun SnackbarHostState.showSuccess(
    scope: CoroutineScope,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        ToastHelper.showSuccess(this@showSuccess, message, duration)
    }
}

fun SnackbarHostState.showInfo(
    scope: CoroutineScope,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        ToastHelper.showInfo(this@showInfo, message, duration)
    }
}

fun SnackbarHostState.showWarning(
    scope: CoroutineScope,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    scope.launch {
        ToastHelper.showWarning(this@showWarning, message, duration)
    }
}

/**
 * Custom Toast Snackbar Host
 * Styled toast that appears at the bottom of the screen
 */
@Composable
fun ToastSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier.padding(16.dp)
    ) { data ->
        // Custom styled snackbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1A1A1A),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = data.visuals.message,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
