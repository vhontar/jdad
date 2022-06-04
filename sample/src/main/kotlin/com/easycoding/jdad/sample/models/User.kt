package com.easycoding.jdad.sample.models

import com.easycoding.jdad.*
import java.util.*

@JsonExcludeNulls
data class User(
    @JsonName("name") val firstName: String,
    @JsonName("surname") val lastName: String,
    val age: Int,
    @JsonExclude val isAdult: Boolean,
    val address: UserAddress,
    val userEmails: List<UserEmail>,
    @CustomSerializer(DateSerializer::class) val birthday: Date,
    val token: Double? = null,
    val address2: UserAddress? = null
)
