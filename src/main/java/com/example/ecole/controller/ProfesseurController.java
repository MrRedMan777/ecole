package com.example.ecole.controller;

import com.example.ecole.entity.Professeur;
import com.example.ecole.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {
    @Autowired
    private ProfesseurService professeurService;

    @GetMapping
    public String listProfesseurs(Model model) {
        model.addAttribute("professeurs", professeurService.findAll());
        return "professeurs/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("professeur", new Professeur());
        return "professeurs/form";
    }

    @PostMapping("/save")
    public String saveProfesseur(@ModelAttribute Professeur professeur) {
        professeurService.save(professeur);
        return "redirect:/professeurs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Professeur professeur = professeurService.findById(id);
        if (professeur == null) return "redirect:/professeurs";
        model.addAttribute("professeur", professeur);
        return "professeurs/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProfesseur(@PathVariable Long id) {
        professeurService.deleteById(id);
        return "redirect:/professeurs";
    }
}