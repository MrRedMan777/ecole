package com.example.ecole.repository;

import com.example.ecole.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByNumeroEtudiant(String numeroEtudiant);
}