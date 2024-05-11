package edu.uit.o21.lichu.ui.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(topEnd = 16.dp, bottomStart = 16.dp),
    large = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp, bottomEnd = 0.dp, bottomStart = 0.dp),
)