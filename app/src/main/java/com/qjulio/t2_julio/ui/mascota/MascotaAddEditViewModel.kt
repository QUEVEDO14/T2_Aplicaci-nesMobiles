package com.qjulio.t2_julio.ui.mascota

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qjulio.t2_julio.data.model.Mascota
import com.qjulio.t2_julio.data.repository.AuthRepository
import com.qjulio.t2_julio.data.repository.MascotaRepository
import kotlinx.coroutines.launch

class MascotaAddEditViewModel(
    private val mascotaRepository: MascotaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _guardarResultado = MutableLiveData<Result<Unit>>()
    val guardarResultado: LiveData<Result<Unit>> = _guardarResultado

    fun savePet(mascota: Mascota) {
        viewModelScope.launch {
            val result = mascotaRepository.addPet(mascota)
            _guardarResultado.postValue(result)
        }
    }
}
