package com.cursosant.android.stores

import androidx.room.Database
import androidx.room.RoomDatabase

/****
 * Project: Stores
 * From: com.cursosant.android.stores
 * Created by Alain Nicolás Tello on 27/11/20 at 12:37
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
@Database(entities = arrayOf(StoreEntity::class), version = 2) //anotación room database, define las entidades como un arreglo y la unica que hay es la de stora, 
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}