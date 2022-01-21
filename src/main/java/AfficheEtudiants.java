import data_access.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class AfficheEtudiants
{
    //trouver tous les etudiants rentres au clavier
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int input;
        String query;

        System.out.print("Entrez l'id de la section : ");
        input = sc.nextInt();

        query = "SELECT * FROM student JOIN section ON student.section_id = section.section_id WHERE student.section_id = "+input;
        try(Connection co = ConnectionFactory.getConnection();
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query);)
        {
            while(rs.next())
            {
                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String sectionName = rs.getString("section_name");
                Date birthDate = rs.getDate("birth_date");

                System.out.printf("%s - %s - %s - %s - %s%n", studentId, lastName, firstName, sectionName, birthDate);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
