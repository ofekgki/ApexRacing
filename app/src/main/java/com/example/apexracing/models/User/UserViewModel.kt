package com.example.apexracing.models.User

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apexracing.utilities.DBData
import com.example.apexracing.utilities.Mapper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {

    enum class PickType { DRIVER, CONSTRUCTOR }

    sealed class UserUiState {
        data object Idle : UserUiState()
        data object Loading : UserUiState()
        data class Ready(
            val user: User,
            val userIds: UserIds,
            val uid: String
        ) : UserUiState()

        data class Error(val message: String) : UserUiState()
    }

    private val _state = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val state: StateFlow<UserUiState> = _state

    fun loadUser(uid: String) {
        viewModelScope.launch {
            try {
                _state.value = UserUiState.Loading

                val snap = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .get()
                    .await()

                val ids = snap.getValue(UserIds::class.java)
                    ?: throw IllegalStateException("User not found in RTDB")

                val user = Mapper.userMapper(ids)

                _state.value = UserUiState.Ready(
                    user = user,
                    userIds = ids,
                    uid = uid
                )
            } catch (e: Exception) {
                _state.value = UserUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clear() {
        _state.value = UserUiState.Idle
    }

    fun toggleFantasyItem(type: PickType, pickedId: String, priceM: Float) {
        val ready = state.value as? UserUiState.Ready ?: return

        val alreadyPicked = when (type) {
            PickType.DRIVER -> ready.user.fantasyDriver.any { it.id == pickedId }
            PickType.CONSTRUCTOR -> ready.user.fantasyConstructor.any { it.id == pickedId }
        }

        if (alreadyPicked) {
            removeFantasyItem(type, pickedId, priceM)
        } else {
            pickFantasyItem(type, pickedId, priceM)
        }
    }

    fun pickFantasyItem(type: PickType, pickedId: String, priceM: Float) {
        val ready = state.value as? UserUiState.Ready ?: return
        val dto = ready.userIds
        val user = ready.user
        val uid = ready.uid

        val alreadyPicked =
            dto.fantasyDriverIds.contains(pickedId) ||
                    dto.fantasyConstructorIds.contains(pickedId)
        if (alreadyPicked) return

        val canPick = when (type) {
            PickType.DRIVER -> dto.fantasyDriverIds.size < 3
            PickType.CONSTRUCTOR -> dto.fantasyConstructorIds.size < 2
        }
        if (!canPick) return

        val currentBudget = user.fantasyBudget ?: 0f
        val newBudget = currentBudget - priceM
        if (newBudget < 0f) {

            return
        }

        val pickedDriver =
            if (type == PickType.DRIVER) DBData.drivers.find { it.id == pickedId } else null
        val pickedTeam =
            if (type == PickType.CONSTRUCTOR) DBData.teams.find { it.id == pickedId } else null

        if (type == PickType.DRIVER && pickedDriver == null) return
        if (type == PickType.CONSTRUCTOR && pickedTeam == null) return

        val newDto = when (type) {
            PickType.DRIVER ->
                dto.copy(fantasyDriverIds = dto.fantasyDriverIds + pickedId)

            PickType.CONSTRUCTOR ->
                dto.copy(fantasyConstructorIds = dto.fantasyConstructorIds + pickedId)
        }.copy(fantasyBudget = newBudget)

        val newUser = when (type) {
            PickType.DRIVER ->
                user.copy(fantasyDriver = user.fantasyDriver + pickedDriver!!)

            PickType.CONSTRUCTOR ->
                user.copy(fantasyConstructor = user.fantasyConstructor + pickedTeam!!)
        }.copy(fantasyBudget = newBudget)

        _state.value = UserUiState.Ready(
            user = newUser,
            userIds = newDto,
            uid = uid
        )
    }

    fun removeFantasyItem(type: PickType, removeId: String, priceM: Float) {
        val ready = state.value as? UserUiState.Ready ?: return
        val dto = ready.userIds
        val user = ready.user
        val uid = ready.uid

        val exists = when (type) {
            PickType.DRIVER -> dto.fantasyDriverIds.contains(removeId)
            PickType.CONSTRUCTOR -> dto.fantasyConstructorIds.contains(removeId)
        }
        if (!exists) return

        val currentBudget = user.fantasyBudget ?: 0f
        val newBudget = currentBudget + priceM

        val newDto = when (type) {
            PickType.DRIVER ->
                dto.copy(fantasyDriverIds = dto.fantasyDriverIds.filter { it != removeId })

            PickType.CONSTRUCTOR ->
                dto.copy(fantasyConstructorIds = dto.fantasyConstructorIds.filter { it != removeId })
        }.copy(fantasyBudget = newBudget)

        val newUser = when (type) {
            PickType.DRIVER ->
                user.copy(fantasyDriver = user.fantasyDriver.filter { it.id != removeId })

            PickType.CONSTRUCTOR ->
                user.copy(fantasyConstructor = user.fantasyConstructor.filter { it.id != removeId })
        }.copy(fantasyBudget = newBudget)

        _state.value = UserUiState.Ready(
            user = newUser,
            userIds = newDto,
            uid = uid
        )
    }

    fun clearFantasyGrid(capBudget: Float = 100.0f) {
        val ready = state.value as? UserUiState.Ready ?: return
        val dto = ready.userIds
        val user = ready.user
        val uid = ready.uid

        val newDto = dto.copy(
            fantasyDriverIds = emptyList(),
            fantasyConstructorIds = emptyList(),
            fantasyBudget = capBudget
        )

        val newUser = user.copy(
            fantasyDriver = emptyList(),
            fantasyConstructor = emptyList(),
            fantasyBudget = capBudget
        )

        _state.value = UserUiState.Ready(
            user = newUser,
            userIds = newDto,
            uid = uid
        )
    }

    fun saveFantasyTeam() {
        val ready = state.value as? UserUiState.Ready ?: return
        saveUserIdsToRTDB(ready.uid, ready.userIds)
    }

    private fun saveUserIdsToRTDB(uid: String, dto: UserIds) {
        viewModelScope.launch {
            try {
                FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .setValue(dto)
                    .await()
            } catch (e: Exception) {
                _state.value = UserUiState.Error(e.message ?: "Failed to save team")
            }
        }
    }

    fun reloadUser(uid: String) {
        loadUser(uid)
    }
}


