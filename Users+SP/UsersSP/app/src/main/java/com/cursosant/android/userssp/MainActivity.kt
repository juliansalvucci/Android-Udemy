package com.cursosant.android.userssp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursosant.android.userssp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), OnClickListener { //Implementa interfaces

    private lateinit var userAdapter: UserAdapter //Instancia del adaptador
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager //para que sea de una sola columna.

    private lateinit var binding: ActivityMainBinding //variable para vincular la vista del recyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)  //Inicialización de bindigng
        setContentView(binding.root)

        val preferences = getPreferences(Context.MODE_PRIVATE) //Almacenamiento interno, no sirve para grandes volumenes de datos.

        val isFirstTime = preferences.getBoolean(getString(R.string.sp_first_time), true) //imgresar clave string no nula, con valor por defecto true.
        Log.i("SP", "${getString(R.string.sp_first_time)} = $isFirstTime")  //Impresión.

        //Si ingresa por primera vez, le solicita el nombre para registrar
        if (isFirstTime) {
            val dialgoView = layoutInflater.inflate(R.layout.dialog_register, null) //Inflar vista mat dialog
            MaterialAlertDialogBuilder(this)  //configuración de mat dialog
                    .setTitle(R.string.dialog_title)
                    .setView(dialgoView)
                    .setCancelable(false) //Dialog solo se va al presionar confirmar.
                    .setPositiveButton(R.string.dialog_confirm, { dialogInterface, i ->
                        val username = dialgoView.findViewById<TextInputEditText>(R.id.etUsername)
                                .text.toString()
                        with(preferences.edit()){
                            putBoolean(getString(R.string.sp_first_time), false)
                            putString(getString(R.string.sp_username), username)
                                    .apply()
                        }
                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT) //Mensaje de registro exitoso
                                .show()
                    })
                    .show()

        } else {
            //Sino que de la bienvenida
            val username = preferences.getString(getString(R.string.sp_username), getString(R.string.hint_username)) //Toma el valor del input layout
            Toast.makeText(this, "Bienvenido $username", Toast.LENGTH_SHORT).show()
        }

        userAdapter = UserAdapter(getUsers(), this) //adaptador que recibe objetos de tipo usuario.
        linearLayoutManager = LinearLayoutManager(this) //thos refiere al contexto.

        binding.recyclerView.apply {
            setHasFixedSize(true)  //Indico que el listado es fijo con los cual se optimiza la app
            layoutManager = linearLayoutManager
            adapter = userAdapter
        }
    }

    private fun getUsers(): MutableList<User>{  //Método para obtener usuarios.
        val users = mutableListOf<User>() //Definición de lista de usuarios

        //Definir usuarios hardcodeados.
        val alain = User(1, "Alain", "Nicolás", "https://frogames.es/wp-content/uploads/2020/09/alain-1.jpg")
        val samanta = User(2, "Samanta", "Meza", "https://upload.wikimedia.org/wikipedia/commons/b/b2/Samanta_villar.jpg")
        val javier = User(3, "Javier", "Gómez", "https://live.staticflickr.com/974/42098804942_b9ce35b1c8_b.jpg")
        val emma = User(4, "Emma", "Cruz", "https://upload.wikimedia.org/wikipedia/commons/d/d9/Emma_Wortelboer_%282018%29.jpg")

        users.add(alain)  //Agregar objeto al arreglo. Se duplicaron para probar el scroll
        users.add(samanta)
        users.add(javier)
        users.add(emma)
        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)
        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)
        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)

        return users
    }

    override fun onClick(user: User, position: Int) { //Cuando haga click muestre el nombre del usuario.
        Toast.makeText(this, "$position: ${user.getFullName()}" , Toast.LENGTH_SHORT).show()
    }
}