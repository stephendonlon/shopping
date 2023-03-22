package cart.service.model

import cart.service.model.product.Product
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CartItem(val product: Product, val quantity: Int) {

}
