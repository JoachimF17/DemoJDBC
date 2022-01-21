package data_access;

import model.Professor;
import model.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ProfessorDAOImpl implements ProfessorDAO
{
    @Override public boolean insert(Professor toInsert)
    {
        String query= "INSERT INTO professor(professor_id, professor_name, professor_surname, section_id, professor_wage) VALUES (?, ?, ?, ?, ?)";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setInt(1, toInsert.getId());
            stmt.setString(2, toInsert.getLastname());
            stmt.setString(3, toInsert.getFirstname());
            stmt.setInt(4, toInsert.getSection().getId());
            stmt.setInt(5, toInsert.getWage());

            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override public List<Professor> getAll()
    {
        String query = "SELECT p.professor_id, p.professor_name, p.professor_surname, p.section_id, p.professor_wage, s.section_name " +
                        "FROM professor p LEFT JOIN section s ON p.section_id = s.section_id;";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            List<Professor> liste = new ArrayList<>();

            while(rs.next())
                liste.add(extract(rs));

            return liste;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override public List<Professor> getAllWithAverageResultBiggerThan(int result)
    {
        String query = "SELECT p.professor_id, p.professor_name, p.professor_surname, p.section_id, p.professor_wage, s.section_name " +
                "FROM professor p LEFT JOIN section s ON p.section_id = s.section_id;";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            List<Professor> liste = new ArrayList<>();
            double moy;

            while(rs.next())
            {
                moy = moyenneNotes(rs.getInt("section_id"));

                if(moy > result)
                    liste.add(extract(rs));
            }

            return liste;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override public Professor getOne(int id)
    {
        String query = "SELECT p.professor_id, p.professor_name, p.professor_surname, p.section_id, p.professor_wage, s.section_name " +
                "FROM professor p LEFT JOIN section s ON p.section_id = s.section_id WHERE p.professor_id = "+id;

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            if(rs.next())
                return extract(rs);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override public boolean update(int id, Professor newValues)
    {
        String query = "UPDATE professor SET professor_name = ?, professor_surname = ?, section_id = ?, professor_wage = ? WHERE professor_id = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setString(1, newValues.getLastname());
            stmt.setString(2, newValues.getFirstname());
            stmt.setLong(3, newValues.getSection().getId());
            stmt.setInt(4, newValues.getWage());
            stmt.setLong(5, id);

            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override public boolean delete(int id)
    {
        String query = "DELETE FROM professor WHERE professor_id = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setLong(1, id);

            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override public int separateProfInSections()
    {
        int cpt = 0;

        String query = "SELECT professor_id, professor_name, professor_surname, section_id FROM professor";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            Set<Integer> listeId = new TreeSet<>();

            while(rs.next())
            {
                if(listeId.contains(rs.getInt("section_id")))
                {
                    long newId = createSection(rs);
                    addSectionToProfessor(newId, rs.getInt("professor_id"));
                    cpt++;
                }
                else
                    listeId.add(rs.getInt("section_id"));
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return cpt;
    }

    private void addSectionToProfessor(long newId, int pId)
    {
        String query = "UPDATE professor SET section_id = ? WHERE professor_id = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setLong(1, newId);
            stmt.setInt(2, pId);

            stmt.execute();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private long createSection(ResultSet rs) throws SQLException
    {
        Section s = new Section();

        s.setDelegate(null);
        s.setSectionName((rs.getString("professor_name")+rs.getString("professor_surname")).toLowerCase());
        s.setSectionId(rs.getInt("section_id")+5);

        SectionDAO sdao = new SectionDAO();
        sdao.insert(s);

        return s.getSectionId();
    }

    private Professor extract(ResultSet rs) throws SQLException
    {
        Professor p = new Professor();

        p.setId(rs.getInt("professor_id"));
        p.setLastname(rs.getString("professor_name"));
        p.setFirstname(rs.getString("professor_surname"));
        p.setWage(rs.getInt("professor_wage"));

        long seId = rs.getLong("section_id");

        Professor.Section se = null;

        if(seId != 0)
        {
            se = new Professor.Section(rs.getInt("section_id"), rs.getString("section_name"));
        }

        p.setSection(se);

        return p;
    }

    private double moyenneNotes(int id)
    {
        String query = "SELECT year_result FROM student WHERE section_id = "+id;

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            int cpt = 0;
            double total = 0;

            while(rs.next())
            {
                total+=rs.getInt(1);
                cpt++;
            }

            return total;

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }
}
