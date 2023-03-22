package cart.service.model

import cart.service.model.event.CartEvent
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

    private val itemsMap: Map<String, CartItem> = cartEvents.map { event ->
        objectMapper.readValue(event.payload, CartItem::class.java)
    }.groupBy { it?.product!!.id }
        .mapValues { (_, items) ->
            CartItem(items.first()!!.product, items.sumOf { it!!.quantity })
        }

    val items: Set<CartItem>
        get() = itemsMap.values.toSet()

    val total: BigDecimal
        get() = items.map { it.product.price.multiply(BigDecimal(it.quantity)) }
            .reduceOrNull { acc, itemTotal -> acc.add(itemTotal) } ?: BigDecimal.ZERO
}