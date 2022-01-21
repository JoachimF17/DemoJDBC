package model;

import java.time.LocalDate;

public class Student
{
    private long studentId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Section section;

    public Student()
    {
    }

    public Student(long studentId, String firstName, String lastName, LocalDate birthDate, Section section)
    {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.section = section;
    }

    public static StudentBuilder builder()
    {
        return new StudentBuilder();
    }

    public long getStudentId()
    {
        return studentId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public Section getSection()
    {
        return section;
    }
}
