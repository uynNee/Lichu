package edu.uit.o21.lichu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = ["id"],
    childColumns = ["categoryId"],
    onDelete = ForeignKey.CASCADE
)],
    indices = [Index("categoryId")]
)
data class ToDo(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "startTime") val startTime: LocalDate,
    @ColumnInfo(name = "endTime") val endTime: LocalDate,
    @ColumnInfo(name = "categoryId") val categoryId: Int
)