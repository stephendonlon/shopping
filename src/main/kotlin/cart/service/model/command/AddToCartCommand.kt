package cart.service.model.command

import cart.service.model.product.Product
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
data class AddToCartCommand(var product: Product, var quantity: Int)
