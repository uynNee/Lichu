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
        Category(id=1, "Note 1 dài vãi lìn hehehehe he he h he"),
        Category(id=2,"Note 2"),
        Category(id=3,"Note 3"),
        Category(id=4,"Note 4")
    )
}