package edu.uit.o21.lichu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "startTime") val startTime: Long,
    @ColumnInfo(name = "endTime") val endTime: Long,
    @ColumnInfo(name = "categoryId") val categoryId: Int
)

fun getFakeToDo():List<ToDo>{
    return listOf<ToDo>(
        ToDo(id=1,content="đây là test khi text dài vãi lìn he he he", isDone = false, startTime = 0, endTime = 10, categoryId = 1),
        ToDo(id=2,content="check true", isDone = false, startTime = 0, endTime = 10, categoryId = 1),
        ToDo(id=3,content="homework", isDone = false, startTime = 0, endTime = 10, categoryId = 1),
        ToDo(id=4,content="housework", isDone = false, startTime = 0, endTime = 10, categoryId = 1),
        ToDo(id=5,content="free time", isDone = false, startTime = 0, endTime = 10, categoryId = 1),
    )
}