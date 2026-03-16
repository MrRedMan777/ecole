package com.example.ecole.service;

import com.example.ecole.entity.Professeur;
import com.example.ecole.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfesseurService {
    @Autowired
    private ProfesseurRepository professeurRepository;

    public List<Professeur> findAll() {
        return professeurRepository.findAll();
    }

    public Professeur findById(Long id) {
        return professeurRepository.findById(id).orElse(null);
    }

    public Professeur save(Professeur professeur) {
        return professeurRepository.save(professeur);
    }

    public void deleteById(Long id) {
        professeurRepository.deleteById(id);
    }
}