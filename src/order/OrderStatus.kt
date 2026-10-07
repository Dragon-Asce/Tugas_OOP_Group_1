package order

/**
 * Representasi status pesanan dalam sistem e-commerce menggunakan **Sealed Class**.
 *
 * Menerapkan konsep **Sealed Class** dan **Polimorfisme**:
 * - Membatasi hierarki status yang valid hanya pada jenis status yang telah didefinisikan secara terbatas.
 * - Memungkinkan pengecekan `when` secara ekshaustif (exhaustive check) tanpa memerlukan branch `else`.
 * - Menyediakan metode abstrak [display] dan helper [isFinal] untuk menentukan apakah status sudah berstatus akhir.
 */
sealed class OrderStatus {

    /**
     * Status ketika pesanan baru dibuat dan menunggu pelunasan pembayaran.
     */
    data object Pending : OrderStatus() {
        override fun display(): String = "⏳ Menunggu Pembayaran"
    }

    /**
     * Status ketika pembayaran pesanan telah berhasil diverifikasi.
     */
    data object Paid : OrderStatus() {
        override fun display(): String = "✅ Dibayar"
    }

    /**
     * Status ketika barang pesanan sedang dalam proses pengiriman oleh kurir logistik.
     */
    data object Shipped : OrderStatus() {
        override fun display(): String = "🚚 Dikirim"
    }

    /**
     * Status ketika pesanan telah tiba di tujuan dan diterima oleh pelanggan (status final).
     */
    data object Delivered : OrderStatus() {
        override fun display(): String = "📦 Diterima"
    }

    /**
     * Status ketika pesanan dibatalkan beserta alasannya (status final).
     *
     * @property reason Alasan pembatalan pesanan
     */
    data class Cancelled(val reason: String) : OrderStatus() {
        override fun display(): String = "❌ Dibatalkan: $reason"
    }

    /**
     * Mengembalikan teks representasi status pesanan lengkap dengan ikon indikator.
     *
     * @return String representasi status pesanan
     */
    abstract fun display(): String

    /**
     * Memeriksa apakah status pesanan sudah bersifat final ([Delivered] atau [Cancelled]).
     * Pesanan dengan status final tidak dapat diubah lagi statusnya.
     *
     * @return `true` jika status adalah Delivered atau Cancelled, `false` jika masih dalam proses
     */
    fun isFinal(): Boolean {
        return this is Delivered || this is Cancelled
    }
}

