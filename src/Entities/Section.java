package Entities;

import java.util.Objects;

public class Section {
    private String Label;
    private Character Bookshelf; //for physical identification of sections

    public Section(String label, Character shelf){
        Label = label;
        Bookshelf = shelf;
    }

    public String getLabel() { return Label; }

    public Character getBookshelf() { return Bookshelf; }

    public void setLabel(String label) { Label = label; }

    public void setBookshelf(Character bookshelf) { Bookshelf = bookshelf; }

    @Override
    public String toString() {
        return "Section{" +
                "Label='" + Label + '\'' +
                ", Bookshelf=" + Bookshelf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(getLabel(), section.getLabel()) && Objects.equals(getBookshelf(), section.getBookshelf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel(), getBookshelf());
    }
}
