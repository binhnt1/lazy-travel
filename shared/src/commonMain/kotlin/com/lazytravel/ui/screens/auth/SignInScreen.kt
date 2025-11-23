package com.lazytravel.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazytravel.core.i18n.localizedString
import com.lazytravel.ui.components.atoms.LanguageDropdown
import com.lazytravel.ui.components.molecules.ToastHelper
import com.lazytravel.ui.components.molecules.ToastSnackbarHost
import com.lazytravel.ui.components.molecules.ValidatedTextField
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: AuthViewModel = remember { AuthViewModel() },
    onNavigateBack: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignInSuccess: () -> Unit = {}
) {
    val authState by viewModel.authState.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Validation errors
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> onSignInSuccess()
            is AuthState.Error -> {
                ToastHelper.showError(snackbarHostState, state.message)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { ToastSnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFFAFAFA)
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Gradient Header Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF6B35),
                                Color(0xFFF7931E)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Custom Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    // Back Button
                    Surface(
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .size(40.dp)
                            .align(Alignment.TopStart),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        onClick = onNavigateBack
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }

                    // Language Dropdown
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 48.dp)
                    ) {
                        LanguageDropdown(
                            textColor = Color.White,
                            backgroundColor = Color.White.copy(alpha = 0.2f)
                        )
                    }

                    // Logo and Welcome Text
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✈️",
                            fontSize = 64.sp,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        Text(
                            text = localizedString("auth_welcome_back"),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = localizedString("auth_signin_subtitle"),
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // White Content Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E8E8))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Email/Phone Input with validation
                        ValidatedTextField(
                            value = email,
                            onValueChange = {
                                viewModel.setEmail(it)
                                emailError = null // Clear error on change
                            },
                            label = localizedString("auth_email_or_phone"),
                            placeholder = localizedString("auth_email_or_phone_placeholder"),
                            errorMessage = emailError
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Password Input with validation
                        ValidatedTextField(
                            value = password,
                            onValueChange = {
                                viewModel.setPassword(it)
                                passwordError = null // Clear error on change
                            },
                            label = localizedString("auth_password"),
                            placeholder = localizedString("auth_password_placeholder"),
                            errorMessage = passwordError,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                    Text(if (isPasswordVisible) "👁" else "👁‍🗨", fontSize = 18.sp)
                                }
                            }
                        )

                        // Forgot Password Link
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onForgotPasswordClick) {
                                Text(
                                    text = localizedString("auth_forgot_password_link"),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AppColors.Primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Login Button
                        Button(
                            onClick = {
                                // Validate inputs
                                emailError = ValidationHelper.validateEmailOrPhone(email)
                                passwordError = ValidationHelper.validatePassword(password)

                                // If no errors, proceed with sign in
                                if (emailError == null && passwordError == null) {
                                    coroutineScope.launch {
                                        viewModel.signInWithEmail()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = authState !is AuthState.Loading
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
                                if (authState is AuthState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Text(
                                        text = localizedString("auth_signin_button"),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(24.dp))

                        // Sign Up Link
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = localizedString("auth_no_account"),
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            TextButton(onClick = onSignUpClick) {
                                Text(
                                    text = localizedString("auth_signup_now"),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppColors.Primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
