import data_access.ProfessorDAOImpl;
import data_access.SectionDAO;
import model.Professor;
import model.Section;

import java.util.List;

public class Program
{
    public static void main(String[] args)
    {
        /*get*/

        /*SectionDAO sdao = new SectionDAO();
        Section s = sdao.getOne(7777);
        System.out.println("--------SECTION--------");
        System.out.println(s.getSectionId());
        System.out.println(s.getSectionName());
        System.out.printf("%s %s%n", s.getDelegate().getFirstName(), s.getDelegate().getLastName());*/

        ProfessorDAOImpl pdao = new ProfessorDAOImpl();

        System.out.printf("Split effectue %s fois%n", pdao.separateProfInSections());
        List<Professor> liste = pdao.getAll();

        liste.forEach(System.out::println);
    }
}
