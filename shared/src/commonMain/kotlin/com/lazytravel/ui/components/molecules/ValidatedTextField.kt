package com.lazytravel.ui.components.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.ui.theme.AppColors

/**
 * Validated Text Field - Text field with inline error message support
 *
 * Shows validation error message below the field
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    val isError = errorMessage != null

    Column(modifier = modifier.fillMaxWidth()) {
        // Label
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) Color(0xFFE53935) else Color(0xFF1A1A1A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color(0xFF999999), fontSize = 14.sp) },
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color(0xFFE53935) else AppColors.Primary,
                unfocusedBorderColor = if (isError) Color(0xFFE53935) else Color(0xFFE8E8E8),
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                errorBorderColor = Color(0xFFE53935),
                errorContainerColor = Color(0xFFFFF5F5)
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions,
            singleLine = singleLine
        )

        // Error Message
        if (isError) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFE53935),
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
