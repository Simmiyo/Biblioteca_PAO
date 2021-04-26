package entities;

import java.util.Objects;

public class Section implements Comparable<Section> {
    private Integer Id;
    private String Label;
    private Character Bookshelf; //for physical identification of sections

    public Section(String label, Character shelf){
        Label = label;
        Bookshelf = shelf;
    }

    public Section(){

    }

    public Integer getId() {return Id;}

    public String getLabel() { return Label; }

    public Character getBookshelf() { return Bookshelf; }

    public void setId(Integer id) {Id = id;}

    public void setLabel(String label) { Label = label; }

    public void setBookshelf(Character bookshelf) { Bookshelf = bookshelf; }

    public int compareTo(Section s)
    {
        return this.Label.compareTo(s.Label);
    }

    @Override
    public String toString() {
        return "Section{" +
                "Id='" + Id.toString() + '\'' +
                "Label='" + Label + '\'' +
                ", Bookshelf=" + Bookshelf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(getId(), section.getId())
                && Objects.equals(getLabel(), section.getLabel())
                && Objects.equals(getBookshelf(), section.getBookshelf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLabel(), getBookshelf());
    }
}
