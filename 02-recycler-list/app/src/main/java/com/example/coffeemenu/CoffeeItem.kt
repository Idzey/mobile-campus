package com.example.coffeemenu

import androidx.annotation.DrawableRes

data class CoffeeItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    @DrawableRes val icon: Int,
)
