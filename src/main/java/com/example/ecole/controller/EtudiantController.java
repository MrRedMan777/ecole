package com.example.ecole.controller;

import com.example.ecole.entity.Etudiant;
import com.example.ecole.service.EtudiantService;
import com.example.ecole.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {
    
    @Autowired
    private EtudiantService etudiantService;
    
    @Autowired
    private NoteService noteService;
    
    // Redirection vers le bulletin (pour éviter 404)
    @GetMapping
    public String redirectToBulletin() {
        return "redirect:/etudiants/bulletin";
    }
    
    // Formulaire recherche bulletin
    @GetMapping("/bulletin")
    public String showBulletinForm() {
        return "etudiants/bulletin-form";
    }
    
    // Afficher bulletin
    @PostMapping("/bulletin")
    public String afficherBulletin(@RequestParam String numeroEtudiant, Model model) {
        Etudiant etudiant = etudiantService.findByNumero(numeroEtudiant);
        if (etudiant == null) {
            model.addAttribute("error", "Étudiant non trouvé");
            return "etudiants/bulletin-form";
        }
        
        Map<String, Object> stats = noteService.getStatistiquesEtudiant(etudiant.getId());
        
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("notesS1", stats.get("notesS1"));
        model.addAttribute("notesS2", stats.get("notesS2"));
        model.addAttribute("moyenneS1", stats.get("moyenneS1"));
        model.addAttribute("moyenneS2", stats.get("moyenneS2"));
        model.addAttribute("moyenneGenerale", stats.get("moyenneGenerale"));
        model.addAttribute("statutS1", stats.get("statutS1"));
        model.addAttribute("statutS2", stats.get("statutS2"));
        model.addAttribute("statutGeneral", stats.get("statutGeneral"));
        model.addAttribute("mentionS1", stats.get("mentionS1"));
        model.addAttribute("mentionS2", stats.get("mentionS2"));
        model.addAttribute("mentionGenerale", stats.get("mentionGenerale"));
        model.addAttribute("reprisesS1", stats.get("reprisesS1"));
        model.addAttribute("reprisesS2", stats.get("reprisesS2"));
        
        return "etudiants/bulletin";
    }
    
    // Détail des notes
    @GetMapping("/notes")
    public String voirNotes(@RequestParam String numero, Model model) {
        Etudiant etudiant = etudiantService.findByNumero(numero);
        if (etudiant == null) {
            return "redirect:/etudiants/bulletin";
        }
        
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("notes", noteService.getNotesByEtudiantId(etudiant.getId()));
        
        return "etudiants/notes";
    }
    
    // PARTIE ADMIN (protégée par session)
    @GetMapping("/liste")
    public String listEtudiants(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("etudiants", etudiantService.findAll());
        return "etudiants/list";
    }
    
    @GetMapping("/new")
    public String showForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("etudiant", new Etudiant());
        return "etudiants/form";
    }
    
    @PostMapping("/save")
    public String saveEtudiant(@ModelAttribute Etudiant etudiant, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        etudiantService.save(etudiant);
        return "redirect:/etudiants/liste";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        Etudiant etudiant = etudiantService.findById(id);
        if (etudiant == null) return "redirect:/etudiants/liste";
        model.addAttribute("etudiant", etudiant);
        return "etudiants/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteEtudiant(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        etudiantService.deleteById(id);
        return "redirect:/etudiants/liste";
    }
}