package cart.service

import cart.repository.CartEventRepository
import cart.service.converter.ObjectToJsonStringConverter
import cart.service.model.command.CartCommand
import cart.service.model.Cart
import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.*


interface CartService {
    fun addToNewCart(cartCommand: CartCommand): CartEvent
    fun update(cartCommand: CartCommand, cartId: String): CartEvent
    fun getCart(cartId: String): Cart
}

@Singleton
class CartServiceImpl(
    private val objectToJsonStringConverter: ObjectToJsonStringConverter,
    private val cartEventRepository: CartEventRepository,
    private val objectMapper: ObjectMapper
) : CartService {

    override fun addToNewCart(cartCommand: CartCommand): CartEvent {

        val cartId = UUID.randomUUID().toString();

        val commandAsString = objectToJsonStringConverter.objectToJson(cartCommand)

        return cartEventRepository.save(
            CartEvent(
                cartId = cartId,
                payload = commandAsString,
                createdTime = LocalDateTime.now(),
                eventType = EventType.ADD
            )
        )
    }

    override fun update(cartCommand: CartCommand, cartId: String): CartEvent {
        val commandAsString = objectToJsonStringConverter.objectToJson(cartCommand)
        return cartEventRepository.save(
            CartEvent(
                cartId = cartId,
                payload = commandAsString,
                createdTime = LocalDateTime.now(),
                eventType = cartCommand.command
            )
        )
    }

    override fun getCart(cartId: String): Cart {
        val cartEvents = cartEventRepository.findByCartIdOrderByCreatedTimeAsc(cartId)
        return Cart(cartEvents, objectMapper)
    }

}