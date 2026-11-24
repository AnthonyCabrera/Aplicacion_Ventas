import java.math.BigDecimal

data class Cliente(
    val id: Long,
    val nombre: String,
    val telefono: String,
    val saldo: BigDecimal = BigDecimal.ZERO
)


