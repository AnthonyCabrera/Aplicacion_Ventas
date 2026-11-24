import java.math.BigDecimal

data class DetalleVenta(
    val id: Long,
    val producto: Producto,
    val cantidad: Int,
    val precioUnitario: BigDecimal = producto.precioVenta,
    val costoUnitario: BigDecimal = producto.costoCompra
) {
    init {
        require(cantidad > 0) { "La cantidad debe ser mayor que cero." }
        require(precioUnitario >= BigDecimal.ZERO) { "El precio unitario no puede ser negativo." }
        require(costoUnitario >= BigDecimal.ZERO) { "El costo unitario no puede ser negativo." }
    }

    fun calcularSubtotal(): BigDecimal = precioUnitario * cantidad.toBigDecimal()

    fun calcularGanancia(): BigDecimal =
        (precioUnitario - costoUnitario) * cantidad.toBigDecimal()
}
