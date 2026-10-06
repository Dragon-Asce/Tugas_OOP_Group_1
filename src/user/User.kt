package user

import cart.ShoppingCart
import order.Order
import payment.PaymentMethod
import payment.PaymentResult

/**
 * Kelas yang merepresentasikan pengguna (customer) di dalam sistem e-commerce.
 *
 * Menerapkan prinsip **Enkapsulasi**:
 * - Properti [password] bersifat `private` dan tidak memiliki getter publik, hanya dapat diverifikasi lewat [authenticate].
 * - Koleksi [orders] dan objek [cart] dienkapsulasi untuk mengontrol integritas transaksi.
 * - Informasi total pesanan dan total pengeluaran disediakan melalui properti terhitung [orderCount] dan [totalSpent].
 *
 * @property username Nama unik pengguna untuk login dan identifikasi
 * @property email Alamat email pengguna
 * @property password Kata sandi akun (dienkapsulasi secara private)
 */
class User(
    val username: String,
    val email: String,
    private val password: String
) {
    // ============================================================
    // PROPERTI
    // ============================================================

    /**
     * Daftar riwayat seluruh pesanan yang pernah dilakukan oleh pengguna.
     */
    private val orders = mutableListOf<Order>()

    /**
     * Objek keranjang belanja milik pengguna ini.
     */
    private val cart = ShoppingCart(username)

    /**
     * Jumlah transaksi pesanan yang telah berhasil dibuat.
     */
    val orderCount: Int
        get() = orders.size

    /**
     * Akumulasi nominal uang yang telah dibelanjakan pada seluruh pesanan.
     */
    val totalSpent: Double
        get() = orders.sumOf { it.totalPrice }

    // ============================================================
    // METODE
    // ============================================================

    /**
     * Memverifikasi keabsahan kata sandi pengguna tanpa membocorkan kata sandi asli (enkapsulasi).
     *
     * @param inputPassword Kata sandi yang dimasukkan pengguna
     * @return `true` jika kata sandi cocok, `false` jika salah
     */
    fun authenticate(inputPassword: String): Boolean {
        return password == inputPassword
    }

    /**
     * Mengakses objek keranjang belanja milik pengguna.
     *
     * @return Objek [ShoppingCart] milik user
     */
    fun getCart(): ShoppingCart = cart

    /**
     * Melakukan proses checkout belanja dari keranjang menggunakan metode pembayaran yang dipilih.
     * Menerapkan prinsip Polimorfisme pada parameter [paymentMethod].
     *
     * @param paymentMethod Objek pembayaran polimorfik yang mengimplementasikan [PaymentMethod]
     * @return Objek [Order] jika pembayaran berhasil, atau `null` jika pembayaran gagal / keranjang kosong
     */
    fun checkout(paymentMethod: PaymentMethod): Order? {
        if (cart.isEmpty()) {
            println("❌ Keranjang kosong")
            return null
        }

        val amount = cart.getTotalPrice()
        println("💳 Memproses pembayaran dengan ${paymentMethod.name}...")

        val result = paymentMethod.processPayment(amount)
        return when (result) {
            is PaymentResult.Success -> {
                println("✅ Pembayaran berhasil! ID: ${result.transactionId}")
                val order = createOrder()
                orders.add(order)
                println("✅ Order ${order.id} berhasil dibuat")
                order
            }
            is PaymentResult.Failed -> {
                println("❌ Pembayaran gagal: ${result.reason} (Error Code: ${result.errorCode})")
                null
            }
            PaymentResult.Pending -> {
                println("⏳ Pembayaran pending...")
                null
            }
        }
    }

    /**
     * Helper privat untuk membuat objek [Order] baru dari item keranjang belanja saat ini
     * dan mengosongkan keranjang belanja setelahnya.
     *
     * @return Objek [Order] yang baru dibuat
     */
    private fun createOrder(): Order {
        val orderId = "ORD-${System.currentTimeMillis()}"
        val items = cart.getItems()
        val order = Order(orderId, username, items)
        // Kosongkan keranjang setelah order dibuat
        cart.clear()
        return order
    }

    /**
     * Mengambil daftar salinan riwayat pesanan (read-only list).
     *
     * @return List berisi objek [Order]
     */
    fun getOrders(): List<Order> = orders.toList()

    /**
     * Menampilkan daftar seluruh riwayat pesanan pengguna ke konsol beserta total belanjaan.
     */
    fun displayOrders() {
        println("=".repeat(50))
        println("📋 RIWAYAT ORDER - $username")
        println("=".repeat(50))

        if (orders.isEmpty()) {
            println("   Belum ada order")
        } else {
            orders.forEachIndexed { index, order ->
                println("${index + 1}. Order ${order.id} - ${order.status.display()}")
                println("   Total: Rp ${formatRupiah(order.totalPrice)}")
            }
        }
        println("Total Belanja: Rp ${formatRupiah(totalSpent)}")
        println("=".repeat(50))
    }

    /**
     * Helper privat untuk memformat angka nominal menjadi format Rupiah dengan pemisah titik ribuan.
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

