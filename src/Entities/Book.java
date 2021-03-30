package Entities;

import java.util.Date;
import java.util.Objects;

public class Book {
    private String ISBN;
    private String Title;
    private Author Author;
    private Publisher Publisher;
    private Section Section;
    private Date Apparition;
    private String CoverType;

    public Book(String isbn, String title, Author author, Publisher publisher, Section section, Date date, String cover){
        ISBN = isbn;
        Title = title;
        Author = author;
        Publisher = publisher;
        Section = section;
        Apparition = date;
        CoverType = cover;
    }

    public String getISBN() { return ISBN; }

    public String getTitle() { return Title; }

    public Entities.Author getAuthor() { return Author; }

    public Entities.Publisher getPublisher() { return Publisher; }

    public Entities.Section getSection() { return Section; }

    public Date getApparition() { return Apparition; }

    public String getCoverType() { return CoverType; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public void setTitle(String title) { Title = title; }

    public void setAuthor(Entities.Author author) { Author = author; }

    public void setPublisher(Entities.Publisher publisher) { Publisher = publisher; }

    public void setSection(Entities.Section section) { Section = section; }

    public void setApparition(Date apparition) { Apparition = apparition; }

    public void setCoverType(String coverType) { CoverType = coverType; }

    @Override
    public String toString() {
        return "Book{" +
                "Title='" + Title + '\'' +
                ", Author=" + Author +
                ", Publisher=" + Publisher +
                ", Section=" + Section +
                ", Apparition=" + Apparition +
                ", CoverType='" + CoverType + '\'' +
                "ISBN='" + ISBN + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getAuthor(), book.getAuthor()) && Objects.equals(getPublisher(), book.getPublisher()) && Objects.equals(getSection(), book.getSection()) && Objects.equals(getApparition(), book.getApparition()) && Objects.equals(getCoverType(), book.getCoverType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getISBN(), getTitle(), getAuthor(), getPublisher(), getSection(), getApparition(), getCoverType());
    }
}
