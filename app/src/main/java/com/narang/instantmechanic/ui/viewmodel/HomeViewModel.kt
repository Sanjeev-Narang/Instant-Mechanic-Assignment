package com.narang.instantmechanic.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narang.instantmechanic.domain.Result
import com.narang.instantmechanic.domain.Mechanic
import com.narang.instantmechanic.domain.MechanicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MechanicRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<Result<List<Mechanic>>>(Result.Loading)
    val uiState: LiveData<Result<List<Mechanic>>> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = Result.Loading
            Timber.d("HomeViewModel: fetching mechanics")
            val result = repository.getMechanics()
            _uiState.value = result
            when (result) {
                is Result.Success -> Timber.d("Loaded %d mechanics", result.data.size)
                is Result.Error -> Timber.e("Error: %s", result.message)
                else -> Unit
            }
        }
    }

    fun retry() = refresh()
}
