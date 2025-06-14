package com.qjulio.t2_julio.ui.mascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.qjulio.t2_julio.databinding.ActivityMascotaListBinding
import com.qjulio.t2_julio.ui.auth.LoginActivity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MascotaListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMascotaListBinding
    private lateinit var adapter: MascotaAdapter
    private lateinit var viewModel: MascotaListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MascotaListViewModel::class.java]
        binding = ActivityMascotaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MascotaAdapter(emptyList(),
            onEditarClick = { mascota ->
                val intent = Intent(this, MascotaAddEditActivity::class.java)
                intent.putExtra("mascota_id", mascota.id)
                startActivity(intent)
            },
            onEliminarClick = { mascota ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar mascota")
                    .setMessage("¿Estás seguro de que deseas eliminar esta mascota?")
                    .setPositiveButton("Sí") { _, _ ->
                        viewModel.eliminarMascota(mascota.id)
                        Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        binding.recyclerMascotas.layoutManager = LinearLayoutManager(this)
        binding.recyclerMascotas.adapter = adapter

        viewModel.mascotas.observe(this) {
            adapter.actualizarLista(it)
        }

        binding.btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnAgregarMascota.setOnClickListener {
            startActivity(Intent(this, MascotaAddEditActivity::class.java))
        }

        viewModel.obtenerMascotas()
    }

    override fun onResume() {
        super.onResume()
        viewModel.obtenerMascotas()
    }
}