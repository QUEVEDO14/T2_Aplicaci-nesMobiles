package com.qjulio.t2_julio.ui.mascota

import com.qjulio.t2_julio.data.model.Mascota
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.qjulio.t2_julio.R
import com.qjulio.t2_julio.data.repository.AuthRepository
import com.qjulio.t2_julio.data.repository.MascotaRepository
import java.util.UUID

class MascotaAddEditActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var imageUri: Uri? = null
    private var mascotaExistente: Mascota? = null


    private val mascotaAddEditViewModel: MascotaAddEditViewModel by viewModels {
        MascotaAddEditViewModelFactory(
            mascotaRepository = MascotaRepository(firestore, firebaseAuth),
            authRepository = AuthRepository(firebaseAuth, firestore),
            firestore = firestore,
            firebaseAuth = firebaseAuth
        )
    }

    private lateinit var imageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var imageUrlEditText: EditText

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            imageView.setImageURI(uri)
        } else {
            showToast("No se seleccionó ninguna imagen.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascota_add_edit)

        imageView = findViewById(R.id.imageMascota)
        nameEditText = findViewById(R.id.etNombre)
        descriptionEditText = findViewById(R.id.etDescripcion)
        priceEditText = findViewById(R.id.etPrecio)
        imageUrlEditText = findViewById(R.id.etImagenUrl)


        val seleccionarImagenBtn = findViewById<Button>(R.id.btnSeleccionarImagen)
        val saveButton = findViewById<Button>(R.id.btnGuardar)

        val mascotaId = intent.getStringExtra("mascota_id")
        if (mascotaId != null) {
            mascotaAddEditViewModel.cargarMascotaPorId(mascotaId)
        }

        mascotaAddEditViewModel.mascota.observe(this) { mascota ->
            mascota?.let {
                mascotaExistente = it
                nameEditText.setText(it.name)
                descriptionEditText.setText(it.description)
                priceEditText.setText(it.price.toString())
                imageUrlEditText.setText(it.imageUrl)
                Glide.with(this).load(it.imageUrl).into(imageView)

            }
        }


        seleccionarImagenBtn.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val price = priceEditText.text.toString().toIntOrNull() ?: 0
            val imageUrlFromInput = imageUrlEditText.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                showToast("Por favor completa todos los campos obligatorios")
                return@setOnClickListener
            }

            if (imageUri != null) {
                uploadImageToFirebase(imageUri!!, name, description, price)
            } else if (imageUrlFromInput.isNotEmpty()) {
                guardarMascota(name, description, price, imageUrlFromInput)
            } else if (mascotaExistente != null) {
                guardarMascota(name, description, price, mascotaExistente!!.imageUrl)
            } else {
                showToast("Selecciona una imagen o ingresa una URL")
            }
        }
    }

    private fun uploadImageToFirebase(uri: Uri, name: String, description: String, price: Int) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("mascotas/${UUID.randomUUID()}.jpg")

        imageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Error al subir la imagen")
                }
                imageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                guardarMascota(name, description, price, downloadUri.toString())
            }
            .addOnFailureListener { e ->
                showToast("Error al subir la imagen: ${e.message}")
            }
    }

    private fun guardarMascota(name: String, description: String, price: Int, imageUrl: String) {
        val mascota = Mascota(
            id = mascotaExistente?.id ?: "",
            ownerId = firebaseAuth.currentUser?.uid.orEmpty(),
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl
        )

        mascotaAddEditViewModel.savePet(mascota)
        showToast("Mascota ${if (mascotaExistente == null) "registrada" else "actualizada"} correctamente")
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}