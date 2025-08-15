package com.example.littlelemon

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


/*
Data retrieved from a remote server can be stored in a local SQLite database.
SQLite is a popular choice for storing data in Android applications
because it is a lightweight, self-contained database engine that does not require a separate server process.
It is also widely supported, with native bindings available, including Room.

Using Room in an Android app allows you to store and retrieve structured data in your device,
such as user information, application settings and other types of data that need to be persisted between sessions.
It also provides an easy way to query and manipulate data using SQL,
which is a powerful and widely-used language for working with databases.
The data in an app is stored as a database entity. One of the main advantages of using Room as persistent storage
is that it allows you to store data locally on the device.
This can be useful in cases where an internet connection is not available or when you want to reduce the amount of data
that gets transferred between the device and a remote server. To start using Room 3 you need to:

Create a Database abstract class to define the database

Create an Entity data class to represent the menu entity

Setup a Dao interface to access and manipulate database data

Overall, using Room is a convenient and efficient way to store and manage data in an Android application.
 */


//Una entidad es el concepto del mundo real que deseamos representar, en este caso el menu de un restaurante
//Mientras que el table es la implementacion de la entidad en una base de datos relacional en esta caso SQLite
//Representa una fila en la tabla menu_items.



@Entity
data class MenuItemEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String
)
@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<MenuItemEntity>) // SIN suspend por ahora

    @Query("SELECT * FROM MenuItemEntity") // Usar el nombre de la clase exacto
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>
}
@Database(entities = [MenuItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
}

/*Data Access Object, es un patron de diseño usado para abstraer y encapsular all access a la base de datps
El DAO es una interdaz que permite realizar operaciones basicas en SQL CRUD
 */
