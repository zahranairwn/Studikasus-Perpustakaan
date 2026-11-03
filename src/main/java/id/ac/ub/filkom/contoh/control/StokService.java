package id.ac.ub.filkom.contoh.control;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ub.filkom.contoh.entity.Buku;
import id.ac.ub.filkom.contoh.repository.BukuRepository;

/** «control» use case Kelola Stok Buku (UC-02, aktor Pustakawan). */
@Service
public class StokService {

    private final BukuRepository bukuRepository;

    public StokService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    @Transactional(readOnly = true)
    public List<Buku> daftarBuku() {
        return bukuRepository.findAll(Sort.by("judul"));
    }

    @Transactional
    public Buku tambahStok(String idBuku, int jumlah) {
        Buku buku = cariBuku(idBuku);       // [3]
        buku.tambahStok(jumlah);            // [4] -> [5]
        return bukuRepository.save(buku);   // [6] -> [7]
    }

    @Transactional
    public Buku aturStok(String idBuku, int stokBaru) {
        Buku buku = cariBuku(idBuku);       // [3]
        buku.aturStok(stokBaru);            // [4] -> [5]
        return bukuRepository.save(buku);   // [6] -> [7]
    }

    private Buku cariBuku(String idBuku) {
        return bukuRepository.findById(idBuku)
                .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
    }
}