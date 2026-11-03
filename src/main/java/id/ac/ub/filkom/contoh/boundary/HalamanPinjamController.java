package id.ac.ub.filkom.contoh.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.BuktiReservasi;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import id.ac.ub.filkom.contoh.control.ReservasiService;

@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {

    private final PinjamService pinjamService;
    private final ReservasiService reservasiService;

    public HalamanPinjamController(PinjamService pinjamService,
                                   ReservasiService reservasiService) {
        this.pinjamService = pinjamService;
        this.reservasiService = reservasiService;
    }

    @GetMapping
    public String tampilHalaman(Model model) {
        model.addAttribute("daftarAnggota", pinjamService.daftarAnggota());
        model.addAttribute("daftarBuku", pinjamService.daftarBuku());
        return "pinjam";
    }

    /** [1] pilihBuku(idBuku) dari aktor Anggota. */
    @PostMapping
    public String pilihBuku(@RequestParam String idAnggota,
                            @RequestParam String idBuku,
                            Model model) {
        try {
            // [2] pinjamBuku(idAnggota, idBuku)
            Bukti bukti = pinjamService.pinjamBuku(idAnggota, idBuku);
            return tampilBukti(bukti, model);                 // [12]
        } catch (PinjamGagalException e) {                    // [13]
            model.addAttribute("pesan", e.getMessage());
            model.addAttribute("bisaReservasi", e.isBisaReservasi());
            model.addAttribute("idAnggotaDipilih", idAnggota);
            model.addAttribute("idBukuDipilih", idBuku);
            return tampilHalaman(model);                      // [14]
        }
    }

    /** UC-03 [1] ajukanReservasi(idBuku) */
    @PostMapping("/reservasi")
    public String ajukanReservasi(@RequestParam String idAnggota,
                                  @RequestParam String idBuku,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            // UC-03 [2]
            BuktiReservasi bukti = reservasiService.ajukanReservasi(idAnggota, idBuku);
            // UC-03 [11]
            redirectAttributes.addFlashAttribute("sukses",
                    "Reservasi " + bukti.getIdReservasi() + " untuk buku \""
                            + bukti.getJudulBuku() + "\" berhasil diajukan.");
            return "redirect:/pinjam";
        } catch (PinjamGagalException e) {                    // UC-03 [13]
            model.addAttribute("pesan", e.getMessage());      // UC-03 [14]
            model.addAttribute("bisaReservasi", false);
            model.addAttribute("idAnggotaDipilih", idAnggota);
            model.addAttribute("idBukuDipilih", idBuku);
            return tampilHalaman(model);
        }
    }

    private String tampilBukti(Bukti bukti, Model model) {
        model.addAttribute("bukti", bukti);
        return "bukti";
    }
}