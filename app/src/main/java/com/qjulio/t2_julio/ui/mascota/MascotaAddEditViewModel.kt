package com.qjulio.t2_julio.ui.mascota

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qjulio.t2_julio.data.model.Mascota
import com.qjulio.t2_julio.data.repository.AuthRepository
import com.qjulio.t2_julio.data.repository.MascotaRepository

class MascotaAddEditViewModel(
    private val mascotaRepository: MascotaRepository,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _guardarResultado = MutableLiveData<Boolean>()
    val guardarResultado: LiveData<Boolean> = _guardarResultado

    private val _mascota = MutableLiveData<Mascota?>()
    val mascota: LiveData<Mascota?> = _mascota

    fun savePet(mascota: Mascota) {
        val id = mascota.id.takeIf { it.isNotEmpty() } ?: firestore.collection("mascotas").document().id
        val nuevaMascota = mascota.copy(id = id)
        firestore.collection("mascotas").document(id)
            .set(nuevaMascota)
            .addOnSuccessListener {
                _guardarResultado.value = true
            }
            .addOnFailureListener {
                _guardarResultado.value = false
            }
    }

    fun cargarMascotaPorId(id: String) {
        firestore.collection("mascotas").document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val mascota = document.toObject(Mascota::class.java)?.copy(id = document.id)
                    _mascota.value = mascota
                } else {
                    _mascota.value = null
                }
            }
            .addOnFailureListener {
                _mascota.value = null
            }
    }

}