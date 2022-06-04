package com.easycoding.jdad

interface ValueSerializer<T> {
    fun toJsonValue(value: T): String
    fun fromJsonValue(value: String): T
}