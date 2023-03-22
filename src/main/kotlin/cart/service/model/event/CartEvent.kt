package cart.service.model.event

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
@MappedEntity
data class CartEvent(
    @field: Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    val cartId: String,
    val payload: String,
    val createdTime: LocalDateTime,
    val eventType: EventType
)