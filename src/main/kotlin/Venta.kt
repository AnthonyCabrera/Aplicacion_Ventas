import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Una venta pertenece a un cliente y contiene al menos un detalle.
 * Los identificadores de detalles y pagos son locales a esta venta.
 */
class Venta(
    val nombre: String,
    val fecha: LocalDateTime,
    val cliente: Cliente,
    detallesIniciales: List<DetalleVenta>
) {
    private val detallesInternos = detallesIniciales.toMutableList()
    private val pagosInternos = mutableListOf<Pago>()

    val detalles: List<DetalleVenta>
        get() = detallesInternos.toList()

    val pagos: List<Pago>
        get() = pagosInternos.toList()

    init {
        require(detallesInternos.isNotEmpty()) { "La venta debe contener al menos un detalle." }
        require(detallesInternos.map { it.id }.distinct().size == detallesInternos.size) {
            "Los identificadores de los detalles no pueden repetirse."
        }
    }

    // Se conservan el precio y costo al agregar el producto a la venta.
    fun agregarProducto(producto: Producto, cantidad: Int) {
        val idDetalle = (detallesInternos.maxOfOrNull { it.id } ?: 0L) + 1L
        detallesInternos.add(
            DetalleVenta(
                id = idDetalle,
                producto = producto,
                cantidad = cantidad
            )
        )
    }

    fun quitarProducto(nombreProducto: String) {
        val restantes = detallesInternos.filter { it.producto.nombre != nombreProducto }
        require(restantes.isNotEmpty()) { "La venta debe conservar al menos un detalle." }
        val nuevoTotal = restantes.fold(BigDecimal.ZERO) { total, detalle ->
            total + detalle.calcularSubtotal()
        }
        require(nuevoTotal >= calcularTotalPagado()) {
            "No se puede reducir el total por debajo del monto ya pagado."
        }
        detallesInternos.removeAll { it.producto.nombre == nombreProducto }
    }

    fun calcularTotal(): BigDecimal =
        detallesInternos.fold(BigDecimal.ZERO) { total, detalle ->
            total + detalle.calcularSubtotal()
        }

    fun calcularGanancia(): BigDecimal =
        detallesInternos.fold(BigDecimal.ZERO) { total, detalle ->
            total + detalle.calcularGanancia()
        }

    fun calcularSaldoPendiente(): BigDecimal = calcularTotal() - calcularTotalPagado()

    fun estaPagada(): Boolean = calcularSaldoPendiente().compareTo(BigDecimal.ZERO) == 0

    fun registrarPago(monto: BigDecimal) {
        require(monto > BigDecimal.ZERO) { "El monto del pago debe ser mayor que cero." }
        require(monto <= calcularSaldoPendiente()) {
            "El pago no puede superar el saldo pendiente."
        }
        pagosInternos.add(
            Pago(
                id = (pagosInternos.maxOfOrNull { it.id } ?: 0L) + 1L,
                fecha = LocalDateTime.now(),
                monto = monto
            )
        )
    }

    private fun calcularTotalPagado(): BigDecimal =
        pagosInternos.fold(BigDecimal.ZERO) { total, pago -> total + pago.monto }
}
