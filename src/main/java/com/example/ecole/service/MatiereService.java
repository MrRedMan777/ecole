package com.example.ecole.service;

import com.example.ecole.entity.Matiere;
import com.example.ecole.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatiereService {
    @Autowired
    private MatiereRepository matiereRepository;

    public List<Matiere> findAll() {
        return matiereRepository.findAll();
    }

    public Matiere findById(Long id) {
        return matiereRepository.findById(id).orElse(null);
    }

    public Matiere save(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    public void deleteById(Long id) {
        matiereRepository.deleteById(id);
    }
}