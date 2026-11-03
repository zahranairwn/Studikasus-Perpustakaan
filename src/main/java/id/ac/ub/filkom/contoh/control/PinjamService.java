package id.ac.ub.filkom.contoh.control;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.entity.TransaksiPinjam;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;
import id.ac.ub.filkom.contoh.repository.BukuRepository;
import id.ac.ub.filkom.contoh.repository.TransaksiPinjamRepository;

/** «control» PinjamController pada desain 8a, direalisasikan sebagai @Service. */
@Service
public class PinjamService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final TransaksiPinjamRepository transaksiRepository;

    public PinjamService(AnggotaRepository anggotaRepository,
                         BukuRepository bukuRepository,
                         TransaksiPinjamRepository transaksiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.transaksiRepository = transaksiRepository;
    }

    @Transactional(readOnly = true)
    public List<Buku> daftarBuku() {
        return bukuRepository.findAll(Sort.by("judul"));
    }

    @Transactional
    public Bukti pinjamBuku(String idAnggota, String idBuku) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Anggota"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Buku"));

        // [3] cekBatasPinjam() -> [4] boleh pinjam
        if (!anggota.cekBatasPinjam()) {
            throw PinjamGagalException.melewatiBatas();   // alur 4b
        }

        // [5] cekStok() -> [6] stok tersedia / habis
        if (!buku.cekStok()) {
            throw PinjamGagalException.stokHabis();       // [13] pesanHabis
        }

        // [7] buat(idAnggota, idBuku) -> [8] transaksi tersimpan
        TransaksiPinjam transaksi = TransaksiPinjam.buat(anggota, buku);
        transaksiRepository.save(transaksi);

        // [9] kurangiStok() -> [10] ok
        buku.kurangiStok();
        bukuRepository.save(buku);

        return Bukti.dari(transaksi);                     // [11] bukti peminjaman
    }

    @Transactional(readOnly = true)
    public List<Anggota> daftarAnggota() {
        return anggotaRepository.findAll(Sort.by("nama"));
    }
}