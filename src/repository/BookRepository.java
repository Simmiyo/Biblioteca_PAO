package repository;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Book;
import entities.Publisher;
import entities.Section;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class BookRepository {
    ArrayList<Book> Books = new ArrayList<Book>();

    public BookRepository(){

    }

    public void InitializeBooksFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/books.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
            AuthorRepository authors = new AuthorRepository();
            PublisherRepository publishers = new PublisherRepository();
            SectionRepository sections = new SectionRepository();

            List<Book> csvObjectList = csvReader.readAll().stream().map(data-> {
                Book book = new Book();
                book.setId(Integer.parseInt(data[0].trim()));
                book.setISBN(data[1].trim());
                book.setTitle(data[2].trim());
                book.setAuthor(authors.getAuthor(Integer.parseInt(data[3].trim())));
                book.setPublisher(publishers.getPublisher(Integer.parseInt(data[4].trim())));
                book.setSection(sections.getSection(Integer.parseInt(data[5].trim())));
                try {
                    book.setApparition(frmt.parse(data[6].trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                book.setCoverType(data[7].trim());
                return book;
            }).collect(Collectors.toList());
            this.Books.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Book getBook(Integer id){
        for (Book book: Books) {
            if (book.getId().equals(id))
                return book;
        }
        return null;
    }

    public void addBook(Book x) throws Exception {
        AuthorRepository authors = new AuthorRepository();
        boolean existsId = Boolean.FALSE;
        for (Author author: authors.getAuthors()) {
            if (x.getAuthor().getId().equals(author.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The author of the book cannot be find in the database!");
        }
        PublisherRepository publishers = new PublisherRepository();
        existsId = Boolean.FALSE;
        for (Publisher publisher: publishers.getPublishers()) {
            if (x.getPublisher().getId().equals(publisher.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The publisher of the book cannot be find in the database!");
        }
        SectionRepository sections = new SectionRepository();
        existsId = Boolean.FALSE;
        for (Section section: sections.getSections()) {
            if (x.getSection().getId().equals(section.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The section of the book cannot be find in the database!");
        }
        List<Integer> ids = Books.stream().map(Book::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
                x.setId(i);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/books.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getISBN(), x.getTitle(),
                    x.getAuthor().getId().toString(), x.getPublisher().getId().toString(),
                    x.getSection().getId().toString(), x.getApparition().toString(),x.getCoverType()});
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.Books.add(x);
    }


    public void deleteBook(Book x){
        this.Books.remove(x);
    }

    public Book aboutBook(Integer id){
        for (Book book : this.Books)
            if (book.getId().equals(id))
                return book;
        return null;
    }

    public ArrayList<Book> getBooks() {
        return Books;
    }

    public void deleteBooks(){
        while (this.Books.isEmpty()!=true)
            this.Books.remove(0);
    }

    public void updateBook(Integer id, Book x) throws Exception {
        AuthorRepository authors = new AuthorRepository();
        boolean existsId = Boolean.FALSE;
        for (Author author: authors.getAuthors()) {
            if (x.getAuthor().getId().equals(author.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The author of the book cannot be find in the database!");
        }
        PublisherRepository publishers = new PublisherRepository();
        existsId = Boolean.FALSE;
        for (Publisher publisher: publishers.getPublishers()) {
            if (x.getPublisher().getId().equals(publisher.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The publisher of the book cannot be find in the database!");
        }
        SectionRepository sections = new SectionRepository();
        existsId = Boolean.FALSE;
        for (Section section: sections.getSections()) {
            if (x.getSection().getId().equals(section.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            throw new Exception("The section of the book cannot be find in the database!");
        }
        x.setId(id);
        for (int i=0;i<this.Books.size();i++)
        {
            if(this.Books.get(i).getId().equals(id)) {
                this.Books.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/authors.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "ISBN", "Title", "Author", "Publisher", "Section", "Apparition", "CoverType"});
            for (Book book: Books) {
                writer.writeNext(new String[]{x.getId().toString(), x.getISBN(), x.getTitle(),
                        x.getAuthor().getId().toString(), x.getPublisher().getId().toString(),
                        x.getSection().getId().toString(), x.getApparition().toString(),x.getCoverType()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortBooks()
    {
        Collections.sort(Books);
    }
}
