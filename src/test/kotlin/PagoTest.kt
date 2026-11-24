import java.math.BigDecimal
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PagoTest {
    private val fecha = LocalDateTime.of(2026, 11, 17, 10, 0)

    @Test
    fun aceptaUnPagoDeUnCentavo() {
        val pago = Pago(1L, fecha, BigDecimal("0.01"))
        assertEquals(0, BigDecimal("0.01").compareTo(pago.monto))
        assertEquals(fecha, pago.fecha)
    }

    @Test
    fun rechazaMontoCeroONegativo() {
        for (monto in listOf(BigDecimal.ZERO, BigDecimal("-1"))) {
            assertThrows(IllegalArgumentException::class.java) {
                Pago(1L, fecha, monto)
            }
        }
    }
}
