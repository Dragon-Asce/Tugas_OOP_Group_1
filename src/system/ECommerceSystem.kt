package system

import order.Order
import product.Product
import user.User

/**
 * Pengendali utama (Main Controller) untuk sistem manajemen e-commerce.
 *
 * Menerapkan prinsip **Enkapsulasi**:
 * - Mengelola koleksi produk ([products]), pengguna ([users]), dan pesanan ([orders]) secara privat.
 * - Menyediakan API publik terstruktur untuk manajemen produk, autentikasi/registrasi user,
 *   dan pelaporan penjualan sistem.
 *
 * @property name Nama platform toko online (contoh: "Toko Online Kampus")
 */
class ECommerceSystem(val name: String = "Toko Online") {

    // ============================================================
    // PROPERTI
    // ============================================================

    /**
     * Koleksi produk yang terdaftar di dalam katalog toko.
     */
    private val products = mutableListOf<Product>()

    /**
     * Koleksi pengguna yang terdaftar di dalam sistem.
     */
    private val users = mutableListOf<User>()

    /**
     * Koleksi seluruh pesanan yang telah diselesaikan di dalam sistem toko.
     */
    private val orders = mutableListOf<Order>()

    /**
     * Jumlah keseluruhan pesanan yang tercatat dalam sistem.
     */
    val orderCount: Int
        get() = orders.size


    // ============================================================
    // MANAJEMEN PRODUK
    // ============================================================

    /**
     * Menambahkan produk baru ke katalog toko online.
     *
     * @param product Objek [Product] yang akan ditambahkan
     */
    fun addProduct(product: Product) {
        products.add(product)
        println("✅ Produk ${product.name} ditambahkan")
    }

    /**
     * Mencari produk berdasarkan kata kunci yang dicocokkan dengan nama atau ID produk (case-insensitive).
     *
     * @param keyword Kata kunci pencarian
     * @return List produk yang sesuai dengan kriteria pencarian
     */
    fun searchProduct(keyword: String): List<Product> {
        return products.filter {
            it.name.lowercase().contains(keyword.lowercase()) ||
            it.id.lowercase().contains(keyword.lowercase())
        }
    }

    /**
     * Menemukan produk berdasarkan ID uniknya.
     *
     * @param id ID produk yang dicari
     * @return Objek [Product] jika ditemukan, atau `null` jika tidak ditemukan
     */
    fun findProductById(id: String): Product? {
        return products.find { it.id.equals(id, ignoreCase = true) }
    }

    /**
     * Mengambil seluruh daftar produk terdaftar sebagai read-only list.
     *
     * @return List seluruh objek [Product]
     */
    fun getAllProducts(): List<Product> = products.toList()

    /**
     * Menampilkan seluruh katalog produk ke konsol dengan memanggil [Product.displayInfo].
     */
    fun displayAllProducts() {
        println("=".repeat(55))
        println("📦 SEMUA PRODUK")
        println("=".repeat(55))
        println("Total: ${products.size} produk")
        println("-".repeat(55))

        products.forEach { product ->
            product.displayInfo()
        }
    }

    // ============================================================
    // MANAJEMEN USER
    // ============================================================

    /**
     * Mendaftarkan pengguna baru ke dalam sistem setelah memvalidasi keunikan username.
     *
     * @param username Nama pengguna baru
     * @param email Alamat email
     * @param password Kata sandi akun pengguna
     * @return `true` jika registrasi berhasil, `false` jika username sudah terpakai
     */
    fun registerUser(username: String, email: String, password: String): Boolean {
        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            println("❌ Username $username sudah digunakan")
            return false
        }
        val user = User(username, email, password)
        users.add(user)
        println("✅ User $username berhasil didaftarkan")
        return true
    }

    /**
     * Menemukan akun pengguna berdasarkan nama pengguna.
     *
     * @param username Nama pengguna yang dicari
     * @return Objek [User] jika ditemukan, atau `null` jika tidak terdaftar
     */
    fun findUser(username: String): User? {
        return users.find { it.username.equals(username, ignoreCase = true) }
    }

    // ============================================================
    // MANAJEMEN ORDER & LAPORAN
    // ============================================================

    /**
     * Menambahkan pesanan baru ke dalam rekapitulasi transaksi toko.
     *
     * @param order Objek [Order] yang telah berhasil dibuat
     */
    fun addOrder(order: Order) {
        orders.add(order)
    }

    /**
     * Menghitung total keseluruhan pendapatan (revenue) dari semua order yang tercatat.
     *
     * @return Akumulasi pendapatan dalam satuan Rupiah
     */
    fun getTotalRevenue(): Double {
        return orders.sumOf { it.totalPrice }
    }

    /**
     * Mengambil jumlah pesanan yang ada dalam sistem.
     *
     * @return Total pesanan
     */
    @JvmName("fetchOrderCount")
    fun getOrderCount(): Int = orders.size


    /**
     * Menampilkan laporan penjualan toko online ke konsol,
     * merangkum total pesanan, total omset/revenue, dan daftar riwayat tiap transaksi.
     */
    fun displaySalesReport() {
        println("=".repeat(55))
        println("📊 LAPORAN PENJUALAN")
        println("=".repeat(55))
        println("Total Order   : $orderCount")
        println("Total Revenue : Rp ${formatRupiah(getTotalRevenue())}")
        println("-".repeat(55))

        if (orders.isEmpty()) {
            println("   Belum ada penjualan")
        } else {
            orders.forEachIndexed { index, order ->
                println("${index + 1}. ${order.id} - ${order.customerName}")
                println("   Status: ${order.status.display()}")
                println("   Total: Rp ${formatRupiah(order.totalPrice)}")
            }
        }
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
