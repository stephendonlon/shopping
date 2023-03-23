package cart.repository

import cart.service.model.event.CartEvent
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.MYSQL)
abstract class CartEventRepository : PageableRepository<CartEvent, Long> {

    abstract fun findByCartIdOrderByCreatedTimeAsc(cartId: String): List<CartEvent>
}