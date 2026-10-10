import cart.ShoppingCart
import order.Order
import order.OrderStatus
import payment.BankTransferPayment
import payment.CreditCardPayment
import payment.PaymentMethod
import payment.PaymentResult
import payment.QRISPayment
import product.ClothingProduct
import product.ElectronicProduct
import product.FoodProduct
import product.Product
import system.ECommerceSystem
import user.User
import java.text.NumberFormat
import java.util.Locale

// Extension function untuk format mata uang Rupiah
fun Double.toRupiah(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(this)
}

// Helper cetak pemisah seksi
private fun printSection(title: String) {
    println("\n" + "=".repeat(55))
    println(title)
    println("=".repeat(55))
}

fun main() {
    println("=".repeat(55))
    println("🛍️  SELAMAT DATANG DI TOKO ONLINE KAMPUS")
    println("=".repeat(55))\n

    // 1. Inisialisasi Sistem & Data
    val system = ECommerceSystem("Toko Online Kampus")
    println(">>> 1. INISIALISASI SISTEM E-COMMERCE <<<")
    println("Sistem '${system.name}' berhasil diinisialisasi.\n")

    val products = setupProducts(system)
    setupUsers(system)

    val userBudi = system.findUser("budi") ?: error("User budi tidak ditemukan")
    val userSiti = system.findUser("siti") ?: error("User siti tidak ditemukan")

    // 2. Transaksi User 1 (Budi) & User 2 (Siti)
    runUserBudiFlow(system, userBudi, products)
    runUserSitiFlow(system, userSiti, products)

    // 3. Laporan Penjualan
    println("--- LAPORAN PENJUALAN TOKO KAMPUS ---")
    system.displaySalesReport()

    // 4. Demonstrasi Pilar OOP
    demonstratePolymorphism(products)
    demonstrateSealedClass()
    demonstrateEncapsulation(userBudi, products.laptopAsus)
    demonstrateSmartCasting(products)

    println("=".repeat(55))
    println("🎉 SELURUH DEMONSTRASI FITUR OOP BERHASIL DIJALANKAN!")
    println("=".repeat(55))
}

// Data Container untuk mempermudah passing referensi produk
data class ProductCatalog(
    val laptopAsus: ElectronicProduct,
    val smartphoneXiaomi: ElectronicProduct,
    val jaketWinter: ClothingProduct,
    val kaosPolos: ClothingProduct,
    val berasOrganik: FoodProduct,
    val mieInstan: FoodProduct
)

private fun setupProducts(system: ECommerceSystem): ProductCatalog {
    println("--- MENAMBAHKAN PRODUK ---")
    val catalog = ProductCatalog(
        laptopAsus = ElectronicProduct("E001", "Laptop Gaming ASUS ROG", 15000000.0, 10, "ASUS", 36, true),
        smartphoneXiaomi = ElectronicProduct("E002", "Smartphone Xiaomi Redmi", 2500000.0, 15, "Xiaomi", 12, false),
        jaketWinter = ClothingProduct("C001", "Jaket Musim Dingin Parka", 400000.0, 20, "XL", "Wol & Dacron", true),
        kaosPolos = ClothingProduct("C002", "Kaos Polos Cotton Combed 30s", 100000.0, 50, "L", "Katun Combed", false),
        berasOrganik = FoodProduct("F001", "Beras Organik Raja Pandan", 85000.0, 30, "2026-12-31", 5000.0, true),
        mieInstan = FoodProduct("F002", "Mie Instan Goreng Spesial", 3500.0, 100, "2026-06-30", 85.0, false)
    )

    listOf(
        catalog.laptopAsus, catalog.smartphoneXiaomi,
        catalog.jaketWinter, catalog.kaosPolos,
        catalog.berasOrganik, catalog.mieInstan
    ).forEach { system.addProduct(it) }

    println()
    return catalog
}

private fun setupUsers(system: ECommerceSystem) {
    println("--- REGISTRASI USER ---")
    system.registerUser("budi", "budi@kampus.ac.id", "rahasiaBudi123")
    system.registerUser("siti", "siti@kampus.ac.id", "sitiAman456")
    system.registerUser("budi", "budi.baru@kampus.ac.id", "passwordLain")
    println()

    println("--- KATALOG SEMUA PRODUK ---")
    system.displayAllProducts()
    println()
}

