package com.example.ecole.controller;

import com.example.ecole.entity.Matiere;
import com.example.ecole.service.MatiereService;
import com.example.ecole.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/matieres")
public class MatiereController {
    @Autowired
    private MatiereService matiereService;
    @Autowired
    private ProfesseurService professeurService;

    @GetMapping
    public String listMatieres(Model model) {
        model.addAttribute("matieres", matiereService.findAll());
        return "matieres/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("matiere", new Matiere());
        model.addAttribute("professeurs", professeurService.findAll());
        return "matieres/form";
    }

    @PostMapping("/save")
    public String saveMatiere(@ModelAttribute Matiere matiere) {
        matiereService.save(matiere);
        return "redirect:/matieres";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Matiere matiere = matiereService.findById(id);
        if (matiere == null) return "redirect:/matieres";
        model.addAttribute("matiere", matiere);
        model.addAttribute("professeurs", professeurService.findAll());
        return "matieres/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMatiere(@PathVariable Long id) {
        matiereService.deleteById(id);
        return "redirect:/matieres";
    }
}