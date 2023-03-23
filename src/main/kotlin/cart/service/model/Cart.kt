package cart.service.model

import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import io.micronaut.serde.ObjectMapper
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal


@Serdeable
class Cart(cartEvents: List<CartEvent>, private val objectMapper: ObjectMapper) {
    val id: String = cartEvents.firstOrNull()?.cartId ?: ""

    init {
        if (cartEvents.any { it.cartId != id }) {
            throw IllegalArgumentException("All cart events must have the same cartId")
        }
    }

    private val itemsMap: Map<String, CartItem> = cartEvents
        .fold(mutableMapOf()) { acc, event ->
            val cartItem = objectMapper.readValue(event.payload, CartItem::class.java)
            val productId = cartItem!!.product.id
            val currentQuantity = acc[productId]?.quantity ?: 0

            when (event.eventType) {
                EventType.ADD -> {
                    val updatedQuantity = currentQuantity + cartItem.quantity
                    acc[productId] = CartItem(cartItem.product, updatedQuantity)
                }
                EventType.REMOVE -> {
                    val updatedQuantity = currentQuantity - cartItem.quantity
                    if (updatedQuantity > 0) {
                        acc[productId] = CartItem(cartItem.product, updatedQuantity)
                    } else {
                        acc.remove(productId)
                    }
                }
            }
            acc
        }


    val items: Set<CartItem>
        get() = itemsMap.values.toSet()

    val total: BigDecimal
        get() = items.map { it.product.price.multiply(BigDecimal(it.quantity)) }
            .reduceOrNull { acc, itemTotal -> acc.add(itemTotal) } ?: BigDecimal.ZERO
}