package com.lazytravel.ui.screens.trip

import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.models.Trip
import com.lazytravel.data.models.TripDestination
import com.lazytravel.data.models.enums.TripStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TripViewModel {
    private val tripRepository = BaseRepository<Trip>()

    // UI State
    private val _uiState = MutableStateFlow<TripUiState>(TripUiState.Idle)
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    // Current step (0-3)
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    // STEP 1: Basic Info
    private val _tripTitle = MutableStateFlow("")
    val tripTitle: StateFlow<String> = _tripTitle.asStateFlow()

    private val _emoji = MutableStateFlow("🏖️")
    val emoji: StateFlow<String> = _emoji.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _coverImage = MutableStateFlow("")
    val coverImage: StateFlow<String> = _coverImage.asStateFlow()

    private val _startDate = MutableStateFlow(0L)
    val startDate: StateFlow<Long> = _startDate.asStateFlow()

    private val _duration = MutableStateFlow(3)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _region = MutableStateFlow("")
    val region: StateFlow<String> = _region.asStateFlow()

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags: StateFlow<List<String>> = _tags.asStateFlow()

    // STEP 2: Budget & Members
    private val _budgetMin = MutableStateFlow(0.0)
    val budgetMin: StateFlow<Double> = _budgetMin.asStateFlow()

    private val _budgetMax = MutableStateFlow(10.0)
    val budgetMax: StateFlow<Double> = _budgetMax.asStateFlow()

    private val _estimatedBudget = MutableStateFlow(0L)
    val estimatedBudget: StateFlow<Long> = _estimatedBudget.asStateFlow()

    private val _maxParticipants = MutableStateFlow(6)
    val maxParticipants: StateFlow<Int> = _maxParticipants.asStateFlow()

    private val _ageRange = MutableStateFlow("18-35")
    val ageRange: StateFlow<String> = _ageRange.asStateFlow()

    private val _requirements = MutableStateFlow<List<String>>(emptyList())
    val requirements: StateFlow<List<String>> = _requirements.asStateFlow()

    // STEP 3: Destinations
    private val _destinations = MutableStateFlow<List<DestinationInput>>(emptyList())
    val destinations: StateFlow<List<DestinationInput>> = _destinations.asStateFlow()

    private val _allowMultipleVotes = MutableStateFlow(true)
    val allowMultipleVotes: StateFlow<Boolean> = _allowMultipleVotes.asStateFlow()

    private val _votingEndsAt = MutableStateFlow(0L)
    val votingEndsAt: StateFlow<Long> = _votingEndsAt.asStateFlow()

    // STEP 4: Publish
    private val _publishStatus = MutableStateFlow(TripStatus.DRAFT.name)
    val publishStatus: StateFlow<String> = _publishStatus.asStateFlow()

    private val _acceptTerms = MutableStateFlow(false)
    val acceptTerms: StateFlow<Boolean> = _acceptTerms.asStateFlow()

    // Validation
    private val _validation = MutableStateFlow(TripFormValidation(0, false))
    val validation: StateFlow<TripFormValidation> = _validation.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    // ===== STEP 1: BASIC INFO SETTERS =====
    fun setTripTitle(value: String) {
        _tripTitle.value = value
        validateCurrentStep()
    }

    fun setEmoji(value: String) {
        _emoji.value = value
    }

    fun setDescription(value: String) {
        _description.value = value
        validateCurrentStep()
    }

    fun setCoverImage(value: String) {
        _coverImage.value = value
    }

    fun setStartDate(value: Long) {
        _startDate.value = value
        validateCurrentStep()
    }

    fun setDuration(value: Int) {
        _duration.value = value
        validateCurrentStep()
    }

    fun setRegion(value: String) {
        _region.value = value
    }

    fun toggleTag(tag: String) {
        val current = _tags.value.toMutableList()
        if (current.contains(tag)) {
            current.remove(tag)
        } else {
            if (current.size < 5) {
                current.add(tag)
            }
        }
        _tags.value = current
    }

    // ===== STEP 2: BUDGET & MEMBERS SETTERS =====
    fun setBudgetRange(min: Double, max: Double) {
        _budgetMin.value = min
        _budgetMax.value = max
        validateCurrentStep()
    }

    fun setEstimatedBudget(value: Long) {
        _estimatedBudget.value = value
    }

    fun setMaxParticipants(value: Int) {
        _maxParticipants.value = value
        validateCurrentStep()
    }

    fun setAgeRange(value: String) {
        _ageRange.value = value
    }

    fun addRequirement(requirement: String) {
        if (requirement.isNotBlank()) {
            _requirements.value = _requirements.value + requirement
        }
    }

    fun removeRequirement(requirement: String) {
        _requirements.value = _requirements.value - requirement
    }

    // ===== STEP 3: DESTINATIONS SETTERS =====
    fun addDestination(destination: DestinationInput) {
        if (_destinations.value.size < 5) {
            val updated = _destinations.value + destination.copy(
                orderIndex = _destinations.value.size
            )
            _destinations.value = updated
            validateCurrentStep()
        }
    }

    fun updateDestination(index: Int, destination: DestinationInput) {
        val updated = _destinations.value.toMutableList()
        if (index in updated.indices) {
            updated[index] = destination
            _destinations.value = updated
            validateCurrentStep()
        }
    }

    fun removeDestination(index: Int) {
        val updated = _destinations.value.toMutableList()
        if (index in updated.indices) {
            updated.removeAt(index)
            // Update orderIndex
            updated.forEachIndexed { i, dest ->
                updated[i] = dest.copy(orderIndex = i)
            }
            _destinations.value = updated
            validateCurrentStep()
        }
    }

    fun reorderDestinations(from: Int, to: Int) {
        val updated = _destinations.value.toMutableList()
        if (from in updated.indices && to in updated.indices) {
            val item = updated.removeAt(from)
            updated.add(to, item)
            // Update orderIndex
            updated.forEachIndexed { i, dest ->
                updated[i] = dest.copy(orderIndex = i)
            }
            _destinations.value = updated
        }
    }

    fun setAllowMultipleVotes(value: Boolean) {
        _allowMultipleVotes.value = value
    }

    fun setVotingEndsAt(value: Long) {
        _votingEndsAt.value = value
        validateCurrentStep()
    }

    // ===== STEP 4: PUBLISH SETTERS =====
    fun setPublishStatus(value: String) {
        _publishStatus.value = value
    }

    fun setAcceptTerms(value: Boolean) {
        _acceptTerms.value = value
        validateCurrentStep()
    }

    // ===== NAVIGATION =====
    fun nextStep() {
        if (_currentStep.value < 3 && validateCurrentStep()) {
            _currentStep.value += 1
        }
    }

    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value -= 1
        }
    }

    fun goToStep(step: Int) {
        if (step in 0..3) {
            _currentStep.value = step
        }
    }

    // ===== VALIDATION =====
    private fun validateCurrentStep(): Boolean {
        val errors = mutableMapOf<String, String>()
        val isValid = when (_currentStep.value) {
            0 -> validateBasicInfo(errors)
            1 -> validateBudgetMembers(errors)
            2 -> validateDestinations(errors)
            3 -> validatePublish(errors)
            else -> false
        }

        _validation.value = TripFormValidation(
            step = _currentStep.value,
            isValid = isValid,
            errors = errors
        )
        _validationErrors.value = errors

        return isValid
    }

    private fun validateBasicInfo(errors: MutableMap<String, String>): Boolean {
        var isValid = true

        if (_tripTitle.value.isBlank()) {
            errors["tripTitle"] = "Vui lòng nhập tên hành trình"
            isValid = false
        }

        if (_description.value.isBlank()) {
            errors["description"] = "Vui lòng nhập mô tả"
            isValid = false
        }

        if (_startDate.value <= System.currentTimeMillis()) {
            errors["startDate"] = "Ngày khởi hành phải trong tương lai"
            isValid = false
        }

        if (_duration.value < 1) {
            errors["duration"] = "Thời gian phải ít nhất 1 ngày"
            isValid = false
        }

        return isValid
    }

    private fun validateBudgetMembers(errors: MutableMap<String, String>): Boolean {
        var isValid = true

        if (_budgetMin.value <= 0) {
            errors["budgetMin"] = "Ngân sách tối thiểu phải lớn hơn 0"
            isValid = false
        }

        if (_budgetMax.value < _budgetMin.value) {
            errors["budgetMax"] = "Ngân sách tối đa phải lớn hơn ngân sách tối thiểu"
            isValid = false
        }

        if (_maxParticipants.value < 2) {
            errors["maxParticipants"] = "Số người tối đa phải ít nhất 2"
            isValid = false
        }

        return isValid
    }

    private fun validateDestinations(errors: MutableMap<String, String>): Boolean {
        var isValid = true

        if (_destinations.value.size < 2) {
            errors["destinations"] = "Vui lòng thêm ít nhất 2 địa điểm"
            isValid = false
        }

        if (_destinations.value.any { it.name.isBlank() }) {
            errors["destinationName"] = "Tên địa điểm không được để trống"
            isValid = false
        }

        if (_votingEndsAt.value > 0 && _votingEndsAt.value >= _startDate.value) {
            errors["votingEndsAt"] = "Hạn chót vote phải trước ngày khởi hành"
            isValid = false
        }

        return isValid
    }

    private fun validatePublish(errors: MutableMap<String, String>): Boolean {
        if (!_acceptTerms.value) {
            errors["terms"] = "Vui lòng đồng ý với điều khoản"
            return false
        }
        return true
    }

    // ===== ACTIONS =====
    suspend fun saveDraft() {
        try {
            _uiState.value = TripUiState.Loading
            val trip = buildTrip(TripStatus.DRAFT.name)
            val result = tripRepository.createRecord(trip)

            result.fold(
                onSuccess = { createdTrip ->
                    _uiState.value = TripUiState.Success(createdTrip)
                },
                onFailure = { error ->
                    _uiState.value = TripUiState.Error(
                        error.message ?: "Lưu nháp thất bại"
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = TripUiState.Error(e.message ?: "Có lỗi xảy ra")
        }
    }

    suspend fun publishTrip() {
        if (!validateAll()) {
            _uiState.value = TripUiState.Error("Vui lòng kiểm tra lại thông tin")
            return
        }

        try {
            _uiState.value = TripUiState.Loading
            val trip = buildTrip(_publishStatus.value)
            val result = tripRepository.createRecord(trip)

            result.fold(
                onSuccess = { createdTrip ->
                    _uiState.value = TripUiState.Success(createdTrip)
                },
                onFailure = { error ->
                    _uiState.value = TripUiState.Error(
                        error.message ?: "Xuất bản thất bại"
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = TripUiState.Error(e.message ?: "Có lỗi xảy ra")
        }
    }

    private fun validateAll(): Boolean {
        for (step in 0..3) {
            _currentStep.value = step
            if (!validateCurrentStep()) {
                return false
            }
        }
        return true
    }

    private fun buildTrip(status: String): Trip {
        return Trip(
            userId = "", // TODO: Get from auth
            tripTitle = _tripTitle.value,
            description = _description.value,
            coverImage = _coverImage.value,
            emoji = _emoji.value,
            startDate = _startDate.value,
            duration = _duration.value,
            budgetMin = _budgetMin.value,
            budgetMax = _budgetMax.value,
            estimatedBudget = _estimatedBudget.value,
            maxParticipants = _maxParticipants.value,
            ageRange = _ageRange.value,
            requirements = _requirements.value,
            tags = _tags.value,
            region = _region.value,
            allowMultipleVotes = _allowMultipleVotes.value,
            votingEndsAt = _votingEndsAt.value,
            status = status
        )
    }

    fun reset() {
        _uiState.value = TripUiState.Idle
        _currentStep.value = 0
        _tripTitle.value = ""
        _emoji.value = "🏖️"
        _description.value = ""
        _coverImage.value = ""
        _startDate.value = 0L
        _duration.value = 3
        _region.value = ""
        _tags.value = emptyList()
        _budgetMin.value = 0.0
        _budgetMax.value = 10.0
        _estimatedBudget.value = 0L
        _maxParticipants.value = 6
        _ageRange.value = "18-35"
        _requirements.value = emptyList()
        _destinations.value = emptyList()
        _allowMultipleVotes.value = true
        _votingEndsAt.value = 0L
        _publishStatus.value = TripStatus.DRAFT.name
        _acceptTerms.value = false
    }

    // ===== ALIAS FUNCTIONS FOR CreateTripScreen COMPATIBILITY =====
    // Expose imageUrl as alias for coverImage
    val imageUrl: StateFlow<String> = coverImage

    fun updateTripTitle(value: String) = setTripTitle(value)
    fun updateEmoji(value: String) = setEmoji(value)
    fun updateDescription(value: String) = setDescription(value)
    fun updateImageUrl(value: String) = setCoverImage(value)
    fun updateStartDate(value: Long) = setStartDate(value)
    fun updateDuration(value: Int) = setDuration(value)
    fun updateRegion(value: String) = setRegion(value)
    fun updateTags(tags: List<String>) {
        _tags.value = tags
    }
    fun updateBudgetRange(min: Double, max: Double) = setBudgetRange(min, max)
    fun updateMaxParticipants(value: Int) = setMaxParticipants(value)
    fun updateAgeRange(value: String) = setAgeRange(value)
    fun updateAllowMultipleVotes(value: Boolean) = setAllowMultipleVotes(value)
    fun updateVotingEndsAt(value: Long) = setVotingEndsAt(value)
    fun updatePublishStatus(value: String) = setPublishStatus(value)
    fun updateAcceptTerms(value: Boolean) = setAcceptTerms(value)
}
