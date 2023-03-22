package cart.service.converter

import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton

@Singleton
class ObjectToJsonStringConverter(private val objectMapper: ObjectMapper) {

    fun objectToJson(any: Any): String {
        return objectMapper.writeValueAsString(any)
    }

}