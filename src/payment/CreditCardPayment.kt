package payment

/**
 * Implementasi metode pembayaran menggunakan Kartu Kredit.
 *
 * Menerapkan prinsip **Abstraksi** dan **Enkapsulasi**:
 * - Mengimplementasikan kontrak [PaymentMethod].
 * - Data sensitif kartu kredit ([cardNumber], [expiryDate], dan [cvv]) dienkapsulasi dengan modifier `private`.
 * - Biaya transaksi sebesar 2% dari nominal belanja.
 *
 * @property cardNumber Nomor kartu kredit (minimal 16 karakter)
 * @property expiryDate Tanggal kedaluwarsa kartu (misal: "12/28")
 * @property cvv Tiga digit kode keamanan di belakang kartu
 */
class CreditCardPayment(
    private val cardNumber: String,
    private val expiryDate: String,
    private val cvv: String
) : PaymentMethod {

    override val name: String = "Kartu Kredit"

    /**
     * Menghitung biaya administrasi transaksi kartu kredit sebesar 2% dari jumlah transaksi.
     *
     * @param amount Nominal transaksi
     * @return Biaya transaksi (2% dari amount)
     */
    override fun getFee(amount: Double): Double {
        return amount * 0.02
    }

    /**
     * Memproses verifikasi kartu kredit:
     * - Nomor kartu harus minimal 16 digit.
     * - CVV harus tepat 3 digit.
     *
     * @param amount Nominal transaksi
     * @return [PaymentResult.Success] jika valid, atau [PaymentResult.Failed] jika tidak valid
     */
    override fun processPayment(amount: Double): PaymentResult {
        if (cardNumber.length < 16) {
            return PaymentResult.Failed("Nomor kartu tidak valid", 401)
        }
        if (cvv.length != 3) {
            return PaymentResult.Failed("CVV tidak valid", 402)
        }
        return PaymentResult.Success("CC-${System.currentTimeMillis()}")
    }
}

