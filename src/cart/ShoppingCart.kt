package cart

import product.Product

/**
 * Kelas yang merepresentasikan keranjang belanja seorang pelanggan.
 *
 * Menerapkan prinsip **Enkapsulasi**:
 * - Koleksi [items] bersifat `private` untuk mencegah manipulasi eksternal tanpa validasi stok.
 * - Properti [totalItems] memiliki getter publik namun setter `private` ([private set]),
 *   sehingga hanya metode internal yang dapat memodifikasi jumlah item.
 *
 * @property owner Nama pemilik keranjang belanja
 */
class ShoppingCart(val owner: String) {

    // ============================================================
    // PROPERTI
    // ============================================================

    /**
     * Map privat yang memetakan objek [Product] ke jumlah kuantitas yang dimasukkan ke keranjang.
     */
    private val items = mutableMapOf<Product, Int>()

    /**
     * Total jumlah kuantitas barang yang ada di dalam keranjang.
     * Hanya dapat dibaca secara publik, mutasi hanya melalui metode [addItem], [removeItem], atau [clear].
     */
    var totalItems: Int = 0
        private set

    // ============================================================
    // METODE
    // ============================================================

    /**
     * Menambahkan sejumlah unit produk ke dalam keranjang.
     * Melakukan validasi kuantitas harus positif dan memeriksa ketersediaan stok pada [product].
     *
     * @param product Objek produk yang akan ditambahkan
     * @param quantity Jumlah unit produk
     * @return `true` jika berhasil ditambahkan dan stok mencukupi, `false` jika gagal
     */
    fun addItem(product: Product, quantity: Int): Boolean {
        if (quantity <= 0) {
            println("❌ Jumlah harus lebih dari 0")
            return false
        }

        if (!product.reduceStock(quantity)) {
            println("❌ Stok tidak mencukupi (tersedia: ${product.stock})")
            return false
        }

        items[product] = items.getOrDefault(product, 0) + quantity
        totalItems += quantity
        println("✅ ${product.name} x$quantity ditambahkan ke keranjang")
        return true
    }

    /**
     * Menghapus seluruh kuantitas suatu produk dari keranjang belanja
     * dan mengembalikan stok produk tersebut ke inventaris.
     *
     * @param product Objek produk yang akan dihapus dari keranjang
     * @return `true` jika produk ada di keranjang dan berhasil dihapus, `false` jika tidak ditemukan
     */
    fun removeItem(product: Product): Boolean {
        val quantity = items[product] ?: return false
        items.remove(product)
        totalItems -= quantity
        // Kembalikan stok ke produk
        product.reduceStock(-quantity)
        println("✅ ${product.name} dihapus dari keranjang")
        return true
    }

    /**
     * Menghitung total harga belanja seluruh barang di keranjang setelah dipotong diskon masing-masing produk.
     *
     * @return Total biaya belanja dalam satuan Rupiah
     */
    fun getTotalPrice(): Double {
        return items.entries.sumOf { (product, quantity) ->
            product.getDiscountedPrice() * quantity
        }
    }

    /**
     * Menghitung total akumulasi penghematan diskon dari seluruh barang di keranjang.
     *
     * @return Total nominal diskon dalam satuan Rupiah
     */
    fun getTotalDiscount(): Double {
        return items.entries.sumOf { (product, quantity) ->
            product.calculateDiscount() * quantity
        }
    }

    /**
     * Mendapatkan salinan read-only dari seluruh item produk beserta kuantitasnya yang ada di keranjang.
     *
     * @return Map immutable berisi [Product] dan kuantitasnya
     */
    fun getItems(): Map<Product, Int> = items.toMap()

    /**
     * Memeriksa apakah keranjang belanja saat ini dalam keadaan kosong.
     *
     * @return `true` jika tidak ada barang di keranjang, `false` jika ada
     */
    fun isEmpty(): Boolean = items.isEmpty()

    /**
     * Mengosongkan seluruh item dari keranjang belanja dan mereset [totalItems] ke 0.
     * Digunakan setelah proses checkout pesanan berhasil diselesaikan.
     */
    fun clear() {
        items.clear()
        totalItems = 0
    }

    /**
     * Menampilkan rincian isi keranjang belanja ke konsol, termasuk nama barang,
     * kuantitas, harga subtotal, potongan diskon, dan total keseluruhan.
     */
    fun displayCart() {
        println("=".repeat(50))
        println("🛒 KERANJANG BELANJA - $owner")
        println("=".repeat(50))

        if (items.isEmpty()) {
            println("   Keranjang kosong")
        } else {
            items.forEach { (product, quantity) ->
                println("${product.name} x$quantity = Rp ${formatRupiah(product.getDiscountedPrice() * quantity)}")
                println("   (Diskon: Rp ${formatRupiah(product.calculateDiscount() * quantity)})")
            }
            println("-".repeat(50))
            println("Total Diskon : Rp ${formatRupiah(getTotalDiscount())}")
            println("Total Belanja: Rp ${formatRupiah(getTotalPrice())}")
        }
        println("=".repeat(50))
    }

    /**
     * Helper privat untuk memformat nominal angka menjadi format Rupiah dengan pemisah ribuan titik.
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

