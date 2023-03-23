package cart

import cart.service.model.command.CartCommand
import cart.service.model.event.EventType
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
        val cartCommand =
            objectMapper.writeValueAsString(
                CartCommand(
                    Product("1", "product1", BigDecimal.TEN),
                    1,
                    EventType.ADD
                )
            )

        spec
            .contentType("application/json")
            .body(cartCommand)
            .`when`()
            .post("/carts")
            .then()
            .statusCode(200)
            .body("cartId", notNullValue())
    }

    @Test
    fun `test addToCart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val cartId = "testCartId2"
        val cartCommand =
            objectMapper.writeValueAsString(
                CartCommand(
                    Product("1", "product1", BigDecimal.TEN),
                    1,
                    EventType.ADD
                )
            )

        spec
            .contentType("application/json")
            .body(cartCommand)
            .`when`()
            .post("/carts/$cartId")
            .then()
            .statusCode(200)
    }

    @Test
    fun `test getCart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val cartId = "testCartId3"
        val cartCommand =
            objectMapper.writeValueAsString(
                CartCommand(
                    Product("1", "product1", BigDecimal.TEN),
                    1,
                    EventType.ADD
                )
            )

        spec
            .contentType("application/json")
            .body(cartCommand)
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

    @Test
    fun `test remove item from cart`(spec: RequestSpecification, objectMapper: ObjectMapper) {
        val cartId = "testCartId4"
        val cartCommand =
            objectMapper.writeValueAsString(
                CartCommand(
                    Product("1", "product1", BigDecimal.TEN),
                    1,
                    EventType.ADD
                )
            )

        spec
            .contentType("application/json")
            .body(cartCommand)
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

        val removeCommand =
            objectMapper.writeValueAsString(
                CartCommand(
                    Product("1", "product1", BigDecimal.TEN),
                    1,
                    EventType.REMOVE
                )
            )

        spec
            .contentType("application/json")
            .body(removeCommand)
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
            .body("items", equalTo(null))
    }
}