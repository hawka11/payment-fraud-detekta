package detekta.payment.cypher

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun objectMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.registerModule(KotlinModule())
    return mapper
}
