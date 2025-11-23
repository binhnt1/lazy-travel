package com.lazytravel.ui.screens.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.lazytravel.ui.components.cards.trip.CreateTripBottomBar
import com.lazytravel.ui.components.cards.trip.StepIndicator
import com.lazytravel.ui.screens.trip.steps.BasicInfoStep
import com.lazytravel.ui.screens.trip.steps.BudgetMembersStep
import com.lazytravel.ui.screens.trip.steps.DestinationsStep
import com.lazytravel.ui.screens.trip.steps.PreviewPublishStep
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    viewModel: TripViewModel = TripViewModel(),
    onNavigateBack: () -> Unit,
    onTripCreated: (String) -> Unit
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val validationErrors by viewModel.validationErrors.collectAsState()

    // Form fields
    val tripTitle by viewModel.tripTitle.collectAsState()
    val emoji by viewModel.emoji.collectAsState()
    val description by viewModel.description.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val region by viewModel.region.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val budgetMin by viewModel.budgetMin.collectAsState()
    val budgetMax by viewModel.budgetMax.collectAsState()
    val maxParticipants by viewModel.maxParticipants.collectAsState()
    val ageRange by viewModel.ageRange.collectAsState()
    val requirements by viewModel.requirements.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val allowMultipleVotes by viewModel.allowMultipleVotes.collectAsState()
    val votingEndsAt by viewModel.votingEndsAt.collectAsState()
    val publishStatus by viewModel.publishStatus.collectAsState()
    val acceptTerms by viewModel.acceptTerms.collectAsState()

    var showDiscardDialog by remember { mutableStateOf(false) }
    var showSaveDraftDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is TripUiState.Success -> {
                onTripCreated(state.trip.id)
            }
            is TripUiState.Error -> {
                // Error is already shown in validation errors
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tạo hành trình mới",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Show discard dialog if there are changes
                        if (tripTitle.isNotBlank() || description.isNotBlank() || destinations.isNotEmpty()) {
                            showDiscardDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Save Draft button
                    TextButton(
                        onClick = { showSaveDraftDialog = true },
                        enabled = tripTitle.isNotBlank()
                    ) {
                        Text(
                            text = "Lưu nháp",
                            color = Color(0xFFFF6B35)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            CreateTripBottomBar(
                currentStep = currentStep,
                totalSteps = 4,
                canProceed = validationErrors.isEmpty() || currentStep < 3,
                onBack = { viewModel.previousStep() },
                onNext = { viewModel.nextStep() },
                onPublish = {
                    scope.launch {
                        viewModel.publishTrip()
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Step Indicator
            StepIndicator(currentStep = currentStep)

            HorizontalDivider()

            // Loading overlay
            if (uiState is TripUiState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFF6B35)
                    )
                }
            } else {
                // Step Content
                when (currentStep) {
                    0 -> BasicInfoStep(
                        tripTitle = tripTitle,
                        onTripTitleChange = viewModel::updateTripTitle,
                        emoji = emoji,
                        onEmojiChange = viewModel::updateEmoji,
                        description = description,
                        onDescriptionChange = viewModel::updateDescription,
                        imageUrl = imageUrl,
                        onImageUrlChange = viewModel::updateImageUrl,
                        startDate = startDate,
                        onStartDateChange = viewModel::updateStartDate,
                        duration = duration,
                        onDurationChange = viewModel::updateDuration,
                        region = region,
                        onRegionChange = viewModel::updateRegion,
                        tags = tags,
                        onTagsChange = viewModel::updateTags,
                        validationErrors = validationErrors
                    )

                    1 -> BudgetMembersStep(
                        budgetMin = budgetMin,
                        budgetMax = budgetMax,
                        onBudgetRangeChange = viewModel::updateBudgetRange,
                        maxParticipants = maxParticipants,
                        onMaxParticipantsChange = viewModel::updateMaxParticipants,
                        ageRange = ageRange,
                        onAgeRangeChange = viewModel::updateAgeRange,
                        requirements = requirements,
                        onAddRequirement = viewModel::addRequirement,
                        onRemoveRequirement = viewModel::removeRequirement,
                        validationErrors = validationErrors
                    )

                    2 -> DestinationsStep(
                        destinations = destinations,
                        onAddDestination = {
                            // Add empty destination for user to fill in
                            viewModel.addDestination(
                                DestinationInput(
                                    name = "",
                                    description = "",
                                    imageUrl = "",
                                    orderIndex = destinations.size
                                )
                            )
                        },
                        onUpdateDestination = viewModel::updateDestination,
                        onDeleteDestination = viewModel::removeDestination,
                        allowMultipleVotes = allowMultipleVotes,
                        onAllowMultipleVotesChange = viewModel::updateAllowMultipleVotes,
                        votingEndsAt = votingEndsAt,
                        onVotingEndsAtChange = viewModel::updateVotingEndsAt,
                        validationErrors = validationErrors
                    )

                    3 -> PreviewPublishStep(
                        tripTitle = tripTitle,
                        emoji = emoji,
                        description = description,
                        startDate = startDate,
                        duration = duration,
                        region = region,
                        tags = tags,
                        budgetMin = budgetMin,
                        budgetMax = budgetMax,
                        maxParticipants = maxParticipants,
                        ageRange = ageRange,
                        requirements = requirements,
                        destinations = destinations,
                        allowMultipleVotes = allowMultipleVotes,
                        votingEndsAt = votingEndsAt,
                        publishStatus = publishStatus,
                        onPublishStatusChange = viewModel::updatePublishStatus,
                        acceptTerms = acceptTerms,
                        onAcceptTermsChange = viewModel::updateAcceptTerms,
                        onEditBasicInfo = { viewModel.goToStep(0) },
                        onEditBudget = { viewModel.goToStep(1) },
                        onEditDestinations = { viewModel.goToStep(2) },
                        validationErrors = validationErrors
                    )
                }
            }
        }
    }

    // Discard Changes Dialog
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = {
                Text("Hủy tạo hành trình?")
            },
            text = {
                Text("Bạn có chắc muốn hủy? Mọi thay đổi sẽ không được lưu.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hủy bỏ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Tiếp tục")
                }
            }
        )
    }

    // Save Draft Dialog
    if (showSaveDraftDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDraftDialog = false },
            title = {
                Text("Lưu nháp")
            },
            text = {
                Text("Bạn có muốn lưu hành trình này dưới dạng nháp? Bạn có thể tiếp tục chỉnh sửa sau.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            viewModel.saveDraft()
                            showSaveDraftDialog = false
                            // Optionally navigate back or show success message
                        }
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDraftDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
