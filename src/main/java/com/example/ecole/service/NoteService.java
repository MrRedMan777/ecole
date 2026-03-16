package com.example.ecole.service;

import com.example.ecole.entity.Note;
import com.example.ecole.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Note> getNotesByEtudiantId(Long etudiantId) {
        return noteRepository.findByEtudiantId(etudiantId);
    }
    
    public List<Note> getNotesByEtudiantIdAndSemestre(Long etudiantId, int semestre) {
        return getNotesByEtudiantId(etudiantId).stream()
            .filter(n -> n.getSemestre() != null && n.getSemestre() == semestre)
            .collect(Collectors.toList());
    }
    
    // Calcul de la moyenne pondérée
    public double calculerMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) return 0.0;
        
        double sommeNotesCoeff = 0.0;
        double sommeCoeff = 0.0;
        
        for (Note note : notes) {
            double coeff = note.getCoefficient() != null ? note.getCoefficient() : 1.0;
            sommeNotesCoeff += note.getValeur() * coeff;
            sommeCoeff += coeff;
        }
        
        return sommeCoeff > 0 ? sommeNotesCoeff / sommeCoeff : 0.0;
    }
    
    // Vérifier si l'étudiant a des notes < 60
    public boolean aDesReprises(List<Note> notes) {
        return notes.stream().anyMatch(n -> n.getValeur() < 60);
    }
    
    // Statut d'un semestre
    public String getStatutSemestre(List<Note> notes) {
        if (notes.isEmpty()) return "Non évalué";
        double moyenne = calculerMoyenne(notes);
        boolean reprises = aDesReprises(notes);
        
        if (moyenne >= 60 && !reprises) return "Admis";
        if (moyenne >= 60 && reprises) return "Admis avec reprises";
        return "Non admis";
    }
    
    // Mention d'un semestre
    public String getMentionSemestre(List<Note> notes) {
        if (notes.isEmpty()) return "Aucune";
        double moyenne = calculerMoyenne(notes);
        if (moyenne >= 80) return "Très bien";
        if (moyenne >= 70) return "Bien";
        if (moyenne >= 60) return "Passable";
        return "Insuffisant";
    }
    
    // Statistiques complètes pour un étudiant
    public Map<String, Object> getStatistiquesEtudiant(Long etudiantId) {
        List<Note> toutesNotes = getNotesByEtudiantId(etudiantId);
        List<Note> notesS1 = getNotesByEtudiantIdAndSemestre(etudiantId, 1);
        List<Note> notesS2 = getNotesByEtudiantIdAndSemestre(etudiantId, 2);
        
        double moyenneS1 = calculerMoyenne(notesS1);
        double moyenneS2 = calculerMoyenne(notesS2);
        double moyenneGenerale = (moyenneS1 + moyenneS2) / 2.0;
        
        String statutGeneral = moyenneGenerale >= 60 ? "Admis" : "Non admis";
        String mentionGenerale = "Aucune";
        if (moyenneGenerale >= 80) mentionGenerale = "Très bien";
        else if (moyenneGenerale >= 70) mentionGenerale = "Bien";
        else if (moyenneGenerale >= 60) mentionGenerale = "Passable";
        else mentionGenerale = "Insuffisant";
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("moyenneS1", moyenneS1);
        stats.put("moyenneS2", moyenneS2);
        stats.put("moyenneGenerale", moyenneGenerale);
        stats.put("statutS1", getStatutSemestre(notesS1));
        stats.put("statutS2", getStatutSemestre(notesS2));
        stats.put("statutGeneral", statutGeneral);
        stats.put("mentionS1", getMentionSemestre(notesS1));
        stats.put("mentionS2", getMentionSemestre(notesS2));
        stats.put("mentionGenerale", mentionGenerale);
        stats.put("reprisesS1", aDesReprises(notesS1));
        stats.put("reprisesS2", aDesReprises(notesS2));
        stats.put("notesS1", notesS1);
        stats.put("notesS2", notesS2);
        
        return stats;
    }
}