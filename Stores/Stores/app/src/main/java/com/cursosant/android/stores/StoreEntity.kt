package com.cursosant.android.stores

import androidx.room.Entity
import androidx.room.PrimaryKey

/****
 * Project: Stores
 * From: com.cursosant.android.stores
 * Created by Alain Nicolás Tello on 26/11/20 at 13:19
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
@Entity(tableName = "StoreEntity") //señalo la clase como tabla en bd
data class StoreEntity(@PrimaryKey(autoGenerate = true) var id: Long = 0, //configuración de llave primaria, en ste caso es automática
                       var name: String,
                       var phone: String,
                       var website: String = "",
                       var photoUrl: String,
                       var isFavorite: Boolean = false){

    override fun equals(other: Any?): Boolean { //Para evitar que el id ya exista
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int { //Para generar id
        return id.hashCode()
    }
}
