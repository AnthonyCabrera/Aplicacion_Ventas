import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ProductoTest {
    @Test
    fun calculaGananciaConCentavos() {
        val producto = Producto(1L, "Cuaderno", BigDecimal("20.10"), BigDecimal("35.25"))
        assertEquals(0, BigDecimal("15.15").compareTo(producto.calcularGananciaUnitaria()))
    }

    @Test
    fun permiteUnaVentaConPerdida() {
        val producto = Producto(1L, "Cuaderno", BigDecimal("20"), BigDecimal("15"))
        assertEquals(0, BigDecimal("-5").compareTo(producto.calcularGananciaUnitaria()))
    }

    @Test
    fun rechazaCostoNegativo() {
        assertThrows(IllegalArgumentException::class.java) {
            Producto(1L, "Cuaderno", BigDecimal("-1"), BigDecimal("15"))
        }
    }

    @Test
    fun rechazaPrecioNegativo() {
        assertThrows(IllegalArgumentException::class.java) {
            Producto(1L, "Cuaderno", BigDecimal("10"), BigDecimal("-1"))
        }
    }
}
