package com.qjulio.t2_julio.ui.mascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qjulio.t2_julio.data.repository.AuthRepository
import com.qjulio.t2_julio.data.repository.MascotaRepository

class MascotaAddEditViewModelFactory(
    private val mascotaRepository: MascotaRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MascotaAddEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MascotaAddEditViewModel(mascotaRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
