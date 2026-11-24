import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DetalleVentaTest {
    private val producto = Producto(1L, "Cuaderno", BigDecimal("20.10"), BigDecimal("35.25"))

    @Test
    fun calculaSubtotalYGananciaParaVariasUnidades() {
        val detalle = DetalleVenta(1L, producto, 3)
        assertEquals(0, BigDecimal("105.75").compareTo(detalle.calcularSubtotal()))
        assertEquals(0, BigDecimal("45.45").compareTo(detalle.calcularGanancia()))
    }

    @Test
    fun usaLosImportesAcordadosEnElDetalle() {
        val detalle = DetalleVenta(1L, producto, 2, BigDecimal("30"), BigDecimal("18"))
        assertEquals(0, BigDecimal("60").compareTo(detalle.calcularSubtotal()))
        assertEquals(0, BigDecimal("24").compareTo(detalle.calcularGanancia()))
    }

    @Test
    fun rechazaCantidadCeroONegativa() {
        for (cantidad in listOf(0, -1)) {
            assertThrows(IllegalArgumentException::class.java) {
                DetalleVenta(1L, producto, cantidad)
            }
        }
    }

    @Test
    fun rechazaImportesUnitariosNegativos() {
        assertThrows(IllegalArgumentException::class.java) {
            DetalleVenta(1L, producto, 1, precioUnitario = BigDecimal("-1"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            DetalleVenta(1L, producto, 1, costoUnitario = BigDecimal("-1"))
        }
    }
}
