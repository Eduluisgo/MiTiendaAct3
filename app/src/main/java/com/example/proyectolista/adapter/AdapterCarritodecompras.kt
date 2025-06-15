package com.example.proyectolista.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proyectolista.R
import com.example.proyectolista.model.Producto
import java.util.Locale

class AdapterCarritodecompras(private var listaCompras: ArrayList<Producto>): RecyclerView.Adapter<AdapterCarritodecompras.Myholder2>() {

    inner class Myholder2(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagenCompra: ImageView = itemView.findViewById(R.id.imagenProductoSecond)
        val nombreCompra: TextView = itemView.findViewById(R.id.nombreProductoSecond)
        val precioCompra: TextView = itemView.findViewById(R.id.precioProductoSecond)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder2{
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_carritodecompras, parent, false)
        return Myholder2(vista)
    }

    override fun getItemCount(): Int {
        return listaCompras.size
    }

    override fun onBindViewHolder(holder: Myholder2, position: Int) {
        val producto = listaCompras[position]
        holder.nombreCompra.text = producto.nombre
        val formatoCop = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        formatoCop.maximumFractionDigits = 0 // Sin decimales
        holder.precioCompra.text = formatoCop.format(producto.precio)

        //el glide carga la imagen
        Glide.with(holder.itemView.context)
            .load(producto.imagen) //carga la url
            .diskCacheStrategy(DiskCacheStrategy.ALL) //almacená en caché
            .placeholder(R.drawable.productcompra) // <---Imagen predeterminada
            .error(R.drawable.productcompra) // Imagen predeterminada si ocurre un error
            .into(holder.imagenCompra)// asocia la imagen al imageView
    }
}