package cart.controller

import cart.service.CartService
import cart.service.model.Cart
import cart.service.model.command.AddToCartCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import jakarta.inject.Inject
import java.util.*

@Controller("/carts")
class CartController(@Inject private val cartService: CartService) {

    @Post(consumes = [MediaType.APPLICATION_JSON], produces = [MediaType.APPLICATION_JSON])
    fun createCart(@Body addToCartCommand: AddToCartCommand): HttpResponse<Map<String, String>> {

        val cartEvent = cartService.addToNewCart(addToCartCommand)

        return HttpResponse.ok(mapOf("cartId" to cartEvent.cartId))
    }

    @Post("/{id}")
    fun addToCart(
        @PathVariable(name = "id") id: String,
        @Body addToCartCommand: AddToCartCommand
    ): HttpResponse<String> {

        cartService.addToCart(addToCartCommand, id)

        return HttpResponse.ok()
    }

    @Get("/{id}")
    fun getCart(@PathVariable(name = "id") id: String): HttpResponse<Cart> {

        return HttpResponse.ok(cartService.getCart(id))
    }

}