package com.lazytravel.helpers

import com.lazytravel.data.base.BaseModel
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

inline fun <reified T : Any> T.toJson(): JsonObject {
    val json = Json { encodeDefaults = false }
    val element = json.encodeToJsonElement(serializer<T>(), this)
    return element.jsonObject.filterNotEmptyStrings()
}

inline fun <reified T : Any> List<T>.toJsonList(): List<JsonObject> {
    return map { it.toJson() }
}

fun JsonObject.filterNotEmptyStrings(): JsonObject {
    return JsonObject(
        filterNot { (_, value) ->
            value is JsonPrimitive && value.isString && value.content.isEmpty()
        }
    )
}

fun jsonObject(block: MutableMap<String, JsonElement>.() -> Unit): JsonObject {
    return JsonObject(buildMap(block))
}

fun MutableMap<String, JsonElement>.put(key: String, value: String) {
    this[key] = JsonPrimitive(value)
}

fun MutableMap<String, JsonElement>.put(key: String, value: Number) {
    this[key] = JsonPrimitive(value)
}

fun MutableMap<String, JsonElement>.put(key: String, value: Boolean) {
    this[key] = JsonPrimitive(value)
}

fun MutableMap<String, JsonElement>.putIfNotEmpty(key: String, value: String) {
    if (value.isNotEmpty()) this[key] = JsonPrimitive(value)
}