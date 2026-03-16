package com.example.ecole.controller;

import com.example.ecole.entity.Note;
import com.example.ecole.service.NoteService;
import com.example.ecole.service.EtudiantService;
import com.example.ecole.service.MatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private EtudiantService etudiantService;
    
    @Autowired
    private MatiereService matiereService;
    
    @GetMapping
    public String listNotes(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Note> notes = noteService.findAll();
        model.addAttribute("notes", notes);
        model.addAttribute("matieres", matiereService.findAll());
        
        if (!notes.isEmpty()) {
            double somme = 0;
            double meilleure = 0;
            for (Note note : notes) {
                double valeur = note.getValeur();
                somme += valeur;
                if (valeur > meilleure) meilleure = valeur;
            }
            model.addAttribute("moyenneGenerale", somme / notes.size());
            model.addAttribute("meilleureNote", meilleure);
            model.addAttribute("notesSaisies", notes.size());
        }
        
        return "notes/list";
    }
    
    @GetMapping("/new")
    public String showForm(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("note", new Note());
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("matieres", matiereService.findAll());
        return "notes/form";
    }
    
    @PostMapping("/save")
    public String saveNote(@ModelAttribute Note note, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        if (note.getEtudiant() != null && note.getEtudiant().getId() != null) {
            note.setEtudiant(etudiantService.findById(note.getEtudiant().getId()));
        }
        if (note.getMatiere() != null && note.getMatiere().getId() != null) {
            note.setMatiere(matiereService.findById(note.getMatiere().getId()));
        }
        
        noteService.save(note);
        return "redirect:/notes";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        Note note = noteService.findById(id);
        if (note == null) return "redirect:/notes";
        
        model.addAttribute("note", note);
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("matieres", matiereService.findAll());
        return "notes/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        noteService.deleteById(id);
        return "redirect:/notes";
    }
}