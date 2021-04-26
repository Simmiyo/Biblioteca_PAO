package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import entities.Book;
import entities.BookReader;
import entities.LibrarySubscriber;
import services.Triplet;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookReaderRepository {
    ArrayList<BookReader> BookReaders = new ArrayList<BookReader>();


    public BookReaderRepository(){

    }

    private List<Triplet<Book, Date, Boolean>> getBorrowedBooks(String borrowedBooksString, SimpleDateFormat formatter) throws ParseException {
        List<Triplet<Book, Date, Boolean>> borrowedBooks = null;
        String[] arrOfStr = borrowedBooksString.substring(1, borrowedBooksString.length() - 1).split(";");
        BookRepository books = new BookRepository();
        for (String arr : arrOfStr) {
            arr = arr.substring(1, arr.length() - 1);
            String[] splitArr = arr.split(";");
            borrowedBooks.add(new Triplet<>(books.getBook(Integer.parseInt(splitArr[0])), formatter.parse(splitArr[1]),
                    Boolean.parseBoolean(splitArr[2])));
        }
        return borrowedBooks;
    }


    public void InitializeBookReadersFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/library_subscribers.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
            LibrarySubscriberRepository librarySubscriberRepository = new LibrarySubscriberRepository();

            List<BookReader> csvObjectList = csvReader.readAll().stream().map(data-> {
                LibrarySubscriber librarySubscriber = librarySubscriberRepository.getLibrarySubscriber(Integer.parseInt(data[0].trim()););
                BookReader bookReader = new BookReader(librarySubscriber);
                bookReader.setAddress(data[1].trim());
                try {
                    bookReader.setBorrowedBooks(getBorrowedBooks(data[2].trim(), frmt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bookReader.setPenaltyPoints(Integer.parseInt(data[3].trim()));
                bookReader.setFidelityPoints(Integer.parseInt(data[4].trim()));
                return librarySubscriber;
            }).collect(Collectors.toList());
            this.BookReaders.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public BookReader getBookReader(Integer id){
        for (BookReader bookReader: BookReaders) {
            if (bookReader.getId().equals(id))
                return bookReader;
        }
        return null;
    }

    public void addBookReader(BookReader x){
        this.BookReaders.add(x);
    }
    public void deleteBookReader(BookReader x){
        this.BookReaders.remove(x);
    }

    public BookReader aboutBookReader(String phone){
        for (int i=0;i<this.BookReaders.size();i++)
            if(this.BookReaders.get(i).getPhoneNumber()==phone)
                return this.BookReaders.get(i);
        return null;
    }

    public ArrayList<BookReader> getBookReaders() {
        return BookReaders;
    }

    public void deleteBookReaders(){
        while (this.BookReaders.isEmpty()!=true)
            this.BookReaders.remove(0);
    }

    public void updateBookReader(String name, BookReader x){
        for (int i=0;i<this.BookReaders.size();i++)
        {
            if (this.BookReaders.get(i).getName() == name) {
                this.BookReaders.set(i, x);
                break;
            }
        }
    }

    public void sortBookReaders()
    {
        getBookReaders().sort(Comparator.comparing(BookReader::getName));
    }
}
