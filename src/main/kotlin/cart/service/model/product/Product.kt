package cart.service.model.product

import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal

@Serdeable
data class Product(val id: String, val name: String, val price: BigDecimal)
