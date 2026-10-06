package product

/**
 * Representasi produk kategori Elektronik di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Pewarisan** (Inheritance) dari kelas [Product]:
 * - Menambahkan atribut khusus [brand], [warrantyMonths], dan [isPremium].
 * - Meng-override [calculateDiscount] dengan aturan bisnis khusus elektronik.
 * - Meng-override [displayInfo] dengan memanfaatkan kata kunci `super` untuk memanggil implementasi kelas induk.
 *
 * @param id ID unik produk
 * @param name Nama produk elektronik
 * @param price Nilai harga dasar produk
 * @param stock Jumlah unit produk dalam inventaris
 * @property brand Merek atau pabrikan barang elektronik (contoh: "ASUS", "Samsung")
 * @property warrantyMonths Durasi masa garansi dalam satuan bulan
 * @property isPremium Menandakan apakah produk termasuk kategori premium yang berhak atas diskon tambahan
 */
class ElectronicProduct(
    id: String,
    name: String,
    price: Double,
    stock: Int,
    val brand: String,
    val warrantyMonths: Int,
    val isPremium: Boolean
) : Product(id, name, price, stock) {

    /**
     * Menghitung besaran diskon khusus produk elektronik:
     * - Diskon dasar: 5% untuk seluruh barang elektronik.
     * - Diskon tambahan: 10% jika produk berstatus [isPremium].
     * - Diskon tambahan: 5% jika masa garansi lebih dari 24 bulan ([warrantyMonths] > 24).
     *
     * @return Total potongan diskon dalam satuan Rupiah
     */
    override fun calculateDiscount(): Double {
        var discount = 0.0

        // Diskon dasar: 5% untuk semua elektronik
        discount += price * 0.05

        // Diskon tambahan: 10% jika premium
        if (isPremium) {
            discount += price * 0.10
        }

        // Diskon tambahan: 5% jika garansi > 24 bulan
        if (warrantyMonths > 24) {
            discount += price * 0.05
        }

        return discount
    }

    /**
     * Mengembalikan nama kategori produk yaitu "Elektronik".
     */
    override fun getCategory(): String = "Elektronik"

    /**
     * Menampilkan informasi produk secara lengkap termasuk spesifikasi khusus elektronik:
     * merek, durasi garansi, dan status premium.
     */
    override fun displayInfo() {
        super.displayInfo()
        println("Merek     : $brand")
        println("Garansi   : $warrantyMonths bulan")
        println("Premium   : ${if (isPremium) "✅ Ya" else "❌ Tidak"}")
        println("=".repeat(50))
    }
}

