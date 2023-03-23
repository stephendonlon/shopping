package cart.controller

import cart.service.CartService
import cart.service.model.Cart
import cart.service.model.command.CartCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import jakarta.inject.Inject
import java.util.*

@Controller("/carts")
class CartController(@Inject private val cartService: CartService) {

    @Post(consumes = [MediaType.APPLICATION_JSON], produces = [MediaType.APPLICATION_JSON])
    fun createCart(@Body cartCommand: CartCommand): HttpResponse<Map<String, String>> {

        val cartEvent = cartService.addToNewCart(cartCommand)

        return HttpResponse.ok(mapOf("cartId" to cartEvent.cartId))
    }

    @Post("/{id}")
    fun updateCart(
        @PathVariable(name = "id") id: String,
        @Body cartCommand: CartCommand
    ): HttpResponse<String> {

        cartService.update(cartCommand, id)

        return HttpResponse.ok()
    }

    @Get("/{id}")
    fun getCart(@PathVariable(name = "id") id: String): HttpResponse<Cart> {

        return HttpResponse.ok(cartService.getCart(id))
    }
}