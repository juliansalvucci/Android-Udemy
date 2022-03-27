package com.cursosant.android.userssp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosant.android.userssp.databinding.ItemUserAltBinding

/****
 * Project: Users SP
 * From: com.cursosant.android.userssp
 * Created by Alain Nicolás Tello on 20/11/20 at 11:58
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
class UserAdapter(private val users: List<User>, private val listener: OnClickListener) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){ //Herencia de clase personalisada
//OnClickListener para que escuche los eventos click

    private lateinit var context: Context //variable de tipo ontext, lateInit para inicializarla luego y no al arrancar.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { //Método para inflar la vista en xml.
        context = parent.context //Se inicia el contexto

        val view = LayoutInflater.from(context).inflate(R.layout.item_user_alt, parent, false) //Inflación de vista.

        return ViewHolder(view) //retornar el viewHolder y pasar la vista inflada
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users.get(position) //Forma similar a foreach para obtener los usuarios.

        with(holder){
            setListener(user, position+1)
            binding.tvOrder.text = (position + 1).toString()  //Rellenar el txtView tvOrder, pasar a string porque es numérico.
            binding.tvName.text = user.getFullName() //Obtener nombre y apellido del usuario.
            Glide.with(context) //glide para mostrar imagen.
                .load(user.url) //tomar la url del usuario la cual contiene la imagen
                .diskCacheStrategy(DiskCacheStrategy.ALL) //cache
                .centerCrop()
                .circleCrop() //mostrar bordes circulares.
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int = users.size //Obtener la cantidad de usuarios.

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){ //Clase interna, recibe una vista y hereda de RecyblerView
        val binding = ItemUserAltBinding.bind(view) //Habilitar view binding eb graddle scripts.

        fun setListener(user: User, position: Int){  //Listener que toma los eventos click
            binding.root.setOnClickListener { listener.onClick(user, position) }
        }
    }
}