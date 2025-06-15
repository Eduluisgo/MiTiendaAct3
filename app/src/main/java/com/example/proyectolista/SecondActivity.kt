package com.example.proyectolista

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectolista.adapter.AdapterCarritodecompras
import com.example.proyectolista.databinding.ActivitySecondBinding
import com.example.proyectolista.model.Producto
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var adapterCompras: AdapterCarritodecompras //adaptador
    private var carrito = ArrayList<Producto>() // lista recibida del main para el recycler del secondActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //configuración del recyclerview para mostra los productos seleccionado
        binding.recyclerCarritoCompra.layoutManager = LinearLayoutManager(this)
        //recupera los productos seleccionados
        carrito = intent.getSerializableExtra("carrito") as? ArrayList<Producto> ?: ArrayList()
        //configura el adapter al recycler
        adapterCompras = AdapterCarritodecompras(carrito)
        binding.recyclerCarritoCompra.adapter = adapterCompras

        //Actualiza la compra
        actualizarTotal()
        //configura el Toolbar
        instancias()
        //recupera el producta y actualiza la vista
        recuperarProductos()
    }
    private fun instancias() {
        //configuración menu
        setSupportActionBar(binding.menuSecond)
    }
private fun actualizarTotal() {
    //va sumando el precio de los productos que se van agregando
    val total = carrito.sumOf { it.precio }
    //Va mostranto el total en textView
    binding.totalComprar.text = "Total: $" + String.format("%.2f", total)
}

    @SuppressLint("NotifyDataSetChanged")
    private fun recuperarProductos() {
            if (carrito.isNotEmpty()) {
                //si hay producto actualiza el recyclerview
                adapterCompras.notifyDataSetChanged()
                //calcula la compra
                calculoCompras()
            } else {
                //Avisa cuando está vacio
                Snackbar.make(binding.root,"El carrito de compras está vacío", Snackbar.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("DefaultLocale", "SetTextI18n")
    //calcula el total y lo muestra en formato moneda
    private fun calculoCompras() {
        val total = carrito.sumOf { it.precio }
        binding.totalComprar.text = "Total:  \$${String.format(Locale.getDefault(), "%.2f", total)}"
    }

    //Infla el mnú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return true
    }

        //Maneja las acciones del item que se selecciona.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuConfirmarCompra ->{
                if(carrito.isNotEmpty()){
                    //si no está vacio confirma la compra
                    val total = carrito.sumOf { it.precio }
                    Snackbar.make(binding.root, "Enhorabuena, compra por valor de ${String.format(
                        Locale.getDefault(),"%.2f", total)} $ realizada", Snackbar.LENGTH_SHORT).show()
                   val intent = Intent() //Crea un intent para notificar al Main que la compra ha sido realizada
                    intent.putExtra("compraRealizada", true) // Envía la señal de que la compra ha sido confirmada -> llega al onResume
                    carrito.clear() //limpia
                    calculoCompras() //actualiza el total
                    setResult(RESULT_OK)// devuelve el resultado al main
                }else{
                    //Avisa que el carrito está vacio
                    Snackbar.make(binding.root, "Carrito vacío", Snackbar.LENGTH_SHORT).show()
                }
            }
            R.id.vaciarCarrito -> {
                if(carrito.isNotEmpty()){
                    val intent = Intent()//Crea un intent para notificar al Main que la compra ha sido realizada
                    intent.putExtra("compraRealizada", true) // Envía la señal de que la compra ha sido confirmada -> llega al onResume
                    carrito.clear()
                    calculoCompras()
                    Snackbar.make(binding.root, "Carrito vacío", Snackbar.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                }else{
                    Snackbar.make(binding.root, "El carrita ya está vacío", Snackbar.LENGTH_SHORT).show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }
}