import java.math.BigDecimal

data class Producto(
    val id: Long,
    val nombre: String,
    val costoCompra: BigDecimal,
    val precioVenta: BigDecimal
) {
    init {
        require(costoCompra >= BigDecimal.ZERO) { "El costo de compra no puede ser negativo." }
        require(precioVenta >= BigDecimal.ZERO) { "El precio de venta no puede ser negativo." }
    }

    fun calcularGananciaUnitaria(): BigDecimal = precioVenta - costoCompra
}
