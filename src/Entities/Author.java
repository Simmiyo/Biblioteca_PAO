package Entities;

import Services.Pair;

import java.util.Date;
import java.util.Objects;

public class Author {
    private String Name;
    private String Nationality;
    private String Movement;
    private Pair<Date, Date> Activity;

    public Author(String name, String nationality, String movement, Pair<Date,Date> activity){
        Name = name;
        Nationality = nationality;
        Movement = movement;
        Activity = activity;
    }

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

    @Override
    public String toString() {
        return "Author{" +
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
        return Objects.equals(getName(), author.getName()) && Objects.equals(getNationality(), author.getNationality()) && Objects.equals(getMovement(), author.getMovement()) && getActivity().equals(author.getActivity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNationality(), getMovement(), getActivity());
    }
}
