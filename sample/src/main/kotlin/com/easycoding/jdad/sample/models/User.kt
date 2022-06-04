package com.easycoding.jdad.sample.models

import com.easycoding.jdad.JsonExclude
import com.easycoding.jdad.JsonExcludeNulls
import com.easycoding.jdad.JsonName

@JsonExcludeNulls
data class User(
    @JsonName("name") val firstName: String,
    @JsonName("surname") val lastName: String,
    val age: Int,
    @JsonExclude val isAdult: Boolean,
    val address: UserAddress,
    val userEmails: List<UserEmail>,
    val token: Double? = null,
    val address2: UserAddress? = null
)
