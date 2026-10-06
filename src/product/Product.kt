package product

/**
 * Kelas abstrak yang merepresentasikan produk umum di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Abstraksi** dan **Enkapsulasi**:
 * - Properti [price] dienkapsulasi dengan modifier `protected` sehingga hanya dapat diakses dalam hierarki inheritance [Product].
 * - Menyediakan properti terhitung [formattedPrice] untuk mendapatkan harga dalam format Rupiah yang rapi.
 * - Mendefinisikan metode abstrak [calculateDiscount] dan [getCategory] yang wajib diimplementasikan oleh setiap subclass.
 *
 * @property id ID unik produk (contoh: "E001", "C001")
 * @property name Nama produk
 * @property price Nilai harga dasar produk (protected untuk enkapsulasi)
 * @property stock Jumlah stok produk yang tersedia di inventaris
 */
abstract class Product(
    val id: String,
    val name: String,
    protected var price: Double,
    var stock: Int
) {
    // ============================================================
    // PROPERTI
    // ============================================================

    /**
     * Getter untuk harga produk dalam format mata uang Rupiah.
     */
    val formattedPrice: String
        get() = "Rp ${formatRupiah(price)}"

    // ============================================================
    // METODE ABSTRAK
    // ============================================================

    /**
     * Menghitung diskon yang berlaku untuk produk.
     * Implementasi besaran diskon berbeda-beda pada setiap subclass.
     *
     * @return Jumlah potongan diskon dalam satuan Rupiah
     */
    abstract fun calculateDiscount(): Double

    /**
     * Mendapatkan nama kategori produk.
     *
     * @return Nama kategori produk (misal: "Elektronik", "Pakaian", "Makanan")
     */
    abstract fun getCategory(): String

    // ============================================================
    // METODE CONCRETE (Dapat digunakan oleh semua subclass)
    // ============================================================

    /**
     * Menghitung harga akhir setelah dipotong diskon.
     *
     * @return Harga akhir dalam satuan Rupiah
     */
    open fun getDiscountedPrice(): Double {
        return price - calculateDiscount()
    }

    /**
     * Menampilkan informasi detail produk ke konsol.
     * Dapat di-override oleh subclass untuk menyertakan atribut spesifik kategori.
     */
    open fun displayInfo() {
        println("=".repeat(50))
        println("📦 ${getCategory()} - $name")
        println("ID        : $id")
        println("Harga     : $formattedPrice")
        println("Diskon    : Rp ${formatRupiah(calculateDiscount())}")
        println("Harga Akhir: Rp ${formatRupiah(getDiscountedPrice())}")
        println("Stok      : $stock")
        println("=".repeat(50))
    }

    /**
     * Mengurangi jumlah stok produk jika stok mencukupi.
     * Dapat juga menerima nilai negatif untuk mengembalikan stok.
     *
     * @param quantity Jumlah unit produk yang dikurangi
     * @return `true` jika pengurangan stok berhasil, `false` jika stok tidak mencukupi
     */
    fun reduceStock(quantity: Int): Boolean {
        return if (stock >= quantity) {
            stock -= quantity
            true
        } else {
            false
        }
    }

    // ============================================================
    // HELPER METHOD (Protected)
    // ============================================================

    /**
     * Metode pembantu untuk memformat nilai desimal uang menjadi format mata uang Rupiah
     * dengan pemisah ribuan menggunakan titik.
     *
     * @param nominal Angka nominal uang dalam Double
     * @return String angka terformat (contoh: "15.000.000")
     */
    fun formatRupiah(nominal: Double): String {
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

