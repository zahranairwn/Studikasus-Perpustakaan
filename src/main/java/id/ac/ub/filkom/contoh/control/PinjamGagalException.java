package id.ac.ub.filkom.contoh.control;

public class PinjamGagalException extends RuntimeException {

    private final boolean bisaReservasi;

    private PinjamGagalException(String pesan, boolean bisaReservasi) {
        super(pesan);
        this.bisaReservasi = bisaReservasi;
    }

    public static PinjamGagalException tidakDitemukan(String objek) {
        return new PinjamGagalException(objek + " tidak ditemukan", false);
    }

    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Melewati batas pinjam", false);
    }

    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Stok buku habis", true);
    }

    public static PinjamGagalException masihTersedia() {
        return new PinjamGagalException("Buku masih tersedia, tidak perlu reservasi", false);
    }

    public static PinjamGagalException sudahReservasi() {
        return new PinjamGagalException("Anda sudah mereservasi buku ini", false);
    }

    public boolean isBisaReservasi() { return bisaReservasi; }
}