private fun runUserBudiFlow(system: ECommerceSystem, user: User, products: ProductCatalog) {
    println("--- BUDI MENAMBAH BARANG KE KERANJANG ---")
    val cart = user.getCart()
    cart.addItem(products.laptopAsus, 1)
    cart.addItem(products.jaketWinter, 2)
    cart.addItem(products.mieInstan, 5)

    println("Mencoba memesan barang melebihi stok yang tersedia:")
    cart.addItem(products.laptopAsus, 999)
    println()

    println("--- TAMPILAN KERANJANG BELANJA BUDI ---")
    cart.displayCart()
    println()

    println("--- CHECKOUT BUDI (KARTU KREDIT) ---")
    val ccPayment = CreditCardPayment("4111222233334444", "12/28", "888")
    println("Metode Pembayaran: ${ccPayment.name}")
    println("Biaya Layanan (${ccPayment.name}): ${ccPayment.getFee(cart.getTotalPrice()).toRupiah()}")

    val order = user.checkout(ccPayment)
    order?.let {
        system.addOrder(it)
        println("\n--- STATUS DAN DETAIL ORDER BUDI ---")
        user.displayOrders()
        println()
        it.displayOrder()
        println("\n>>> Demonstrasi Pembaruan Status Pesanan <<<")
        it.updateStatus(OrderStatus.Paid)
        it.updateStatus(OrderStatus.Shipped)
        it.updateStatus(OrderStatus.Delivered)
        println("Mencoba mengubah status pesanan yang sudah berstatus final (Delivered):")
        it.updateStatus(OrderStatus.Cancelled("Pelanggan ingin refund"))
    }
    println()
}

private fun runUserSitiFlow(system: ECommerceSystem, user: User, products: ProductCatalog) {
    println("--- SITI MENAMBAH BARANG KE KERANJANG ---")
    val cart = user.getCart()
    cart.addItem(products.berasOrganik, 2)
    cart.addItem(products.kaosPolos, 3)
    cart.displayCart()
    println()

    println("--- CHECKOUT SITI (QRIS) ---")
    val qrisPayment = QRISPayment("00020101021126580014ID.GO.QRIS.WWW.KAMPUS.NMID012345", "MERCHANT_KAMPUS_01")
    println("Metode Pembayaran: ${qrisPayment.name}")
    println("Biaya Layanan (${qrisPayment.name}): ${qrisPayment.getFee(cart.getTotalPrice()).toRupiah()}")

    val order = user.checkout(qrisPayment)
    order?.let {
        system.addOrder(it)
        it.updateStatus(OrderStatus.Paid)
        it.updateStatus(OrderStatus.Shipped)
    }
    println()
    user.displayOrders()
    println()
}

private fun demonstratePolymorphism(products: ProductCatalog) {
    printSection("🧠 11. DEMONSTRASI PILAR OOP: POLIMORFISME")
    println("Menampung berbagai jenis subclass ke dalam List<Product> (Polymorphic Reference):")
    val polymorphicProductList: List<Product> = listOf(
        products.laptopAsus,
        products.jaketWinter,
        products.berasOrganik
    )

    polymorphicProductList.forEach { product ->
        println("Produk: ${product.name}")
        println("  -> Kategori  : ${product.getCategory()}")
        println("  -> Harga Asli: ${product.formattedPrice}")
        println("  -> Diskon    : ${product.calculateDiscount().toRupiah()}")
        println("  -> Tagihan   : ${product.getDiscountedPrice().toRupiah()}")
    }

    println("\nPolimorfisme pada gerbang pembayaran (PaymentMethod):")
    val paymentList: List<PaymentMethod> = listOf(
        CreditCardPayment("1111222233334444", "10/27", "777"),
        QRISPayment("0123456789QRISPAY", "M_001"),
        BankTransferPayment("BCA", "1234567890")
    )
    val testAmount = 500000.0
    paymentList.forEach { method ->
        println("- ${method.name}: Fee untuk transaksi ${testAmount.toRupiah()} adalah ${method.getFee(testAmount).toRupiah()}")
    }
}

