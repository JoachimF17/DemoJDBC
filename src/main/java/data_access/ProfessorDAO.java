package data_access;

import model.Professor;

import java.util.List;

public interface ProfessorDAO
{
    // CREATE
    boolean insert(Professor toInsert);

    // READ
    List<Professor> getAll();
    /**
     * BONUS 1
     * Recupere les prof dont la moyenne des yearResult
     * des etudiants dans la meme section est supérieur à <code>result</code>
     * @param result moyenne minimum des year_result
     * @return liste de prof
     */
    List<Professor> getAllWithAverageResultBiggerThan(int result);
    Professor getOne(int id);

    // UPDATE
    boolean update(int id, Professor newValues);

    // DELETE
    boolean delete(int id);

    // OTHER

    /**
     * BONUS 2
     * Crée une nouvelle section portant le nom d'un prof qui partage sa section avec un autre
     * puis met ce prof dans cette section
     * @return le nombre de fois que l'opération a été faite
     */
    int separateProfInSections();
}
