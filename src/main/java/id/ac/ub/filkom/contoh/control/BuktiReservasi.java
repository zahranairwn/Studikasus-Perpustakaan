package id.ac.ub.filkom.contoh.control;

import java.time.LocalDate;

import id.ac.ub.filkom.contoh.entity.Reservasi;

public class BuktiReservasi {

    private final String idReservasi;
    private final String judulBuku;
    private final LocalDate tanggalReservasi;
    private final String status;

    private BuktiReservasi(String idReservasi, String judulBuku,
                           LocalDate tanggalReservasi, String status) {
        this.idReservasi = idReservasi;
        this.judulBuku = judulBuku;
        this.tanggalReservasi = tanggalReservasi;
        this.status = status;
    }

    public static BuktiReservasi dari(Reservasi r) {
        return new BuktiReservasi(r.getIdReservasi(), r.getBuku().getJudul(),
                r.getTanggalReservasi(), r.getStatus());
    }

    public String getIdReservasi() { return idReservasi; }
    public String getJudulBuku() { return judulBuku; }
    public LocalDate getTanggalReservasi() { return tanggalReservasi; }
    public String getStatus() { return status; }
}