package com.qjulio.t2_julio.data.model

import com.google.firebase.firestore.DocumentId

data class Mascota(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Int = 0,
    val ownerId: String = ""
)