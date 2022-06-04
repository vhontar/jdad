package com.easycoding.jdad.serializer

import com.easycoding.jdad.JsonExclude
import com.easycoding.jdad.JsonExcludeNulls
import com.easycoding.jdad.JsonName
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object Serializer {
    @JvmStatic
    fun serialize(obj: Any): String = buildString { serializeObject(obj) }

    private fun StringBuilder.serializeObject(obj: Any?) {
        // TODO remove check for null
        if (obj == null) {
            serializePropertyValue(obj)
            return
        }

        val kClass: KClass<Any> = obj.javaClass.kotlin

        // should omit serialization of null values
        val omitNullValues: Boolean = kClass.annotations.filterIsInstance<JsonExcludeNulls>().firstOrNull() != null

        // find properties of an object
        val memberProperties = kClass.memberProperties
            .filter { prop -> prop.findAnnotation<JsonExclude>() == null } // exclude properties

        joinToStringBuilder(
            prefix = "{",
            postfix = "}"
        ) {
            memberProperties.forEachIndexed { index, property ->
                val value = property.get(obj)

                if (value == null && omitNullValues) {
                    return@forEachIndexed
                }

                val propertyName = property.findAnnotation<JsonName>()?.name ?: property.name
                serializeProperty(propertyName, value, index, memberProperties.size)
            }
        }
    }

    private fun StringBuilder.serializeProperty(
        propertyName: String,
        value: Any?,
        index: Int,
        size: Int
    ) {
        serializeString(propertyName)
        append(": ")
        serializePropertyValue(value)
        appendComma(index, size)
    }

    private fun StringBuilder.serializePropertyValue(value: Any?) {
        when (value) {
            null -> append("null")
            is String -> serializeString(value)
            is Number, is Boolean -> append(value.toString())
            is List<*> -> serializeList(value)
            else -> serializeObject(value)
        }
    }

    private fun StringBuilder.serializeList(list: List<*>) = joinToStringBuilder(
        prefix = "[",
        postfix = "]"
    ) {
        list.forEachIndexed { index, item ->
            serializeObject(item)
            appendComma(index, list.size)
        }
    }

    private fun StringBuilder.serializeString(value: String) = append("\"$value\"")

    private fun StringBuilder.joinToStringBuilder(
        prefix: String,
        postfix: String,
        block: () -> Unit
    ) {
        append(prefix)
        block.invoke()
        append(postfix)
    }

    // TODO refactor append comma method
    @Deprecated("not so sure if it's the best method to do that", ReplaceWith("if (index < size - 1) append(\", \")"))
    private fun StringBuilder.appendComma(
        index: Int,
        size: Int
    ) {
        if (index < size - 1)
            append(", ")
    }
}