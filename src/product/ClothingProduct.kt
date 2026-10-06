package product

/**
 * Representasi produk kategori Pakaian di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Pewarisan** (Inheritance) dari kelas [Product]:
 * - Menambahkan atribut spesifik [size], [material], dan [isSeasonal].
 * - Meng-override [calculateDiscount] dengan skema diskon busana (diskon dasar, seasonal, dan ukuran besar).
 * - Meng-override [displayInfo] untuk menampilkan atribut pakaian.
 *
 * @param id ID unik produk
 * @param name Nama produk pakaian
 * @param price Nilai harga dasar produk
 * @param stock Jumlah unit produk dalam inventaris
 * @property size Ukuran pakaian (contoh: "S", "M", "L", "XL", "XXL")
 * @property material Bahan pembuat pakaian (contoh: "Katun", "Wol", "Polyester")
 * @property isSeasonal Menandakan apakah pakaian bersifat musiman (misal: pakaian musim dingin) yang memperoleh diskon khusus
 */
class ClothingProduct(
    id: String,
    name: String,
    price: Double,
    stock: Int,
    val size: String,
    val material: String,
    val isSeasonal: Boolean
) : Product(id, name, price, stock) {

    /**
     * Menghitung diskon khusus produk pakaian:
     * - Diskon dasar: 10% untuk semua produk pakaian.
     * - Diskon tambahan: 15% jika produk bersifat seasonal ([isSeasonal]).
     * - Diskon tambahan: 5% jika ukuran pakaian adalah XL, XXL, atau XXXL.
     *
     * @return Total potongan diskon dalam satuan Rupiah
     */
    override fun calculateDiscount(): Double {
        var discount = 0.0

        // Diskon dasar: 10% untuk semua pakaian
        discount += price * 0.10

        // Diskon tambahan: 15% jika seasonal
        if (isSeasonal) {
            discount += price * 0.15
        }

        // Diskon tambahan: 5% untuk size XL atau lebih besar
        if (size.uppercase() in listOf("XL", "XXL", "XXXL")) {
            discount += price * 0.05
        }

        return discount
    }

    /**
     * Mengembalikan nama kategori produk yaitu "Pakaian".
     */
    override fun getCategory(): String = "Pakaian"

    /**
     * Menampilkan informasi produk secara lengkap termasuk ukuran pakaian, bahan, dan status musiman.
     */
    override fun displayInfo() {
        super.displayInfo()
        println("Ukuran    : $size")
        println("Bahan     : $material")
        println("Seasonal  : ${if (isSeasonal) "✅ Ya" else "❌ Tidak"}")
        println("=".repeat(50))
    }
}

