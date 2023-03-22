package cart.service

import cart.repository.CartEventRepository
import cart.service.converter.ObjectToJsonStringConverter
import cart.service.model.command.AddToCartCommand
import cart.service.model.Cart
import cart.service.model.event.CartEvent
import cart.service.model.event.EventType
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.*


interface CartService {
    fun addToNewCart(addToCartCommand: AddToCartCommand): CartEvent
    fun addToCart(addToCartCommand: AddToCartCommand, cartId: String): CartEvent
    fun getCart(cartId: String): Cart

}

@Singleton
class CartServiceImpl(
    private val objectToJsonStringConverter: ObjectToJsonStringConverter,
    private val cartEventRepository: CartEventRepository,
    private val objectMapper: ObjectMapper
) : CartService {

    override fun addToNewCart(addToCartCommand: AddToCartCommand): CartEvent {

        val cartId = UUID.randomUUID().toString();

        val commandAsString = objectToJsonStringConverter.objectToJson(addToCartCommand)

        return cartEventRepository.save(
            CartEvent(
                cartId = cartId,
                payload = commandAsString,
                createdTime = LocalDateTime.now(),
                eventType = EventType.ADD
            )
        )
    }

    override fun addToCart(addToCartCommand: AddToCartCommand, cartId: String): CartEvent {
        val commandAsString = objectToJsonStringConverter.objectToJson(addToCartCommand)
        return cartEventRepository.save(
            CartEvent(
                cartId = cartId,
                payload = commandAsString,
                createdTime = LocalDateTime.now(),
                eventType = EventType.ADD
            )
        )
    }

    override fun getCart(cartId: String): Cart {
        val cartEvents = cartEventRepository.findByCartId(cartId)
        return Cart(cartEvents, objectMapper)
    }

}