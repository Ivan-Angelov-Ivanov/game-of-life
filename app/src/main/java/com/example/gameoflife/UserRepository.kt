package com.example.gameoflife

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.gameoflife.api.RetrofitInstance
import com.example.gameoflife.roomdb.User
import com.example.gameoflife.roomdb.UserDao
import java.util.*

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class UserRepository(private val userDao: UserDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allUsers: LiveData<List<User>> = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insertAll(user)
    }

    suspend fun postUser(user: User) {
        RetrofitInstance.api.addUser(user)
    }
}