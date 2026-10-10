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

/**
 * File program utama untuk mendemonstrasikan implementasi seluruh spesifikasi
 * Sistem Manajemen E-Commerce berbasis OOP pada Tugas Kelompok PBO.
 *
 * Mendemonstrasikan 14 skenario fitur & pilar OOP:
 * 1. Inisialisasi Sistem
 * 2. Penambahan Produk (Elektronik, Pakaian, Makanan)
 * 3. Registrasi Pengguna
 * 4. Katalog Produk
 * 5. Pengelolaan Keranjang Belanja User 1
 * 6. Tampilan & Kalkulasi Diskon Keranjang User 1
 * 7. Proses Checkout & Pembayaran Kartu Kredit User 1
 * 8. Rincian & Transisi Status Pesanan User 1
 * 9. Belanja & Pembayaran QRIS User 2
 * 10. Rekapitulasi Laporan Penjualan Toko
 * 11. Demonstrasi Polimorfisme (Polymorphic references & dynamic dispatch)
 * 12. Demonstrasi Sealed Class (Exhaustive when & status guard)
 * 13. Demonstrasi Enkapsulasi (Data hiding & private accessors)
 * 14. Demonstrasi Smart Casting (`is` check & safe cast `as?`)
 */
fun main() {
    println("=".repeat(55))
    println("🛍️  SELAMAT DATANG DI TOKO ONLINE KAMPUS")
    println("=".repeat(55))
    println()

    // ============================================================
    // 1. Inisialisasi Sistem
    // ============================================================
    println(">>> 1. INISIALISASI SISTEM E-COMMERCE <<<")
    val system = ECommerceSystem("Toko Online Kampus")
    println("Sistem '${system.name}' berhasil diinisialisasi.\n")

    // ============================================================
    // 2. Menambahkan Produk (Minimal 6 produk: 2 per kategori)
    // ============================================================
    println("--- MENAMBAHKAN PRODUK ---")
    // Kategori Elektronik (1 premium, 1 non-premium)
    val laptopAsus = ElectronicProduct(
        id = "E001",
        name = "Laptop Gaming ASUS ROG",
        price = 15000000.0,
        stock = 10,
        brand = "ASUS",
        warrantyMonths = 36,
        isPremium = true
    )
    val smartphoneXiaomi = ElectronicProduct(
        id = "E002",
        name = "Smartphone Xiaomi Redmi",
        price = 2500000.0,
        stock = 15,
        brand = "Xiaomi",
        warrantyMonths = 12,
        isPremium = false
    )

    // Kategori Pakaian (1 seasonal, 1 non-seasonal)
    val jaketWinter = ClothingProduct(
        id = "C001",
        name = "Jaket Musim Dingin Parka",
        price = 400000.0,
        stock = 20,
        size = "XL",
        material = "Wol & Dacron",
        isSeasonal = true
    )
    val kaosPolos = ClothingProduct(
        id = "C002",
        name = "Kaos Polos Cotton Combed 30s",
        price = 100000.0,
        stock = 50,
        size = "L",
        material = "Katun Combed",
        isSeasonal = false
    )

    // Kategori Makanan (1 organik, 1 non-organik)
    val berasOrganik = FoodProduct(
        id = "F001",
        name = "Beras Organik Raja Pandan",
        price = 85000.0,
        stock = 30,
        expiryDate = "2026-12-31",
        weight = 5000.0,
        isOrganic = true
    )
    val mieInstan = FoodProduct(
        id = "F002",
        name = "Mie Instan Goreng Spesial",
        price = 3500.0,
        stock = 100,
        expiryDate = "2026-06-30",
        weight = 85.0,
        isOrganic = false
    )

    // Daftarkan semua produk ke sistem
    system.addProduct(laptopAsus)
    system.addProduct(smartphoneXiaomi)
    system.addProduct(jaketWinter)
    system.addProduct(kaosPolos)
    system.addProduct(berasOrganik)
    system.addProduct(mieInstan)
    println()

    // ============================================================
    // 3. Registrasi User (Minimal 2 user)
    // ============================================================
    println("--- REGISTRASI USER ---")
    system.registerUser("budi", "budi@kampus.ac.id", "rahasiaBudi123")
    system.registerUser("siti", "siti@kampus.ac.id", "sitiAman456")
    system.registerUser("budi", "budi.baru@kampus.ac.id", "passwordLain")
    println()

    // Dapatkan instance user untuk simulasi transaksi
    val userBudi = system.findUser("budi") ?: error("User budi tidak ditemukan")
    val userSiti = system.findUser("siti") ?: error("User siti tidak ditemukan")

    // ============================================================
    // 4. Tampilkan Semua Produk
    // ============================================================
    println("--- KATALOG SEMUA PRODUK ---")
    system.displayAllProducts()
    println()

    // ============================================================
    // 5. User 1 (Budi): Menambahkan Produk ke Keranjang
    // ============================================================
    println("--- BUDI MENAMBAH BARANG KE KERANJANG ---")
    val cartBudi = userBudi.getCart()
    cartBudi.addItem(laptopAsus, 1)     // 1 unit Laptop Gaming
    cartBudi.addItem(jaketWinter, 2)    // 2 unit Jaket Musim Dingin
    cartBudi.addItem(mieInstan, 5)      // 5 bungkus Mie Instan
    // Uji validasi penambahan stok melebihi batas
    println("Mencoba memesan barang melebihi stok yang tersedia:")
    cartBudi.addItem(laptopAsus, 999)
    println()

    // ============================================================
    // 6. User 1 (Budi): Tampilkan Keranjang
    // ============================================================
    println("--- TAMPILAN KERANJANG BELANJA BUDI ---")
    cartBudi.displayCart()
    println()

    // ============================================================
    // 7. User 1 (Budi): Checkout dengan Metode Pembayaran
    // ============================================================
    println("--- CHECKOUT BUDI (KARTU KREDIT) ---")
    val ccPayment = CreditCardPayment(
        cardNumber = "4111222233334444",
        expiryDate = "12/28",
        cvv = "888"
    )
    val feeBudi = ccPayment.getFee(cartBudi.getTotalPrice())
    println("Metode Pembayaran: ${ccPayment.name}")
    println("Biaya Layanan (${ccPayment.name}): Rp ${formatRupiah(feeBudi)}")

    val orderBudi = userBudi.checkout(ccPayment)
    if (orderBudi != null) {
        system.addOrder(orderBudi)
    }
    println()

    // ============================================================
    // 8. Tampilkan Order User 1 & Demonstrasi Status Order
    // ============================================================
    println("--- STATUS DAN DETAIL ORDER BUDI ---")
    userBudi.displayOrders()
    println()
    if (orderBudi != null) {
        orderBudi.displayOrder()
        println("\n>>> Demonstrasi Pembaruan Status Pesanan <<<")
        orderBudi.updateStatus(OrderStatus.Paid)
        orderBudi.updateStatus(OrderStatus.Shipped)
        orderBudi.updateStatus(OrderStatus.Delivered)
        println("Mencoba mengubah status pesanan yang sudah berstatus final (Delivered):")
        orderBudi.updateStatus(OrderStatus.Cancelled("Pelanggan ingin refund"))
    }
    println()

    // ============================================================
    // 9. User 2 (Siti): Belanja & Checkout dengan QRIS
    // ============================================================
    println("--- SITI MENAMBAH BARANG KE KERANJANG ---")
    val cartSiti = userSiti.getCart()
    cartSiti.addItem(berasOrganik, 2)       // 2 karung Beras Organik
    cartSiti.addItem(kaosPolos, 3)          // 3 pcs Kaos Polos
    cartSiti.displayCart()
    println()

    println("--- CHECKOUT SITI (QRIS) ---")
    val qrisPayment = QRISPayment(
        qrCode = "00020101021126580014ID.GO.QRIS.WWW.KAMPUS.NMID012345",
        merchantId = "MERCHANT_KAMPUS_01"
    )
    val feeSiti = qrisPayment.getFee(cartSiti.getTotalPrice())
    println("Metode Pembayaran: ${qrisPayment.name}")
    println("Biaya Layanan (${qrisPayment.name}): Rp ${formatRupiah(feeSiti)}")

    val orderSiti = userSiti.checkout(qrisPayment)
    if (orderSiti != null) {
        system.addOrder(orderSiti)
        orderSiti.updateStatus(OrderStatus.Paid)
        orderSiti.updateStatus(OrderStatus.Shipped)
    }
    println()
    userSiti.displayOrders()
    println()

    // ============================================================
    // 10. Tampilkan Laporan Penjualan Sistem
    // ============================================================
    println("--- LAPORAN PENJUALAN TOKO KAMPUS ---")
    system.displaySalesReport()
    println()

    // ============================================================
    // 11. Demonstrasi Polimorfisme (Polymorphism)
    // ============================================================
    println("=".repeat(55))
    println("🧠 11. DEMONSTRASI PILAR OOP: POLIMORFISME")
    println("=".repeat(55))
    println("Menampung berbagai jenis subclass ke dalam List<Product> (Polymorphic Reference):")
    val polymorphicProductList: List<Product> = listOf(
        laptopAsus,
        jaketWinter,
        berasOrganik
    )

    polymorphicProductList.forEach { product ->
        val diskon = product.calculateDiscount()
        val hargaAkhir = product.getDiscountedPrice()
        println("Produk: ${product.name}")
        println("  -> Kategori  : ${product.getCategory()}")
        println("  -> Harga Asli: ${product.formattedPrice}")
        println("  -> Diskon    : Rp ${formatRupiah(diskon)}")
        println("  -> Tagihan   : Rp ${formatRupiah(hargaAkhir)}")
    }
    println()

    println("Polimorfisme pada gerbang pembayaran (PaymentMethod):")
    val paymentList: List<PaymentMethod> = listOf(
        CreditCardPayment("1111222233334444", "10/27", "777"),
        QRISPayment("0123456789QRISPAY", "M_001"),
        BankTransferPayment("BCA", "1234567890")
    )
    val testAmount = 500000.0
    paymentList.forEach { method ->
        println("- ${method.name}: Fee untuk transaksi Rp ${formatRupiah(testAmount)} adalah Rp ${formatRupiah(method.getFee(testAmount))}")
    }
    println()

    // ============================================================
    // 12. Demonstrasi Sealed Class
    // ============================================================
    println("=".repeat(55))
    println("🔒 12. DEMONSTRASI SEALED CLASS (OrderStatus & PaymentResult)")
    println("=".repeat(55))
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
    println()

    // ============================================================
    // 13. Demonstrasi Enkapsulasi (Encapsulation)
    // ============================================================
    println("=".repeat(55))
    println("🛡️  13. DEMONSTRASI PILAR OOP: ENKAPSULASI")
    println("=".repeat(55))
    println("1. Perlindungan Password Pengguna:")
    println("   - Properti 'password' pada kelas User di-set 'private'.")
    println("   - Memverifikasi login dengan password salah ('salah123'): ${userBudi.authenticate("salah123")}")
    println("   - Memverifikasi login dengan password benar ('rahasiaBudi123'): ${userBudi.authenticate("rahasiaBudi123")}")
    println()

    println("2. Enkapsulasi Harga (Product.price):")
    println("   - 'price' dienkapsulasi dengan access modifier protected sehingga tidak dapat dimodifikasi bebas dari luar kelas.")
    println("   - Konsumen luar hanya dapat membaca harga terformat melalui 'formattedPrice': ${laptopAsus.formattedPrice}")
    println()

    println("3. Enkapsulasi Total Items pada Keranjang Belanja:")
    println("   - Properti 'totalItems' menggunakan 'private set'.")
    println("   - Nilai total items saat ini: ${userBudi.getCart().totalItems} item.")
    println("   - Pihak luar tidak dapat mengubah totalItems secara langsung tanpa melalui fungsi addItem/removeItem.")
    println()

    // ============================================================
    // 14. Demonstrasi Smart Casting & Safe Casting
    // ============================================================
    println("=".repeat(55))
    println("⚡ 14. DEMONSTRASI SMART CASTING & SAFE CASTING")
    println("=".repeat(55))

    val mixedCatalog: List<Any> = listOf(
        laptopAsus,
        jaketWinter,
        berasOrganik,
        "Produk Promosi Tambahan"
    )

    println("A. Smart Casting Otomatis menggunakan pemeriksaan tipe 'is':")
    for (item in mixedCatalog) {
        when (item) {
            is ElectronicProduct -> {
                // Di dalam blok ini, 'item' otomatis di-smart-cast menjadi ElectronicProduct
                println("✨ [Elektronik] Brand: ${item.brand}, Garansi: ${item.warrantyMonths} bulan, Premium: ${item.isPremium}")
            }
            is ClothingProduct -> {
                // Di dalam blok ini, 'item' otomatis di-smart-cast menjadi ClothingProduct
                println("✨ [Pakaian] Ukuran: ${item.size}, Bahan: ${item.material}, Seasonal: ${item.isSeasonal}")
            }
            is FoodProduct -> {
                // Di dalam blok ini, 'item' otomatis di-smart-cast menjadi FoodProduct
                println("✨ [Makanan] Berat: ${item.weight}g, Expired: ${item.expiryDate}, Organik: ${item.isOrganic}")
            }
            else -> {
                println("ℹ️  [Objek Lain]: '$item' bukan turunan dari kelas Product.")
            }
        }
    }
    println()

    println("B. Safe Casting yang aman menggunakan operator 'as?':")
    val testObj1: Any = laptopAsus
    val testObj2: Any = "Bukan produk elektronik"

    val castResult1: ElectronicProduct? = testObj1 as? ElectronicProduct
    val castResult2: ElectronicProduct? = testObj2 as? ElectronicProduct

    println("Hasil safe cast (testObj1 as? ElectronicProduct): ${castResult1?.name ?: "Gagal cast"}")
    println("Hasil safe cast (testObj2 as? ElectronicProduct): ${castResult2?.name ?: "Gagal cast (mengembalikan null tanpa Crash/Exception)"}")

    println()
    println("=".repeat(55))
    println("🎉 SELURUH DEMONSTRASI FITUR OOP BERHASIL DIJALANKAN!")
    println("=".repeat(55))
}

/**
 * Helper fungsi mandiri di file Main untuk memformat mata uang Rupiah.
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

