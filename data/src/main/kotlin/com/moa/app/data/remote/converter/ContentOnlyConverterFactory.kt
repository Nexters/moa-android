package com.moa.app.data.remote.converter

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ContentOnlyConverterFactory(
    private val json: Json
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {

        val serializer = json.serializersModule.serializer(type)

        return Converter<ResponseBody, Any> { body ->
            val raw = body.string()

            val root = json.parseToJsonElement(raw).jsonObject

            val contentElement = root["content"]

            contentElement?.let {
                json.decodeFromJsonElement(serializer, contentElement)
            }
        }
    }
}



