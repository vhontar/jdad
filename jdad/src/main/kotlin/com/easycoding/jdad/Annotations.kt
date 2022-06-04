package com.easycoding.jdad

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class JsonExcludeNulls

@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude

@Target(AnnotationTarget.PROPERTY)
annotation class JsonName(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class CustomSerializer(
    val serializerClass: KClass<out ValueSerializer<*>>
)