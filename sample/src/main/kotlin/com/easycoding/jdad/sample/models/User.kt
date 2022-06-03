package com.easycoding.jdad.sample.models

data class User(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val isAdult: Boolean,
    val address: UserAddress
)