private fun demonstrateSealedClass() {
    printSection("🔒 12. DEMONSTRASI SEALED CLASS (OrderStatus & PaymentResult)")
    val sampleStatuses: List<OrderStatus> = listOf(
        OrderStatus.Pending,
        OrderStatus.Paid,
        OrderStatus.Shipped,
        OrderStatus.Delivered,
        OrderStatus.Cancelled("Stok habis di gudang pusat")
    )

    println("Evaluasi exhaustive 'when' pada sealed class OrderStatus:")
    sampleStatuses.forEach { status ->
        val statusMessage = when (status) {
            is OrderStatus.Pending -> "Tahap 1: Pembeli belum mentransfer dana."
            is OrderStatus.Paid -> "Tahap 2: Dana sudah masuk, pesanan siap dipacking."
            is OrderStatus.Shipped -> "Tahap 3: Paket sedang dalam perjalanan kurir."
            is OrderStatus.Delivered -> "Tahap 4: Transaksi selesai, barang diterima."
            is OrderStatus.Cancelled -> "Tahap Pembatalan: Transaksi gugur (Alasan: ${status.reason})."
        }
        println("Status [${status.display()}] -> $statusMessage (Final? ${if (status.isFinal()) '✅' else '❌'})")
    }
}

private fun demonstrateEncapsulation(user: User, laptop: ElectronicProduct) {
    printSection("🛡️ 13. DEMONSTRASI PILAR OOP: ENKAPSULASI")
    println("1. Perlindungan Password Pengguna:")
    println("   - Properti 'password' pada kelas User di-set 'private'.")
    println("   - Memverifikasi login password salah ('salah123'): ${user.authenticate("salah123")}")
    println("   - Memverifikasi login password benar ('rahasiaBudi123'): ${user.authenticate("rahasiaBudi123")}\n")

    println("2. Enkapsulasi Harga (Product.price):")
    println("   - 'price' dienkapsulasi dengan access modifier protected.")
    println("   - Konsumen luar membaca via 'formattedPrice': ${laptop.formattedPrice}\n")

    println("3. Enkapsulasi Total Items pada Keranjang Belanja:")
    println("   - Properti 'totalItems' menggunakan 'private set'.")
    println("   - Nilai total items saat ini: ${user.getCart().totalItems} item.")
}

private fun demonstrateSmartCasting(products: ProductCatalog) {
    printSection("⚡ 14. DEMONSTRASI SMART CASTING & SAFE CASTING")
    val mixedCatalog: List<Any> = listOf(
        products.laptopAsus,
        products.jaketWinter,
        products.berasOrganik,
        "Produk Promosi Tambahan"
    )

    println("A. Smart Casting Otomatis menggunakan pemeriksaan tipe 'is':")
    for (item in mixedCatalog) {
        when (item) {
            is ElectronicProduct -> println("✨ [Elektronik] Brand: ${item.brand}, Garansi: ${item.warrantyMonths} bulan, Premium: ${item.isPremium}")
            is ClothingProduct -> println("✨ [Pakaian] Ukuran: ${item.size}, Bahan: ${item.material}, Seasonal: ${item.isSeasonal}")
            is FoodProduct -> println("✨ [Makanan] Berat: ${item.weight}g, Expired: ${item.expiryDate}, Organik: ${item.isOrganic}")
            else -> println("ℹ️  [Objek Lain]: '$item' bukan turunan dari kelas Product.")
        }
    }

    println("\nB. Safe Casting yang aman menggunakan operator 'as?':")
    val testObj1: Any = products.laptopAsus
    val testObj2: Any = "Bukan produk elektronik"

    val castResult1 = testObj1 as? ElectronicProduct
    val castResult2 = testObj2 as? ElectronicProduct

    println("Hasil safe cast (testObj1 as? ElectronicProduct): ${castResult1?.name ?: "Gagal cast"}")
    println("Hasil safe cast (testObj2 as? ElectronicProduct): ${castResult2?.name ?: "Gagal cast (mengembalikan null tanpa Crash)"}")
}