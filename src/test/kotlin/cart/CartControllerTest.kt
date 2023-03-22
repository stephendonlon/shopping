package cart

import cart.service.model.command.AddToCartCommand
import cart.service.model.product.Product
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.IsNull.notNullValue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@MicronautTest
class CartControllerTest {

    @Test
    fun `test createCart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val addToCartCommand =
            objectMapper.writeValueAsString(AddToCartCommand(Product("1", "product1", BigDecimal.TEN), 1))

        spec
            .contentType("application/json")
            .body(addToCartCommand)
            .`when`()
            .post("/carts")
            .then()
            .statusCode(200)
            .body("cartId", notNullValue())
    }

    @Test
    fun `test addToCart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val cartId = "testCartId2"
        val addToCartCommand =
            objectMapper.writeValueAsString(AddToCartCommand(Product("1", "product1", BigDecimal.TEN), 1))

        spec
            .contentType("application/json")
            .body(addToCartCommand)
            .`when`()
            .post("/carts/$cartId")
            .then()
            .statusCode(200)
    }

    @Test
    fun `test getCart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val cartId = "testCartId3"
        val addToCartCommand =
            objectMapper.writeValueAsString(AddToCartCommand(Product("1", "product1", BigDecimal.TEN), 1))

        spec
            .contentType("application/json")
            .body(addToCartCommand)
            .`when`()
            .post("/carts/$cartId")
            .then()
            .statusCode(200)

        spec
            .`when`()
            .get("/carts/$cartId")
            .then()
            .statusCode(200)
            .body("id", equalTo(cartId))
            .body("items[0].product.id", equalTo("1"))
            .body("items[0].quantity", equalTo(1))
    }

}