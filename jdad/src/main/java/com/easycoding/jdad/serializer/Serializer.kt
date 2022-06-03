package com.easycoding.jdad.serializer

import com.easycoding.jdad.JsonExclude
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object Serializer {
    @JvmStatic
    fun serialize(obj: Any): String = buildString { serializeObject(obj) }

    @JvmStatic
    private fun StringBuilder.serializeObject(obj: Any) {
        val kClass: KClass<Any> = obj.javaClass.kotlin
        val memberProperties = kClass.memberProperties

        memberProperties.filter { prop -> prop.findAnnotation<JsonExclude>() == null }

        memberProperties.joinToString(
            separator = ", ",
            prefix = "{",
            postfix = "}"
        ) { property ->
            append(serializeString(property.name))
            append(": ")
            append(serializePropertyValue(obj))
        }
    }

    private fun serializeString(name: String): String = "\"$name\""

    private fun serializePropertyValue(obj: Any): String = "\"$obj\""
}