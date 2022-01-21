package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentBuilder
{
    private long studentId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String login;
    private String courseId;
    private int yearResult;
    private Section section;

    public void test()
    {
        //StudentBuilder builder = new StudentBuilder().firstName();
    }

    public StudentBuilder studentId(long studentId)
    {
        this.studentId = studentId;
        return this;
    }

    public StudentBuilder firstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public StudentBuilder lastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public StudentBuilder birthDate(Date birthDate)
    {
        this.birthDate = birthDate.toLocalDate();
        return this;
    }

    public StudentBuilder login(String login)
    {
        this.login = login;
        return this;
    }

    public StudentBuilder courseId(String courseId)
    {
        this.courseId = courseId;
        return this;
    }

    public StudentBuilder yearResult(int yearResult)
    {
        this.yearResult = yearResult;
        return this;
    }

    public StudentBuilder section(Section section)
    {
        this.section = section;
        return this;
    }

    public Student build(String firstName, String lastName)
    {
        return new Student(studentId, firstName, lastName, birthDate, section);
    }
}
