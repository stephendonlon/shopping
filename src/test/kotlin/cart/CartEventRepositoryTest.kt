package cart.repository

import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

@MicronautTest
class CartEventRepositoryTest {

    @Inject
    lateinit var cartEventRepository: CartEventRepository

    private lateinit var savedCartEvent: CartEvent

    @BeforeEach
    fun setUp() {
        val cartEvent = CartEvent(
            eventType = EventType.ADD,
            cartId = "1",
            payload = "{item: '1', quantity: 2}",
            createdTime = LocalDateTime.now()
        )
        savedCartEvent = cartEventRepository.save(cartEvent)
    }

    @AfterEach
    fun tearDown() {
        cartEventRepository.deleteById(savedCartEvent.id!!)
    }

    @Test
    fun `test save CartEvent`() {
        assertNotNull(savedCartEvent.id)
    }

    @Test
    fun `test find CartEvent by ID`() {
        val foundCartEvent = cartEventRepository.findById(savedCartEvent.id!!)
        assertTrue(foundCartEvent.isPresent)
        assertEquals(savedCartEvent.id, foundCartEvent.get().id)
    }

    @Test
    fun `test fetch CartEvents using pagination`() {
        val pageable: Pageable = Pageable.from(0, 10)
        val cartEvents: Page<CartEvent> = cartEventRepository.findAll(pageable)
        assertFalse(cartEvents.isEmpty)
    }

    @Test
    fun `test delete CartEvent`() {
        cartEventRepository.deleteById(savedCartEvent.id!!)
        val deletedCartEvent = cartEventRepository.findById(savedCartEvent.id!!)
        assertFalse(deletedCartEvent.isPresent)
    }
}
