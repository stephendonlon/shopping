package cart.service.model.command

import cart.service.model.event.EventType
import cart.service.model.product.Product
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
data class CartCommand(var product: Product, var quantity: Int, var command: EventType)
