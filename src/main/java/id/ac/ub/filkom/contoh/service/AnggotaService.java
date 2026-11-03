// package id.ac.ub.filkom.contoh.service;

// import java.util.List;

// import org.springframework.stereotype.Service;

// import id.ac.ub.filkom.contoh.entity.Anggota;
// import id.ac.ub.filkom.contoh.repository.AnggotaRepository;

// /** Service: logika untuk fitur anggota. */
// @Service
// public class AnggotaService {

//     private final AnggotaRepository anggotaRepository;

//     public AnggotaService(AnggotaRepository anggotaRepository) {
//         this.anggotaRepository = anggotaRepository;
//     }

//     public List<Anggota> lihatSemuaAnggota() {
//         return anggotaRepository.findAll();
//     }
// }

package id.ac.ub.filkom.contoh.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import id.ac.ub.filkom.contoh.entity.Anggota;
import id.ac.ub.filkom.contoh.repository.AnggotaRepository;

@Service
public class AnggotaService {

    private final AnggotaRepository anggotaRepository;

    public AnggotaService(AnggotaRepository anggotaRepository) {
        this.anggotaRepository = anggotaRepository;
    }

    public List<Anggota> daftarAnggota() {
        return anggotaRepository.findAll(Sort.by("idAnggota"));
    }

    public Anggota tambahAnggota(String idAnggota, String nama, int batasPinjam) {
        if (idAnggota == null || idAnggota.isBlank()) {
            throw new IllegalArgumentException("ID anggota wajib diisi");
        }
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama wajib diisi");
        }
        String id = idAnggota.trim();
        if (id.length() > 10) {
            throw new IllegalArgumentException("ID anggota maksimal 10 karakter");
        }
        if (nama.trim().length() > 100) {
            throw new IllegalArgumentException("Nama maksimal 100 karakter");
        }
        if (batasPinjam < 1) {
            throw new IllegalArgumentException("Batas pinjam minimal 1");
        }
        if (anggotaRepository.existsById(id)) {
            throw new IllegalArgumentException("ID anggota sudah dipakai");
        }
        return anggotaRepository.save(new Anggota(id, nama.trim(), batasPinjam));
    }
}