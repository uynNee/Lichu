package edu.uit.o21.lichu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "name") val name: String
)