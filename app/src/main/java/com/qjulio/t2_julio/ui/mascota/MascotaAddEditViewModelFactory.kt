package com.qjulio.t2_julio.ui.mascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qjulio.t2_julio.data.repository.AuthRepository
import com.qjulio.t2_julio.data.repository.MascotaRepository

class MascotaAddEditViewModelFactory(
    private val mascotaRepository: MascotaRepository,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MascotaAddEditViewModel::class.java)) {
            MascotaAddEditViewModel(
                mascotaRepository,
                authRepository,
                firestore,
                firebaseAuth
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}