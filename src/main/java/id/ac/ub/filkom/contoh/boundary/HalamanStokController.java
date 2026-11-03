package id.ac.ub.filkom.contoh.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ub.filkom.contoh.control.StokService;

@Controller
@RequestMapping("/stok")
public class HalamanStokController {

    private final StokService stokService;

    public HalamanStokController(StokService stokService) {
        this.stokService = stokService;
    }

    @GetMapping
    public String tampilHalaman(Model model) {
        model.addAttribute("daftarBuku", stokService.daftarBuku());
        return "stok";
    }

    /** [1] tambahStok(idBuku, jumlah) */
    @PostMapping("/tambah")
    public String tambahStok(@RequestParam String idBuku,
                             @RequestParam int jumlah,
                             Model model, RedirectAttributes redirectAttributes) {
        try {
            stokService.tambahStok(idBuku, jumlah);                      // [2]
            redirectAttributes.addFlashAttribute("sukses",
                    "Stok buku " + idBuku + " bertambah " + jumlah);     // [8]
            return "redirect:/stok";
        } catch (IllegalArgumentException e) {                           // [9]
            model.addAttribute("pesan", e.getMessage());                 // [10]
            return tampilHalaman(model);
        }
    }

    /** [1] aturStok(idBuku, stokBaru) */
    @PostMapping("/atur")
    public String aturStok(@RequestParam String idBuku,
                           @RequestParam int stokBaru,
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            stokService.aturStok(idBuku, stokBaru);                      // [2]
            redirectAttributes.addFlashAttribute("sukses",
                    "Stok buku " + idBuku + " diatur menjadi " + stokBaru); // [8]
            return "redirect:/stok";
        } catch (IllegalArgumentException e) {                           // [9]
            model.addAttribute("pesan", e.getMessage());                 // [10]
            return tampilHalaman(model);
        }
    }
}