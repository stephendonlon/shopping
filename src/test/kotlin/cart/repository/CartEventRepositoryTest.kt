package cart.repository

import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
class CartEventRepositoryTest {

    @Inject
    lateinit var cartEventRepository: CartEventRepository

    private lateinit var savedCartEvent: CartEvent

    private lateinit var savedCartEvent2: CartEvent

    @BeforeEach
    fun setUp() {
        val cartEvent = CartEvent(
            eventType = EventType.ADD,
            cartId = "1",
            payload = "{item: '1', quantity: 2}",
            createdTime = LocalDateTime.now()
        )

        val cartEvent2 = CartEvent(
            eventType = EventType.ADD,
            cartId = "1",
            payload = "{item: '1', quantity: 2}",
            createdTime = LocalDateTime.now().plusDays(1)
        )

        savedCartEvent = cartEventRepository.save(cartEvent)
        savedCartEvent2 = cartEventRepository.save(cartEvent2)
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

    @Test
    fun `test find CartEvents by cartId`() {
        val cartEvents = cartEventRepository.findByCartIdOrderByCreatedTimeAsc(savedCartEvent.cartId)
        assertFalse(cartEvents.isEmpty())
        assertEquals(2, cartEvents.size)
        assertEquals(savedCartEvent.id, cartEvents[0].id)
        assertEquals(savedCartEvent2.id, cartEvents[1].id)
        assertTrue(cartEvents[0].createdTime.isBefore(cartEvents[1].createdTime))
    }
}
