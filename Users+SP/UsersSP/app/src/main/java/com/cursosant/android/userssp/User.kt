package com.cursosant.android.userssp

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin.classes
 * Created by Alain Nicolás Tello on 10/11/20 at 13:17
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/

data class User (val id: Long, var name: String, var lastName: String, var url: String) { //Url para poder visualizar la foto.
   
    fun getFullName(): String = "$name $lastName" //Devolver nombre y apellido del usuario.
}