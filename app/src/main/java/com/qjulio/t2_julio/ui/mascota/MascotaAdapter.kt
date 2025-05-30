package com.qjulio.t2_julio.ui.mascota

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qjulio.t2_julio.R
import com.qjulio.t2_julio.data.model.Mascota
import com.qjulio.t2_julio.databinding.ItemMascotaBinding

class MascotaAdapter(
    private var mascotas: List<Mascota>,
    private val onEditarClick: (Mascota) -> Unit,
    private val onEliminarClick: (Mascota) -> Unit
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    inner class MascotaViewHolder(val binding: ItemMascotaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val binding = ItemMascotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MascotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]
        holder.binding.tvNombre.text = mascota.name
        holder.binding.tvDescripcion.text = mascota.description
        holder.binding.tvPrecio.text = holder.itemView.context.getString(R.string.precio_formato, mascota.price)

        Glide.with(holder.itemView.context)
            .load(mascota.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.binding.imageMascota)

        holder.binding.btnEditar.setOnClickListener { onEditarClick(mascota) }
        holder.binding.btnEliminar.setOnClickListener { onEliminarClick(mascota) }
    }

    override fun getItemCount(): Int = mascotas.size

    fun actualizarLista(nuevaLista: List<Mascota>) {
        val diffCallback = MascotaDiffCallback(mascotas, nuevaLista)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mascotas = nuevaLista
        diffResult.dispatchUpdatesTo(this)
    }

    class MascotaDiffCallback(
        private val oldList: List<Mascota>,
        private val newList: List<Mascota>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
