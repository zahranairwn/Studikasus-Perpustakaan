package id.ac.ub.filkom.contoh.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservasi")
public class Reservasi {

    public static final String MENUNGGU = "MENUNGGU";
    public static final String DIBATALKAN = "DIBATALKAN";

    @Id
    @Column(name = "id_reservasi", length = 10)
    private String idReservasi;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_anggota")
    private Anggota anggota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_buku")
    private Buku buku;

    @Column(name = "tanggal_reservasi")
    private LocalDate tanggalReservasi;

    @Column(name = "status", length = 20)
    private String status;

    protected Reservasi() { }

    public static Reservasi buat(Anggota anggota, Buku buku) {
        if (buku.cekStok()) {
            throw new IllegalStateException("Buku masih tersedia, tidak perlu reservasi");
        }
        Reservasi r = new Reservasi();
        r.idReservasi = "R" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase();
        r.anggota = anggota;
        r.buku = buku;
        r.tanggalReservasi = LocalDate.now();
        r.status = MENUNGGU;
        return r;
    }

    public boolean isMenunggu() { return MENUNGGU.equals(status); }

    public void batalkan() {
        if (!isMenunggu()) {
            throw new IllegalStateException("Hanya reservasi berstatus MENUNGGU yang bisa dibatalkan");
        }
        status = DIBATALKAN;
    }

    public String getIdReservasi() { return idReservasi; }
    public Anggota getAnggota() { return anggota; }
    public Buku getBuku() { return buku; }
    public LocalDate getTanggalReservasi() { return tanggalReservasi; }
    public String getStatus() { return status; }
}