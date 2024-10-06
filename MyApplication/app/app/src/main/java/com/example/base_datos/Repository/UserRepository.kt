package com.example.base_datos.Repository

import androidx.room.Dao
import com.example.base_datos.DAO.UserDAO
import com.example.base_datos.Model.User

class UserRepository(private val userDao: UserDAO) {



    suspend fun insert(user:User){
        userDao.insert(user)
    }
    suspend fun getAllUser(): List<User>{
        return  userDao.getAllUsers()
    }

    suspend fun deleteById(userId: Int): Int{
        return userDao.deleteById(userId)
    }

    suspend fun delete(user: User){
        userDao.delete(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }
}

