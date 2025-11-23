package com.lazytravel.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.LocalizationManager
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.ui.components.atoms.LanguageDropdown
import com.lazytravel.ui.components.molecules.ToastHelper
import com.lazytravel.ui.components.molecules.ToastSnackbarHost
import com.lazytravel.ui.components.molecules.ValidatedTextField
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Forgot Password Screen with 3-step Timeline
 * Step 1: Enter Email/Phone
 * Step 2: Enter Verification Code
 * Step 3: Set New Password
 */

enum class ForgotPasswordStep {
    STEP1_ENTER_EMAIL,
    STEP2_VERIFY_CODE,
    STEP3_NEW_PASSWORD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = remember { ForgotPasswordViewModel() },
    onBackClick: () -> Unit = {},
    onResetSuccess: () -> Unit = {}
) {
    var currentStep by remember { mutableStateOf(ForgotPasswordStep.STEP1_ENTER_EMAIL) }
    val resetState by viewModel.resetState.collectAsState()
    val email by viewModel.email.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Validation errors
    var emailError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var codeError by remember { mutableStateOf<String?>(null) }

    // 6-digit code inputs
    val codeInputs = remember { mutableStateListOf("", "", "", "", "", "") }

    // Resend timer
    var resendCountdown by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(resetState) {
        when (val state = resetState) {
            is PasswordResetState.EmailSent -> {
                currentStep = ForgotPasswordStep.STEP2_VERIFY_CODE
                // Start countdown
                canResend = false
                resendCountdown = 60
                launch {
                    while (resendCountdown > 0) {
                        delay(1000)
                        resendCountdown--
                    }
                    canResend = true
                }
            }
            is PasswordResetState.PasswordReset -> onResetSuccess()
            is PasswordResetState.Error -> {
                ToastHelper.showError(snackbarHostState, state.message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { ToastSnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            // Custom Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
                    .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E8E8)),
                        color = Color.White,
                        onClick = onBackClick
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF1A1A1A)
                            )
                        }
                    }

                    Text(
                        text = localizedString("forgot_password_title"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )

                    // Language Dropdown in top right
                    LanguageDropdown(
                        textColor = Color(0xFF1A1A1A),
                        backgroundColor = Color(0xFFF5F5F5)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timeline Progress
                TimelineProgress(currentStep = currentStep)

            Spacer(modifier = Modifier.height(32.dp))

            when (currentStep) {
                ForgotPasswordStep.STEP1_ENTER_EMAIL -> {
                    Step1EnterEmail(
                        email = email,
                        emailError = emailError,
                        onEmailChange = {
                            viewModel.setEmail(it)
                            emailError = null
                        },
                        isLoading = resetState is PasswordResetState.Loading,
                        onSendCode = {
                            // Validate email/phone
                            emailError = ValidationHelper.validateEmailOrPhone(email)

                            if (emailError == null) {
                                coroutineScope.launch {
                                    viewModel.requestPasswordReset()
                                }
                            }
                        }
                    )
                }

                ForgotPasswordStep.STEP2_VERIFY_CODE -> {
                    Step2VerifyCode(
                        email = email,
                        codeInputs = codeInputs,
                        codeError = codeError,
                        resendCountdown = resendCountdown,
                        canResend = canResend,
                        onVerify = {
                            val code = codeInputs.joinToString("")
                            codeError = ValidationHelper.validateRequired(code)

                            if (codeError == null && code.length == 6) {
                                viewModel.setResetToken(code)
                                currentStep = ForgotPasswordStep.STEP3_NEW_PASSWORD
                                codeError = null
                            } else if (code.length < 6) {
                                codeError = LocalizationManager.getString("validation_code_invalid")
                            }
                        },
                        onResend = {
                            coroutineScope.launch {
                                viewModel.requestPasswordReset()
                            }
                        }
                    )
                }

                ForgotPasswordStep.STEP3_NEW_PASSWORD -> {
                    Step3NewPassword(
                        newPassword = newPassword,
                        confirmPassword = confirmPassword,
                        newPasswordError = newPasswordError,
                        confirmPasswordError = confirmPasswordError,
                        isPasswordVisible = isPasswordVisible,
                        onNewPasswordChange = {
                            viewModel.setNewPassword(it)
                            newPasswordError = null
                        },
                        onConfirmPasswordChange = {
                            viewModel.setConfirmPassword(it)
                            confirmPasswordError = null
                        },
                        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
                        isLoading = resetState is PasswordResetState.ResettingPassword,
                        onResetPassword = {
                            // Validate passwords
                            newPasswordError = ValidationHelper.validatePassword(newPassword)
                            confirmPasswordError = ValidationHelper.validateConfirmPassword(newPassword, confirmPassword)

                            if (newPasswordError == null && confirmPasswordError == null) {
                                coroutineScope.launch {
                                    viewModel.resetPassword()
                                }
                            }
                        }
                    )
                }
            }
            }
        }
    }
}

