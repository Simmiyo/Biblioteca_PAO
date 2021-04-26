package entities;

import services.Pair;

import java.util.Date;
import java.util.Objects;

public class Author implements Comparable<Author> {
    private Integer Id;
    private String Name;
    private String Nationality;
    private String Movement;
    private Pair<Date, Date> Activity;

    public Author(String name, String nationality, String movement, Pair<Date, Date> activity){
        Name = name;
        Nationality = nationality;
        Movement = movement;
        Activity = activity;
    }

    public Author() {

    }

    public Integer getId() {return Id;}

    public String getName() {
        return Name;
    }

    public String getNationality() {
        return Nationality;
    }

    public String getMovement() {
        return Movement;
    }

    public Pair<Date, Date> getActivity() { return Activity; }

    public void setId(Integer id) {Id = id;}

    public void setName(String name) {
        Name = name;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public void setMovement(String movement) {
        Movement = movement;
    }

    public void setActivity(Pair<Date, Date> activity) { Activity = activity; }

    public int compareTo(Author a)
    {
        return this.Name.compareTo(a.Name);
    }

    @Override
    public String toString() {
        return "Author{" +
                "Id='" + Id.toString() + '\'' +
                "Name='" + Name + '\'' +
                ", Nationality='" + Nationality + '\'' +
                ", Movement='" + Movement + '\'' +
                ", Activity=" + Activity.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(getId(), author.getId())
                && Objects.equals(getName(), author.getName())
                && Objects.equals(getNationality(), author.getNationality())
                && Objects.equals(getMovement(), author.getMovement())
                && getActivity().equals(author.getActivity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getNationality(), getMovement(), getActivity());
    }
}
