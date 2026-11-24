import java.math.BigDecimal
import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class VentaTest {
    private val cliente = Cliente(1L, "Ana", "5551234567")
    private val fecha = LocalDateTime.of(2026, 11, 17, 10, 0)
    private val cuaderno = Producto(1L, "Cuaderno", BigDecimal("20"), BigDecimal("35"))
    private val lapiz = Producto(2L, "Lapiz", BigDecimal("2"), BigDecimal("5"))

    private fun nuevaVenta(): Venta = Venta(
        nombre = "Venta de Ana",
        fecha = fecha,
        cliente = cliente,
        detallesIniciales = listOf(DetalleVenta(1L, cuaderno, 2))
    )

    private fun assertImporte(esperado: String, actual: BigDecimal) {
        assertEquals("Importe esperado: $esperado; obtenido: $actual",
            0, BigDecimal(esperado).compareTo(actual))
    }

    @Test
    fun calculaTotalesAlAgregarOtroProducto() {
        val venta = nuevaVenta()
        venta.agregarProducto(lapiz, 3)
        assertImporte("85", venta.calcularTotal())
        assertImporte("39", venta.calcularGanancia())
        assertEquals(2, venta.detalles.map { it.id }.distinct().size)
    }

    @Test
    fun registraAbonoYLiquidaConUnSegundoPago() {
        val venta = nuevaVenta()
        assertFalse(venta.estaPagada())
        venta.registrarPago(BigDecimal("20"))
        assertImporte("50", venta.calcularSaldoPendiente())
        assertFalse(venta.estaPagada())
        venta.registrarPago(BigDecimal("50.00"))
        assertImporte("0", venta.calcularSaldoPendiente())
        assertTrue(venta.estaPagada())
        assertEquals(2, venta.pagos.size)
        assertEquals(2, venta.pagos.map { it.id }.distinct().size)
    }

    @Test
    fun rechazaPagosInvalidosSinRegistrarlos() {
        val venta = nuevaVenta()
        for (monto in listOf("0", "-1", "70.01")) {
            assertThrows(IllegalArgumentException::class.java) {
                venta.registrarPago(BigDecimal(monto))
            }
        }
        assertTrue(venta.pagos.isEmpty())
        assertImporte("70", venta.calcularSaldoPendiente())
    }

    @Test
    fun quitaProductoPorNombreYRecalculaTotal() {
        val venta = nuevaVenta()
        venta.agregarProducto(lapiz, 3)
        venta.quitarProducto("Cuaderno")
        assertEquals(listOf(lapiz), venta.detalles.map { it.producto })
        assertImporte("15", venta.calcularTotal())
        assertImporte("9", venta.calcularGanancia())
    }

    @Test
    fun noPermiteQuitarElUltimoProducto() {
        val venta = nuevaVenta()
        assertThrows(IllegalArgumentException::class.java) {
            venta.quitarProducto("Cuaderno")
        }
        assertImporte("70", venta.calcularTotal())
        assertEquals(1, venta.detalles.size)
    }

    @Test
    fun noPermiteReducirElTotalPorDebajoDeLoPagado() {
        val venta = nuevaVenta()
        venta.agregarProducto(lapiz, 1)
        venta.registrarPago(BigDecimal("71"))
        assertThrows(IllegalArgumentException::class.java) {
            venta.quitarProducto("Lapiz")
        }
        assertImporte("75", venta.calcularTotal())
        assertImporte("4", venta.calcularSaldoPendiente())
        assertEquals(2, venta.detalles.size)
    }

    @Test
    fun rechazaVentaSinDetallesOConIdentificadoresRepetidos() {
        assertThrows(IllegalArgumentException::class.java) {
            Venta("Vacia", fecha, cliente, emptyList())
        }
        assertThrows(IllegalArgumentException::class.java) {
            Venta("Duplicados", fecha, cliente,
                listOf(DetalleVenta(1L, cuaderno, 1), DetalleVenta(1L, lapiz, 1)))
        }
    }

    @Test
    fun copiaLosDetallesInicialesYEntregaUnaInstantanea() {
        val iniciales = mutableListOf(DetalleVenta(1L, cuaderno, 2))
        val venta = Venta("Copia", fecha, cliente, iniciales)
        iniciales.clear()
        val anteriores = venta.detalles
        venta.agregarProducto(lapiz, 1)
        assertEquals(1, anteriores.size)
        assertEquals(2, venta.detalles.size)
        assertImporte("75", venta.calcularTotal())
    }
}
