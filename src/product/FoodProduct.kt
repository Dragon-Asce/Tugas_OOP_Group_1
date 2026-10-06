package product

/**
 * Representasi produk kategori Makanan di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Pewarisan** (Inheritance) dari kelas [Product]:
 * - Menambahkan atribut khusus makanan [expiryDate], [weight], dan [isOrganic].
 * - Meng-override [calculateDiscount] dengan diskon dasar dan diskon makanan organik.
 * - Meng-override [displayInfo] untuk menampilkan atribut makanan.
 *
 * @param id ID unik produk
 * @param name Nama produk makanan
 * @param price Nilai harga dasar produk
 * @param stock Jumlah unit produk dalam inventaris
 * @property expiryDate Tanggal kadaluarsa produk dalam format tanggal (contoh: "2026-12-31")
 * @property weight Berat bersih produk dalam satuan gram (contoh: 5000.0)
 * @property isOrganic Menandakan apakah produk merupakan makanan organik bersertifikasi yang berhak atas diskon khusus
 */
class FoodProduct(
    id: String,
    name: String,
    price: Double,
    stock: Int,
    val expiryDate: String,
    val weight: Double,
    val isOrganic: Boolean
) : Product(id, name, price, stock) {

    /**
     * Menghitung diskon khusus produk makanan:
     * - Diskon dasar: 5% untuk semua kategori makanan.
     * - Diskon tambahan: 20% jika produk merupakan produk organik ([isOrganic]).
     *
     * @return Total potongan diskon dalam satuan Rupiah
     */
    override fun calculateDiscount(): Double {
        var discount = 0.0

        // Diskon dasar: 5% untuk semua makanan
        discount += price * 0.05

        // Diskon tambahan: 20% jika organik
        if (isOrganic) {
            discount += price * 0.20
        }

        return discount
    }

    /**
     * Mengembalikan nama kategori produk yaitu "Makanan".
     */
    override fun getCategory(): String = "Makanan"

    /**
     * Menampilkan informasi produk secara lengkap termasuk berat netto, tanggal kadaluarsa, dan status organik.
     */
    override fun displayInfo() {
        super.displayInfo()
        println("Berat     : $weight gram")
        println("Kadaluarsa: $expiryDate")
        println("Organik   : ${if (isOrganic) "✅ Ya" else "❌ Tidak"}")
        println("=".repeat(50))
    }
}

