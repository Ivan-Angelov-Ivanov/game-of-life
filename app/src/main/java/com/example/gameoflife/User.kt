package com.example.gameoflife

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class User(
    @PrimaryKey
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") val password: String
)