package com.example.proyectolista

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.proyectolista.adapter.AdapterProducto
import com.example.proyectolista.databinding.ActivityMainBinding
import com.example.proyectolista.model.Producto
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), AdapterProducto.OnProductoClickOnListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterProducto: AdapterProducto
    private lateinit var listaProductos: ArrayList<Producto>
    private lateinit var carrito: ArrayList<Producto>
    private lateinit var categorias: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        instancias()
        categoriasSpinner()
    }

    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("compraRealizada", true)) {
            carrito.clear()
            adapterProducto.notifyDataSetChanged()
            intent.removeExtra("compraRealizada")
        }
    }

    private fun instancias() {
        setSupportActionBar(binding.toolbar)
        listaProductos = ArrayList()
        carrito = ArrayList()
        categorias = ArrayList()

        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)
        adapterProducto = AdapterProducto(listaProductos, this) { producto ->
            agregarProductosAlCarrito(producto)
        }
        binding.recyclerProductos.adapter = adapterProducto
    }

    override fun agregarProductosAlCarrito(producto: Producto) {
        carrito.add(producto)
        Snackbar.make(binding.root, "${producto.nombre} agregado al carrito", Snackbar.LENGTH_SHORT).show()
    }

    private fun cargarProductos(categoria: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://fakestoreapi.com/products/category/$categoria"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            listaProductos.clear()
            for (i in 0 until response.length()) {
                val item = response.getJSONObject(i)
                val producto = Producto(
                    item.getInt("id"),
                    item.getString("title"),
                    item.getDouble("price"),
                    item.getString("image")
                )
                listaProductos.add(producto)
            }
            adapterProducto.notifyDataSetChanged()
        }, {
            Log.e("productos", "Error al obtener productos: ${it.message}")
        })
        queue.add(request)
    }

    private fun categoriasSpinner() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://fakestoreapi.com/products/categories"
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            categorias.clear()
            for (i in 0 until response.length()) {
                categorias.add(response.getString(i))
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategoria.adapter = adapter

            binding.spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val categoriaSeleccionada = categorias[position]
                    cargarProductos(categoriaSeleccionada)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }, {
            Log.e("categorias", "Error al obtener categorías: ${it.message}")
        })
        queue.add(request)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCarrito -> {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("carrito", ArrayList(carrito))
                startActivity(intent)
                return true
            }
            R.id.menuSalir -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
