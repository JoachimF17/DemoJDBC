package data_access;

import com.mysql.cj.jdbc.MysqlDataSource;
import model.Section;
import model.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO
{
    public Section getOne(int sectionId)
    {
        String query = "SELECT se.section_id, se.section_name, st.first_name, st.last_name, st.birth_date, " +
                "st.student_id FROM section se LEFT JOIN student st ON se.delegate_id = st.student_id WHERE se.section_id = "
                +sectionId;

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

    public List<Section> getAll()
    {

        String query = "SELECT se.section_id, se.section_name, st.first_name, st.last_name, st.birth_date,"+
                    "st.student_id FROM section LEFT JOIN student ON section.delegate_id = student.student_id";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            List<Section> liste = new ArrayList<>();

            while(rs.next())
                liste.add(extract(rs));

            return liste;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private Section extract(ResultSet rs) throws SQLException
    {
        Section section = new Section();

        section.setSectionId(rs.getInt("section_id"));
        section.setSectionName(rs.getString("section_name"));

        long stId = rs.getLong("student_id");

        Student st = null;

        if(stId != 0)
        {
            st = Student.builder().section(section).birthDate(rs.getDate("birth_date"))
                    .studentId(stId)
                    .build(rs.getString("first_name"), rs.getString("last_name"));
        }

        section.setDelegate(st);

        return section;
    }

    public void displayAll()
    {
        String query = "SELECT * FROM section";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            while(rs.next())
            {
                int sectionId = rs.getInt("section_id");
                String sectionName = rs.getString("section_name");
                int delegateId = rs.getInt("delegate_id");

                System.out.printf("%s - %s - %s%n", sectionId, sectionName, delegateId);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean insert(Section toInsert)
    {
        String query = "INSERT INTO section(section_id, section_name) VALUES (?, ?)";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setLong(1, toInsert.getSectionId());
            stmt.setString(2, toInsert.getSectionName());
            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void insertByProcedure(Section toInsert)
    {
        String query = "CALL SP_INSERT_SECTION(?, ?, ?)";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setLong(1, toInsert.getSectionId());
            stmt.setString(2, toInsert.getSectionName());

            if(toInsert.getDelegate() != null)
                stmt.setLong(3, toInsert.getDelegate().getStudentId());
            else
                stmt.setNull(3, Types.INTEGER);

            if(stmt.execute())
                stmt.getResultSet();
            else
                stmt.getUpdateCount();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void insertByProcedureVDeux(Section toInsert)
    {
        String query = "CALL SP_INSERT_SECTION(?, ?, ?, ?)";

        try(Connection co = ConnectionFactory.getConnection();
            CallableStatement stmt = co.prepareCall(query))
        {
            stmt.setLong(1, toInsert.getSectionId());
            stmt.setString(2, toInsert.getSectionName());

            if(toInsert.getDelegate() != null)
                stmt.setLong(3, toInsert.getDelegate().getStudentId());
            else
                stmt.setNull(3, Types.INTEGER);

            stmt.execute();

            int a = stmt.getInt("result");
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int deleteByName(String sectionName)
    {
        String query = "DELETE FROM section WHERE section_name = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setString(1, sectionName);

            return stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public int deleteByInitial(char initial)
    {
        if((initial < 'a' || initial > 'z') && (initial < 'A' || initial > 'Z'))
            throw new IllegalArgumentException("pas un caractere valide");

        String query = "DELETE FROM section WHERE section_name LIKE ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setString(1, initial+"%");

            return stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean deleteById(long sectionId)
    {
        String query = "DELETE FROM section WHERE section_id = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setLong(1, sectionId);

            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateById(long sectionId, String newName, long newDelegateId)
    {
        String query = "UPDATE section SET section_name = ?, delegate_id = ? WHERE section_id = ?";

        try(Connection co = ConnectionFactory.getConnection();
            PreparedStatement stmt = co.prepareStatement(query))
        {
            stmt.setString(1, newName);
            stmt.setLong(2, newDelegateId);
            stmt.setLong(3, sectionId);

            return 1 == stmt.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public int updateBizarre()
    {
        String query = "SELECT * FROM student";

        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(query))
        {

            int cptLignesModifiees = 0;


            while(rs.next())
            {
                Date birthDate = rs.getDate("birth_date");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                if(birthDate.getYear() >= 1970)
                {
                    rs.updateString("login", (firstName + "_" + lastName).toLowerCase());
                    rs.updateRow();
                    cptLignesModifiees++;
                }
            }

            return cptLignesModifiees;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public static DataSource getDataSource()
    {
        MysqlDataSource ds = new MysqlDataSource();

        return null;
    }
}