@Composable
private fun TimelineProgress(currentStep: ForgotPasswordStep) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step 1
        TimelineStepItem(
            stepNumber = "1",
            label = localizedString("forgot_password_step1_label"),
            isActive = currentStep == ForgotPasswordStep.STEP1_ENTER_EMAIL,
            isCompleted = currentStep.ordinal > ForgotPasswordStep.STEP1_ENTER_EMAIL.ordinal
        )

        // Line 1->2
        TimelineLine(
            isActive = currentStep.ordinal > ForgotPasswordStep.STEP1_ENTER_EMAIL.ordinal
        )

        // Step 2
        TimelineStepItem(
            stepNumber = "2",
            label = localizedString("forgot_password_step2_label"),
            isActive = currentStep == ForgotPasswordStep.STEP2_VERIFY_CODE,
            isCompleted = currentStep.ordinal > ForgotPasswordStep.STEP2_VERIFY_CODE.ordinal
        )

        // Line 2->3
        TimelineLine(
            isActive = currentStep.ordinal > ForgotPasswordStep.STEP2_VERIFY_CODE.ordinal
        )

        // Step 3
        TimelineStepItem(
            stepNumber = "3",
            label = localizedString("forgot_password_step3_label"),
            isActive = currentStep == ForgotPasswordStep.STEP3_NEW_PASSWORD,
            isCompleted = false
        )
    }
}

@Composable
private fun TimelineStepItem(
    stepNumber: String,
    label: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    brush = if (isActive || isCompleted) {
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                        )
                    } else {
                        Brush.linearGradient(colors = listOf(Color.White, Color.White))
                    },
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = if (isActive || isCompleted) Color(0xFFFF6B35) else Color(0xFFE8E8E8),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isCompleted) "✓" else stepNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive || isCompleted) Color.White else Color(0xFF999999)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
            color = when {
                isActive -> Color(0xFFFF6B35)
                isCompleted -> Color(0xFF1A1A1A)
                else -> Color(0xFF999999)
            }
        )
    }
}

@Composable
private fun TimelineLine(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(2.dp)
            .offset(y = (-16).dp)
            .background(if (isActive) Color(0xFFFF6B35) else Color(0xFFE8E8E8))
    )
}

@Composable
private fun Step1EnterEmail(
    email: String,
    emailError: String?,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    onSendCode: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🔒", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = localizedString("forgot_password_title"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = localizedString("forgot_password_subtitle"),
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email/Phone Input with Validation
        ValidatedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = localizedString("auth_email_or_phone"),
            placeholder = localizedString("auth_email_or_phone_placeholder"),
            errorMessage = emailError
        )

        Text(
            text = localizedString("forgot_password_verification_sent"),
            fontSize = 12.sp,
            color = Color(0xFF999999),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Send Button
        Button(
            onClick = onSendCode,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = !isLoading
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = localizedString("forgot_password_send_code"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Box
        InfoBox(
            title = localizedString("forgot_password_info_title"),
            items = listOf(
                localizedString("forgot_password_code_validity"),
                localizedString("forgot_password_check_spam"),
                localizedString("forgot_password_resend_info")
            )
        )
    }
}

@Composable
private fun Step2VerifyCode(
    email: String,
    codeInputs: MutableList<String>,
    codeError: String?,
    resendCountdown: Int,
    canResend: Boolean,
    onVerify: () -> Unit,
    onResend: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "📧", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = localizedString("forgot_password_enter_code"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = localizedString("forgot_password_code_sent_to"),
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
        Text(
            text = email,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Code Inputs
        Text(
            text = localizedString("forgot_password_enter_code"),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            codeInputs.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            codeInputs[index] = newValue
                        }
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(56.dp),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = codeError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.Primary,
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        errorBorderColor = Color(0xFFFF3B30),
                        focusedContainerColor = Color(0xFFFFF5F0),
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        // Error message
        if (codeError != null) {
            Text(
                text = codeError,
                fontSize = 12.sp,
                color = Color(0xFFFF3B30),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = localizedString("forgot_password_code_validity"),
            fontSize = 13.sp,
            color = Color(0xFF999999),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Verify Button
        Button(
            onClick = onVerify,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedString("forgot_password_verify"),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resend
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizedString("forgot_password_no_code"),
                fontSize = 13.sp,
                color = Color(0xFF666666)
            )
            TextButton(
                onClick = onResend,
                enabled = canResend
            ) {
                Text(
                    text = if (canResend) localizedString("forgot_password_resend") else "${localizedString("forgot_password_resend")} (${resendCountdown}s)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (canResend) AppColors.Primary else Color(0xFF999999)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Info Box
        InfoBox(
            title = localizedString("forgot_password_tips_title"),
            items = listOf(
                localizedString("forgot_password_check_spam"),
                localizedString("forgot_password_verify_email")
            )
        )
    }
}

@Composable
private fun Step3NewPassword(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    isPasswordVisible: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    isLoading: Boolean,
    onResetPassword: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🔑", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = localizedString("forgot_password_new_password"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = localizedString("forgot_password_set_subtitle"),
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // New Password with Validation
        ValidatedTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = localizedString("forgot_password_new_password"),
            placeholder = localizedString("forgot_password_new_password_placeholder"),
            errorMessage = newPasswordError,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Text(if (isPasswordVisible) "👁" else "👁‍🗨", fontSize = 18.sp)
                }
            }
        )

        Text(
            text = localizedString("auth_password_hint"),
            fontSize = 12.sp,
            color = Color(0xFF999999),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password with Validation
        ValidatedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = localizedString("forgot_password_confirm_new_password"),
            placeholder = localizedString("forgot_password_new_password_placeholder"),
            errorMessage = confirmPasswordError,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reset Button
        Button(
            onClick = onResetPassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = !isLoading
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = localizedString("forgot_password_reset_button"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Box
        InfoBox(
            title = localizedString("forgot_password_strong_password_title"),
            items = listOf(
                localizedString("forgot_password_password_min_chars"),
                localizedString("forgot_password_password_mix_chars"),
                localizedString("forgot_password_password_no_personal")
            )
        )
    }
}

@Composable
private fun InfoBox(
    title: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E8E8)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            items.forEach { item ->
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 13.sp,
                        color = AppColors.Primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = Color(0xFF666666),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
