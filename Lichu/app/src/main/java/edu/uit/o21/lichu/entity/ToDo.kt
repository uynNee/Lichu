package edu.uit.o21.lichu.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = ["categoryId"],
    childColumns = ["categoryId"],
    onDelete = ForeignKey.CASCADE
)])
data class ToDo(
    @PrimaryKey(autoGenerate = true) val todoId: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "isDone") val isDone: Boolean,
    @ColumnInfo(name = "startTime") val startTime: String,
    @ColumnInfo(name = "endTime") val endTime: String,
    @ColumnInfo(name = "categoryId") val categoryId: Int
)