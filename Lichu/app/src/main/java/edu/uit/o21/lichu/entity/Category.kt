package edu.uit.o21.lichu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int,
    @ColumnInfo(name = "name") val name: String
)