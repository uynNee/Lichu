package edu.uit.o21.lichu.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "name") val name: String
)

fun getFakeCategory():List<Category>{
    return listOf<Category>(
        Category(id=1, "Note1"),
        Category(id=2,"Note2"),
        Category(id=3,"Note3"),
        Category(id=4,"Note4")
    )
}