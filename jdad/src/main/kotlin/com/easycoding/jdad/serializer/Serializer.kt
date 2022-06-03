package com.easycoding.jdad.serializer

import com.easycoding.jdad.JsonExclude
import java.io.File.separator
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object Serializer {
    @JvmStatic
    fun serialize(obj: Any): String = buildString { serializeObject(obj) }

    @JvmStatic
    private fun StringBuilder.serializeObject(obj: Any?) {
        if (obj == null)
            return

        val kClass: KClass<Any> = obj.javaClass.kotlin
        val memberProperties = kClass.memberProperties

        memberProperties.filter { prop -> prop.findAnnotation<JsonExclude>() == null }

        val joined = memberProperties.joinToString(
            separator = ", ",
            prefix = "{",
            postfix = "}"
        ) { property ->
            "${serializeString(property.name)}: ${property.serializePropertyValue(obj)}"
        }

        append(joined)
    }

    private fun serializeString(name: String): String = "\"$name\""

    private fun KProperty1<Any, *>.serializePropertyValue(obj: Any): Any {
        return when (val res = this.get(obj)) {
            is String -> res
            is Boolean -> res
            is Int -> res
            else -> buildString { serializeObject(res) }
        }
    }
}