package com.markoid.parky.core.presentation.serializers

import com.google.gson.* // ktlint-disable no-wildcard-imports
import org.joda.time.DateTime
import java.lang.reflect.Type

class DateTimeSerializer : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    override fun serialize(
        src: DateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DateTime? {
        val jsonString = json.asJsonPrimitive.asString
        return if (jsonString.isNotEmpty()) DateTime(jsonString)
        else null
    }
}
