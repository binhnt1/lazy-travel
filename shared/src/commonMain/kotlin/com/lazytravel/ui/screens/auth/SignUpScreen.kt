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
import com.lazytravel.ui.components.atoms.GenderOption
import com.lazytravel.ui.components.atoms.LanguageDropdown
import com.lazytravel.ui.components.molecules.ToastHelper
import com.lazytravel.ui.components.molecules.ToastSnackbarHost
import com.lazytravel.ui.components.molecules.ValidatedTextField
import com.lazytravel.ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = remember { AuthViewModel() },
    onNavigateBack: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    val authState by viewModel.authState.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showAdvancedForm by remember { mutableStateOf(true) }  // Expanded by default

    // Validation errors
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Additional fields for birthday and gender
    var selectedDay by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    // Precompute month names for use in non-Composable context
    val monthNames = (1..12).map { month ->
        month to localizedString("auth_month_$month")
    }

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> onSignUpSuccess()
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
                    .height(260.dp)
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
                        .height(260.dp)
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
                            text = localizedString("auth_create_account"),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = localizedString("auth_signup_subtitle"),
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // White Content Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp),
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
                        // Email/Phone Signup Form
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            onClick = { showAdvancedForm = !showAdvancedForm }
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "✉️", fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = localizedString("auth_signup_with_email"),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1A1A1A)
                                        )
                                    }
                                    Text(
                                        text = if (showAdvancedForm) "▲" else "▼",
                                        fontSize = 16.sp,
                                        color = AppColors.Primary
                                    )
                                }

                                if (showAdvancedForm) {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Full Name with validation
                                    ValidatedTextField(
                                        value = fullName,
                                        onValueChange = {
                                            viewModel.setFullName(it)
                                            fullNameError = null
                                        },
                                        label = localizedString("auth_full_name"),
                                        placeholder = localizedString("auth_full_name_placeholder"),
                                        errorMessage = fullNameError
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Email with validation
                                    ValidatedTextField(
                                        value = email,
                                        onValueChange = {
                                            viewModel.setEmail(it)
                                            emailError = null
                                        },
                                        label = localizedString("auth_email_or_phone"),
                                        placeholder = localizedString("auth_email_or_phone_placeholder"),
                                        errorMessage = emailError
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Phone with validation
                                    ValidatedTextField(
                                        value = phone,
                                        onValueChange = {
                                            viewModel.setPhone(it)
                                            phoneError = null
                                        },
                                        label = localizedString("auth_phone"),
                                        placeholder = localizedString("auth_phone_placeholder"),
                                        errorMessage = phoneError
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Birthday fields
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = localizedString("auth_birthday"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1A1A1A),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Day
                                            var dayExpanded by remember { mutableStateOf(false) }
                                            ExposedDropdownMenuBox(
                                                expanded = dayExpanded,
                                                onExpandedChange = { dayExpanded = !dayExpanded },
                                                modifier = Modifier.weight(0.8f)
                                            ) {
                                                OutlinedTextField(
                                                    value = selectedDay.ifEmpty { localizedString("auth_day") },
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = AppColors.Primary,
                                                        unfocusedBorderColor = Color(0xFFE8E8E8),
                                                        focusedContainerColor = Color.White,
                                                        unfocusedContainerColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(8.dp),
                                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = dayExpanded,
                                                    onDismissRequest = { dayExpanded = false }
                                                ) {
                                                    (1..31).forEach { day ->
                                                        DropdownMenuItem(
                                                            text = { Text(day.toString()) },
                                                            onClick = {
                                                                selectedDay = day.toString()
                                                                dayExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            // Month
                                            var monthExpanded by remember { mutableStateOf(false) }
                                            ExposedDropdownMenuBox(
                                                expanded = monthExpanded,
                                                onExpandedChange = { monthExpanded = !monthExpanded },
                                                modifier = Modifier.weight(1.2f)
                                            ) {
                                                OutlinedTextField(
                                                    value = selectedMonth.ifEmpty { localizedString("auth_month") },
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = AppColors.Primary,
                                                        unfocusedBorderColor = Color(0xFFE8E8E8),
                                                        focusedContainerColor = Color.White,
                                                        unfocusedContainerColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(8.dp),
                                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = monthExpanded,
                                                    onDismissRequest = { monthExpanded = false }
                                                ) {
                                                    monthNames.forEach { (monthNum, monthName) ->
                                                        DropdownMenuItem(
                                                            text = { Text(monthName) },
                                                            onClick = {
                                                                selectedMonth = monthName
                                                                monthExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            // Year
                                            var yearExpanded by remember { mutableStateOf(false) }
                                            ExposedDropdownMenuBox(
                                                expanded = yearExpanded,
                                                onExpandedChange = { yearExpanded = !yearExpanded },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                OutlinedTextField(
                                                    value = selectedYear.ifEmpty { localizedString("auth_year") },
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = AppColors.Primary,
                                                        unfocusedBorderColor = Color(0xFFE8E8E8),
                                                        focusedContainerColor = Color.White,
                                                        unfocusedContainerColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(8.dp),
                                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = yearExpanded,
                                                    onDismissRequest = { yearExpanded = false }
                                                ) {
                                                    (2012 downTo 1950).forEach { year ->
                                                        DropdownMenuItem(
                                                            text = { Text(year.toString()) },
                                                            onClick = {
                                                                selectedYear = year.toString()
                                                                yearExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Gender selection
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = localizedString("auth_gender"),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1A1A1A),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            GenderOption(
                                                text = localizedString("auth_gender_male"),
                                                isSelected = selectedGender == "male",
                                                onClick = { selectedGender = "male" },
                                                modifier = Modifier.weight(1f)
                                            )
                                            GenderOption(
                                                text = localizedString("auth_gender_female"),
                                                isSelected = selectedGender == "female",
                                                onClick = { selectedGender = "female" },
                                                modifier = Modifier.weight(1f)
                                            )
                                            GenderOption(
                                                text = localizedString("auth_gender_other"),
                                                isSelected = selectedGender == "other",
                                                onClick = { selectedGender = "other" },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Password with validation
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        ValidatedTextField(
                                            value = password,
                                            onValueChange = {
                                                viewModel.setPassword(it)
                                                passwordError = null
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
                                        if (passwordError == null) {
                                            Text(
                                                text = localizedString("auth_password_hint"),
                                                fontSize = 12.sp,
                                                color = Color(0xFF999999),
                                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Confirm Password with validation
                                    ValidatedTextField(
                                        value = confirmPassword,
                                        onValueChange = {
                                            viewModel.setConfirmPassword(it)
                                            confirmPasswordError = null
                                        },
                                        label = localizedString("auth_confirm_password"),
                                        placeholder = localizedString("auth_confirm_password_placeholder"),
                                        errorMessage = confirmPasswordError,
                                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    // Sign Up Button
                                    Button(
                                        onClick = {
                                            // Validate inputs
                                            fullNameError = ValidationHelper.validateName(fullName)
                                            emailError = ValidationHelper.validateEmail(email)
                                            phoneError = ValidationHelper.validatePhone(phone)
                                            passwordError = ValidationHelper.validatePassword(password)
                                            confirmPasswordError = ValidationHelper.validateConfirmPassword(password, confirmPassword)

                                            // If no errors, proceed with sign up
                                            if (fullNameError == null &&
                                                emailError == null &&
                                                phoneError == null &&
                                                passwordError == null &&
                                                confirmPasswordError == null) {
                                                coroutineScope.launch {
                                                    viewModel.signUpWithEmail()
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
                                                    text = localizedString("auth_signup_button"),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Benefits Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E8E8))
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                                Text(
                                    text = localizedString("auth_signup_benefits"),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A1A),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                BenefitItem("✓", localizedString("auth_benefit_1"))
                                BenefitItem("✓", localizedString("auth_benefit_2"))
                                BenefitItem("✓", localizedString("auth_benefit_3"))
                                BenefitItem("✓", localizedString("auth_benefit_4"))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Terms Text
                        Text(
                            text = localizedString("auth_terms"),
                            fontSize = 12.sp,
                            color = Color(0xFF999999),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Sign In Link
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = localizedString("auth_have_account"),
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            TextButton(onClick = onSignInClick) {
                                Text(
                                    text = localizedString("auth_signin_now"),
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

@Composable
fun BenefitItem(icon: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp, color = AppColors.Primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, fontSize = 13.sp, color = Color(0xFF666666), lineHeight = 18.sp)
    }
}

