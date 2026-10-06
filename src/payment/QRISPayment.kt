package payment

/**
 * Implementasi metode pembayaran menggunakan QRIS (Quick Response Code Indonesian Standard).
 *
 * Menerapkan prinsip **Abstraksi** dan **Enkapsulasi**:
 * - Mengimplementasikan kontrak [PaymentMethod].
 * - Properti [qrCode] dan [merchantId] dienkapsulasi dengan modifier `private`.
 * - Biaya transaksi sebesar 0.5% dari nominal belanja.
 *
 * @property qrCode Kode representasi string payload QRIS (minimal 10 karakter)
 * @property merchantId ID unik merchant e-commerce
 */
class QRISPayment(
    private val qrCode: String,
    private val merchantId: String
) : PaymentMethod {

    override val name: String = "QRIS"

    /**
     * Menghitung biaya administrasi transaksi QRIS sebesar 0.5% dari nominal transaksi.
     *
     * @param amount Nominal transaksi
     * @return Biaya transaksi (0.5% dari amount)
     */
    override fun getFee(amount: Double): Double {
        return amount * 0.005
    }

    /**
     * Memproses pembayaran menggunakan QRIS:
     * - Panjang kode QR harus minimal 10 karakter.
     *
     * @param amount Nominal transaksi
     * @return [PaymentResult.Success] jika kode QR valid, atau [PaymentResult.Failed] jika tidak valid
     */
    override fun processPayment(amount: Double): PaymentResult {
        if (qrCode.length < 10) {
            return PaymentResult.Failed("Kode QR tidak valid", 403)
        }
        return PaymentResult.Success("QR-${System.currentTimeMillis()}")
    }
}

