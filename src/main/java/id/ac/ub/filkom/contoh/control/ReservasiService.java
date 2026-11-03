package id.ac.ub.filkom.contoh.control;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.Reservasi;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.ReservasiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** «control» use case Ajukan Reservasi (UC-03). */
@Service
public class ReservasiService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final ReservasiRepository reservasiRepository;

    public ReservasiService(AnggotaRepository anggotaRepository,
                            BukuRepository bukuRepository,
                            ReservasiRepository reservasiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.reservasiRepository = reservasiRepository;
    }

    @Transactional
    public BuktiReservasi ajukanReservasi(String idAnggota, String idBuku) {
        // [3] ambil entity
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Anggota"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Buku"));

        // [4] cekStok() -> [5] hasil
        if (buku.cekStok()) {
            throw PinjamGagalException.masihTersedia();   // [13]
        }

        // [6] cek reservasi ganda
        if (reservasiRepository.existsByAnggota_IdAnggotaAndBuku_IdBukuAndStatus(
                idAnggota, idBuku, Reservasi.MENUNGGU)) {
            throw PinjamGagalException.sudahReservasi();  // [13]
        }

        // [7] buat(anggota, buku) -> [8] reservasi
        Reservasi reservasi = Reservasi.buat(anggota, buku);
        // [9] save(reservasi)
        reservasiRepository.save(reservasi);

        return BuktiReservasi.dari(reservasi);            // [10] bukti reservasi
    }
}