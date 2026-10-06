package payment

/**
 * Implementasi metode pembayaran melalui Transfer Antar Bank.
 *
 * Menerapkan prinsip **Abstraksi** dan **Enkapsulasi**:
 * - Mengimplementasikan interface [PaymentMethod].
 * - Properti [bankName] dan [accountNumber] dienkapsulasi dengan modifier `private`.
 * - Biaya transaksi sebesar 1% dari total belanja dengan nominal minimum sebesar Rp 5.000.
 *
 * @property bankName Nama bank tujuan transfer (contoh: "BCA", "Mandiri", "BNI")
 * @property accountNumber Nomor rekening bank (minimal 8 karakter)
 */
class BankTransferPayment(
    private val bankName: String,
    private val accountNumber: String
) : PaymentMethod {

    override val name: String = "Transfer Bank"

    /**
     * Menghitung biaya transfer bank yaitu 1% dari nominal belanja,
     * dengan nilai minimum Rp 5.000.
     *
     * @param amount Nominal transaksi
     * @return Biaya transaksi dalam Rupiah
     */
    override fun getFee(amount: Double): Double {
        val fee = amount * 0.01
        return maxOf(fee, 5000.0)
    }

    /**
     * Memproses transfer bank:
     * - Nomor rekening harus minimal 8 digit.
     *
     * @param amount Nominal transaksi
     * @return [PaymentResult.Success] jika nomor rekening valid, atau [PaymentResult.Failed] jika tidak valid
     */
    override fun processPayment(amount: Double): PaymentResult {
        if (accountNumber.length < 8) {
            return PaymentResult.Failed("Nomor rekening tidak valid", 404)
        }
        return PaymentResult.Success("BT-${System.currentTimeMillis()}")
    }
}

