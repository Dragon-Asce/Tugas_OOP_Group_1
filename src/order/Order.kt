package order

import product.Product

/**
 * Kelas yang merepresentasikan pesanan pembelian (order) di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Enkapsulasi** dan **Polimorfisme**:
 * - Menyimpan snapshot daftar produk yang dibeli melalui [items].
 * - Menghitung total harga dan total diskon secara dinamis melalui properti terhitung.
 * - Menggunakan [OrderStatus] (sealed class) untuk melacak status transaksi dengan proteksi [updateStatus]
 *   sehingga pesanan yang sudah berstatus final tidak dapat diubah kembali.
 *
 * @property id ID unik pesanan (contoh: "ORD-1700000000001")
 * @property customerName Nama pelanggan yang melakukan pemesanan
 * @property items Map berisi daftar produk dan kuantitas yang dipesan
 * @property status Status pesanan saat ini, default bernilai [OrderStatus.Pending]
 */
class Order(
    val id: String,
    val customerName: String,
    val items: Map<Product, Int>,
    var status: OrderStatus = OrderStatus.Pending
) {
    // ============================================================
    // PROPERTI
    // ============================================================

    /**
     * Total nilai pembayaran pesanan setelah memperhitungkan diskon tiap produk.
     */
    val totalPrice: Double
        get() = items.entries.sumOf { (product, quantity) ->
            product.getDiscountedPrice() * quantity
        }

    /**
     * Total potongan diskon yang didapatkan pelanggan pada pesanan ini.
     */
    val totalDiscount: Double
        get() = items.entries.sumOf { (product, quantity) ->
            product.calculateDiscount() * quantity
        }

    /**
     * Waktu penciptaan pesanan dalam format ISO LocalDateTime.
     */
    val createdAt: String = java.time.LocalDateTime.now().toString()

    // ============================================================
    // METODE
    // ============================================================

    /**
     * Memperbarui status pesanan ke status baru.
     * Mengembalikan nilai `false` dan menolak perubahan jika status pesanan saat ini telah final ([OrderStatus.isFinal]).
     *
     * @param newStatus Status pesanan baru yang akan diaplikasikan
     * @return `true` jika status berhasil diperbarui, `false` jika pesanan sudah dalam status final
     */
    fun updateStatus(newStatus: OrderStatus): Boolean {
        if (status.isFinal()) {
            println("❌ Status sudah final, tidak bisa diubah")
            return false
        }
        status = newStatus
        println("✅ Status order $id diubah menjadi: ${status.display()}")
        return true
    }

    /**
     * Menampilkan informasi rincian nota pesanan ke konsol.
     */
    fun displayOrder() {
        println("=".repeat(55))
        println("📋 DETAIL ORDER")
        println("=".repeat(55))
        println("ID Order    : $id")
        println("Pelanggan   : $customerName")
        println("Tanggal     : $createdAt")
        println("Status      : ${status.display()}")
        println("-".repeat(55))
        println("Items:")
        items.forEach { (product, quantity) ->
            println("   ${product.name} x$quantity = Rp ${formatRupiah(product.getDiscountedPrice() * quantity)}")
        }
        println("-".repeat(55))
        println("Total Diskon: Rp ${formatRupiah(totalDiscount)}")
        println("Total Harga : Rp ${formatRupiah(totalPrice)}")
        println("=".repeat(55))
    }

    /**
     * Helper privat untuk memformat angka nominal menjadi format Rupiah dengan pemisah ribuan.
     */
    private fun formatRupiah(nominal: Double): String {
        val str = nominal.toLong().toString()
        val builder = StringBuilder()
        var count = 0
        for (i in str.length - 1 downTo 0) {
            builder.insert(0, str[i])
            count++
            if (count % 3 == 0 && i > 0) {
                builder.insert(0, ".")
            }
        }
        return builder.toString()
    }
}

