package model;

public class Section
{
    private long sectionId;
    private String sectionName;
    private Student delegate;

    public Section()
    {
    }

    public Section(long sectionId, String sectionName, Student delegate)
    {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.delegate = delegate;
    }

    public long getSectionId()
    {
        return sectionId;
    }

    public void setSectionId(long sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }

    public Student getDelegate()
    {
        return delegate;
    }

    public void setDelegate(Student delegate)
    {
        this.delegate = delegate;
    }
}
