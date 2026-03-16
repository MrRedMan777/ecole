package com.example.ecole.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double valeur; // Note sur 100
    private Double coefficient;
    private LocalDateTime dateSaisie;
    private Integer semestre; // 1 ou 2
    private String commentaire;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    public Note() {
        this.dateSaisie = LocalDateTime.now();
        this.coefficient = 1.0;
    }
    
    public Note(Double valeur, Etudiant etudiant, Matiere matiere, Integer semestre) {
        this.valeur = valeur;
        this.etudiant = etudiant;
        this.matiere = matiere;
        this.semestre = semestre;
        this.dateSaisie = LocalDateTime.now();
        this.coefficient = 1.0;
    }

    // Méthodes métier
    public String getStatut() {
        return valeur >= 60 ? "Réussi" : "Reprise";
    }
    
    public String getMention() {
        if (valeur >= 80) return "Très bien";
        if (valeur >= 70) return "Bien";
        if (valeur >= 60) return "Passable";
        return "Insuffisant";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }
    public Double getCoefficient() { return coefficient; }
    public void setCoefficient(Double coefficient) { this.coefficient = coefficient; }
    public LocalDateTime getDateSaisie() { return dateSaisie; }
    public void setDateSaisie(LocalDateTime dateSaisie) { this.dateSaisie = dateSaisie; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
}