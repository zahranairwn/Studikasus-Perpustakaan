package id.ac.ub.filkom.contoh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import id.ac.ub.filkom.contoh.entity.Reservasi;

public interface ReservasiRepository extends JpaRepository<Reservasi, String> {

    boolean existsByAnggota_IdAnggotaAndBuku_IdBukuAndStatus(
            String idAnggota, String idBuku, String status);
}