package repository;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.*;
import services.Logger;
import services.Triplet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class BookRepository {

    private static ArrayList<Book> Books;

    public static void initBooks() {
        Books = new ArrayList<Book>();
    }

    public void initializeBooksFromCSV() {
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
                    Logger.logOperation("Initialized books from csv file. - FAILED");
                    e.printStackTrace();
                }
                book.setCoverType(data[7].trim());
                return book;
            }).collect(Collectors.toList());
            this.Books.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized books from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Initialized books from csv file. - SUCCESS");
    }

    public Book getBook(Integer id){
        for (Book book: Books) {
            if (book.getId().equals(id))
                return book;
        }
        return null;
    }

    public Integer addBook(Book x) throws Exception {
        AuthorRepository authors = new AuthorRepository();
        boolean existsId = Boolean.FALSE;
        for (Author author: authors.getAuthors()) {
            if (x.getAuthor().getId().equals(author.getId())) {
                existsId = Boolean.TRUE;
                break;
            }
        }
        if (existsId == Boolean.FALSE) {
            Logger.logOperation("New book added in csv file. - FAILED");
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
            Logger.logOperation("New book added in csv file. - FAILED");
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
            Logger.logOperation("New book added in csv file. - FAILED");
            throw new Exception("The section of the book cannot be find in the database!");
        }
        List<Integer> ids = Books.stream().map(Book::getId).sorted(Comparator.comparing(Integer::valueOf)).
                collect(Collectors.toList());
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
            Logger.logOperation("New book added in csv file. - FAILED");
            e.printStackTrace();
        }
        Books.add(x);
        Logger.logOperation("New book added in csv file. - SUCCESS");
        return x.getId();
    }


    public void deleteBook(Integer id) throws Exception {
        for (LibrarySubscriber librarySubscriber: LibrarySubscriberRepository.getLibrarySubscribers()) {
            for (Triplet<Book, Integer, Date> donation: librarySubscriber.getDonation()) {
                if (donation.getFirst().getId().equals(id)) {
                    Logger.logOperation("Book removed from csv file. - FAILED");
                    throw new Exception("This book is referenced in the LibrarySubscriber database. " +
                            "A referenced object cannot be removed");
                }
            }
        }
        for (BookReader bookReader: BookReaderRepository.getBookReaders()) {
            for (Triplet<Book, Date, Boolean> borrowed: bookReader.getBorrowedBooks()) {
                if (borrowed.getFirst().getId().equals(id)) {
                    Logger.logOperation("Book removed from csv file. - FAILED");
                    throw new Exception("This book is referenced in the BookReaders database. " +
                            "A referenced object cannot be removed");
                }
            }
        }
        Books.removeIf(book -> book.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/books.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "ISBN", "Title", "Author", "Publisher", "Section", "Apparition", "CoverType"});
            for (Book book: Books) {
                writer.writeNext(new String[]{book.getId().toString(), book.getISBN(), book.getTitle(),
                        book.getAuthor().getId().toString(), book.getPublisher().getId().toString(),
                        book.getSection().getId().toString(), book.getApparition().toString(), book.getCoverType()});
            }
        } catch (IOException e) {
            Logger.logOperation("Book removed from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Book removed from csv file. - SUCCESS");
    }

    public Book aboutBook(Integer id){
        for (Book book : Books)
            if (book.getId().equals(id))
                return book;
        return null;
    }

    public static ArrayList<Book> getBooks() {
        return Books;
    }

    public void deleteBooks() throws  Exception {
        boolean fullDelete = Boolean.TRUE;
        for (Book book : Books) {
            try {
                deleteBook(book.getId());
            } catch (Exception e) {
                fullDelete = Boolean.FALSE;
            }
        }
        if (fullDelete) {
            Logger.logOperation("Delete all books. - SUCCESS");
        } else {
            Logger.logOperation("Delete all books. - FAILED");
            throw new Exception("Some of the books could not be deleted because they are referenced somewhere else. " +
                    "A referenced object cannot be removed");
        }
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
            Logger.logOperation("Book updated in csv file. - FAILED");
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
            Logger.logOperation("Book updated in csv file. - FAILED");
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
            Logger.logOperation("Book updated in csv file. - FAILED");
            throw new Exception("The section of the book cannot be find in the database!");
        }
        x.setId(id);
        for (int i=0;i<Books.size();i++)
        {
            if(Books.get(i).getId().equals(id)) {
                Books.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/books.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "ISBN", "Title", "Author", "Publisher", "Section", "Apparition", "CoverType"});
            for (Book book: Books) {
                writer.writeNext(new String[]{book.getId().toString(), book.getISBN(), book.getTitle(),
                        book.getAuthor().getId().toString(), book.getPublisher().getId().toString(),
                        book.getSection().getId().toString(), book.getApparition().toString(), book.getCoverType()});
            }
        } catch (IOException e) {
            Logger.logOperation("Book updated in csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Book updated in csv file. - SUCCESS");
    }

    public void sortBooks()
    {
        Collections.sort(Books);
    }
}
