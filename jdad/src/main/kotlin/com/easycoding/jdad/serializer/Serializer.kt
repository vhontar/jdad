package com.easycoding.jdad.serializer

import com.easycoding.jdad.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object Serializer {

    @JvmStatic
    fun serialize(obj: Any): String = buildString { serializeObject(obj) }

    private fun StringBuilder.serializeObject(obj: Any) {

        val kClass: KClass<Any> = obj.javaClass.kotlin

        // must omit serialization of null values if annotation declared above class name
        val omitNullValues: Boolean = kClass.annotations.filterIsInstance<JsonExcludeNulls>().firstOrNull() != null

        // find properties of an object and exclude properties with JsonExclude annotation
        val memberProperties = kClass.memberProperties
            .filter { prop -> prop.findAnnotation<JsonExclude>() == null } // exclude properties

        joinToStringBuilder(
            prefix = "{",
            postfix = "}"
        ) {
            memberProperties.forEachIndexed { index, property ->

                // omit nulls if JsonExcludeNulls annotation is present above a class declaration
                if (property.get(obj) == null && omitNullValues) {
                    return@forEachIndexed
                }

                serializeProperty(
                    property.provideParameterName(),
                    property.provideParameterValue(obj),
                    index == memberProperties.size - 1
                )
            }
        }
    }

    private fun StringBuilder.serializeProperty(
        propertyName: String,
        value: Any?,
        isEnd: Boolean
    ) {
        serializeString(propertyName)
        appendColon()
        serializePropertyValue(value)
        appendComma(isEnd)
    }

    private fun StringBuilder.serializePropertyValue(value: Any?) {
        when (value) {
            null -> appendNull()
            is String -> serializeString(value)
            is Number, is Boolean -> append(value.toString())
            is List<*> -> serializeList(value)
            else -> serializeObject(value)
        }
    }

    // serialize list and pass the item further if it's not null
    private fun StringBuilder.serializeList(list: List<*>) = joinToStringBuilder(
        prefix = "[",
        postfix = "]"
    ) {
        list.forEachIndexed { index, item ->
            if (item == null) appendNull() else serializeObject(item)
            appendComma(index == list.size - 1)
        }
    }

    private fun StringBuilder.joinToStringBuilder(
        prefix: String,
        postfix: String,
        block: () -> Unit
    ) {
        append(prefix)
        block.invoke()
        append(postfix)
    }

    // find annotation if exists to get the right parameter name or get the default one
    private fun KProperty1<Any, *>.provideParameterName(): String {
        return findAnnotation<JsonName>()?.name ?: name
    }

    // find annotation for custom serializer if exists or get the default value
    private fun KProperty1<Any, *>.provideParameterValue(obj: Any): Any? =
        getSerializer()?.toJsonValue(get(obj)) ?: get(obj)

    // get serializer of a property if exists
    private fun KProperty1<Any, *>.getSerializer(): ValueSerializer<Any?>? {
        val customSerializerClass = findAnnotation<CustomSerializer>()?.serializerClass
        val customSerializer = customSerializerClass?.objectInstance ?: customSerializerClass?.createInstance()

        @Suppress("UNCHECKED_CAST")
        return customSerializer as ValueSerializer<Any?>?
    }

    // shorthand for appending string
    private fun StringBuilder.serializeString(value: String) = append("\"$value\"")

    // shorthand for appending comma to the end
    private fun StringBuilder.appendComma(isEnd: Boolean) {
        if (!isEnd) append(", ")
    }

    // shorthand for appending colon to the end
    private fun StringBuilder.appendColon() {
        append(": ")
    }

    // shorthand for appending null value
    private fun StringBuilder.appendNull() {
        append("null")
    }
}