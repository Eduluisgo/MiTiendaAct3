package com.example.proyectolista.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectolista.R
import com.example.proyectolista.model.Producto

class AdapterProducto(
    var listaProducto: ArrayList<Producto>,
    var contexto: Context,
    val listener: (Producto) -> Unit
) : RecyclerView.Adapter<AdapterProducto.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenProducto: ImageView = itemView.findViewById(R.id.imagenProductoMain)
        val nombreProducto: TextView = itemView.findViewById(R.id.nombreProductoMain)
        val precioProducto: TextView = itemView.findViewById(R.id.precioProductoMain)
        val btnAgregarProducto: Button = itemView.findViewById(R.id.btnAgregarProductoMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_producto, parent, false)
        return MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaProducto.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaProducto[position]

        holder.nombreProducto.text = producto.nombre
        holder.precioProducto.text = "$${producto.precio}"

        // Carga la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(producto.imagen)  // debe ser una URL válida (String)
            .into(holder.imagenProducto)

        // Acción del botón
        holder.btnAgregarProducto.setOnClickListener {
            listener(producto)
        }
    }

    interface OnProductoClickOnListener {
        fun agregarProductosAlCarrito(producto: Producto)
    }
}
