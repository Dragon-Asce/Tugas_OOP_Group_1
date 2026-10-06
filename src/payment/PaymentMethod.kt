package payment

/**
 * Interface yang mendefinisikan kontrak sistem pembayaran di e-commerce.
 *
 * Menerapkan prinsip **Abstraksi** dan **Polimorfisme**:
 * - Mengabstraksikan perilaku berbagai macam gerbang pembayaran (Payment Gateway).
 * - Kelas yang mengimplementasikan interface ini wajib menyediakan kalkulasi biaya ([getFee])
 *   dan mekanisme pemrosesan transaksi ([processPayment]).
 */
interface PaymentMethod {

    /**
     * Nama metode pembayaran (misal: "Kartu Kredit", "QRIS", "Transfer Bank").
     */
    val name: String

    /**
     * Memproses transaksi pembayaran dengan nominal tertentu.
     *
     * @param amount Nominal transaksi yang harus dibayarkan
     * @return Objek [PaymentResult] yang merepresentasikan hasil proses pembayaran
     */
    fun processPayment(amount: Double): PaymentResult

    /**
     * Menghitung biaya administrasi/transaksi berdasarkan nominal pembayaran.
     *
     * @param amount Nominal transaksi
     * @return Besaran biaya transaksi dalam Rupiah
     */
    fun getFee(amount: Double): Double
}

/**
 * Representasi status hasil pemrosesan transaksi pembayaran menggunakan **Sealed Class**.
 *
 * Memungkinkan penanganan hasil pembayaran secara komprehensif dan type-safe
 * melalui percabangan `when`.
 */
sealed class PaymentResult {

    /**
     * Status ketika pembayaran sukses diverifikasi.
     *
     * @property transactionId Kode unik referensi transaksi yang berhasil dibuat
     */
    data class Success(val transactionId: String) : PaymentResult()

    /**
     * Status ketika pembayaran gagal diproses karena kesalahan input atau penolakan bank.
     *
     * @property reason Keterangan penyebab kegagalan transaksi
     * @property errorCode Kode status error numerik
     */
    data class Failed(val reason: String, val errorCode: Int) : PaymentResult()

    /**
     * Status ketika transaksi sedang menunggu konfirmasi atau verifikasi pihak ketiga.
     */
    data object Pending : PaymentResult()
}

