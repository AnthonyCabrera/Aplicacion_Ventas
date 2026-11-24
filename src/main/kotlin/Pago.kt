import java.math.BigDecimal
import java.time.LocalDateTime

data class Pago(
    val id: Long,
    val fecha: LocalDateTime,
    val monto: BigDecimal
) {
    init {
        require(monto > BigDecimal.ZERO) { "El monto del pago debe ser mayor que cero." }
    }
}
