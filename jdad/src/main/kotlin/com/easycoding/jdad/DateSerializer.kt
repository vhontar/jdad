package com.easycoding.jdad

import java.util.*

object DateSerializer: ValueSerializer<Date> {
    override fun toJsonValue(value: Date): String {
        // TODO make a better serialization
        return "${value.year}-${value.day}-${value.hours}-${value.minutes}"
    }

    override fun fromJsonValue(value: String): Date {
        TODO("Not yet implemented")
    }
}