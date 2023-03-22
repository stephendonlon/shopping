package cart.service.model

import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import cart.service.model.product.Product
import io.micronaut.serde.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

class CartTest {

    private val objectMapper = ObjectMapper.getDefault()

    private fun createPayloadString(product: Product, quantity: Int): String {
        val cartItem = CartItem(product, quantity)
        return objectMapper.writeValueAsString(cartItem)
    }

    @Test
    fun `should create cart with matching cart ids`() {
        val cartEvents = listOf(
            CartEvent(
                1L,
                "cart1",
                createPayloadString(Product("product1", "Product 1", BigDecimal(10)), 2),
                LocalDateTime.now(),
                EventType.ADD
            ),
            CartEvent(
                2L,
                "cart1",
                createPayloadString(Product("product2", "Product 2", BigDecimal(5)), 1),
                LocalDateTime.now(),
                EventType.ADD
            )
        )

        val cart = Cart(cartEvents, objectMapper)

        assertEquals("cart1", cart.id)
        assertEquals(2, cart.items.size)
        assertEquals(BigDecimal(25), cart.total)
    }

    @Test
    fun `should throw exception when cart ids do not match`() {
        val cartEvents = listOf(
            CartEvent(
                1L,
                "cart1",
                createPayloadString(Product("product1", "Product 1", BigDecimal(10)), 2),
                LocalDateTime.now(),
                EventType.ADD
            ),
            CartEvent(
                2L,
                "cart2",
                createPayloadString(Product("product2", "Product 2", BigDecimal(5)), 1),
                LocalDateTime.now(),
                EventType.ADD
            )
        )

        assertThrows<IllegalArgumentException>("All cart events must have the same cartId") {
            Cart(cartEvents, objectMapper)
        }
    }

    @Test
    fun `should handle empty cart events`() {
        val cartEvents = emptyList<CartEvent>()

        val cart = Cart(cartEvents, objectMapper)

        assertEquals("", cart.id)
        assertEquals(0, cart.items.size)
        assertEquals(BigDecimal.ZERO, cart.total)
    }
}