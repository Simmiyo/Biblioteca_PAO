package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Book;
import entities.BookReader;
import entities.LibrarySubscriber;
import services.Logger;
import services.Triplet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookReaderRepository {

    private static ArrayList<BookReader> BookReaders;

    public static void initBookReaders() {
        BookReaders = new ArrayList<BookReader>();
    }

    private ArrayList<Triplet<Book, Date, Boolean>> getBorrowedBooks(String borrowedBooksString, SimpleDateFormat formatter) throws ParseException {
        ArrayList<Triplet<Book, Date, Boolean>> borrowedBooks = new ArrayList<Triplet<Book, Date, Boolean>>();
        String[] arrOfStr = borrowedBooksString.substring(1, borrowedBooksString.length() - 1).split(",");
        BookRepository books = new BookRepository();
        for (String arr : arrOfStr) {
            arr = arr.substring(1, arr.length() - 1);
            String[] splitArr = arr.split(";");
            borrowedBooks.add(new Triplet<>(books.getBook(Integer.parseInt(splitArr[0])), formatter.parse(splitArr[1]),
                    Boolean.parseBoolean(splitArr[2])));
        }
        return borrowedBooks;
    }

    private String formatBorrowedBooks(ArrayList<Triplet<Book, Date, Boolean>> borrowedBooks) {
        String borrowedBooksString = "";
        if (!borrowedBooks.isEmpty()) {
            StringBuilder borrowedBooksBuilder = new StringBuilder();
            borrowedBooksBuilder.append("\"[");
            for (Triplet<Book, Date, Boolean> bor: borrowedBooks) {
                borrowedBooksBuilder.append("(").append(bor.getFirst().getId().toString()).
                        append(";").append(bor.getSecond().toString()).append(";").append(bor.getThird().toString()).append(");");
            }
            borrowedBooksBuilder.deleteCharAt(borrowedBooksBuilder.length() - 1);
            borrowedBooksBuilder.append("]\"");
            borrowedBooksString = borrowedBooksBuilder.toString();
        }
        return borrowedBooksString;
    }


    public void initializeBookReadersFromCSV() throws IOException, CsvException {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/book_readers.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
            LibrarySubscriberRepository librarySubscriberRepository = new LibrarySubscriberRepository();

            List<BookReader> csvObjectList = csvReader.readAll().stream().map(data-> {
                LibrarySubscriber librarySubscriber = librarySubscriberRepository.getLibrarySubscriber(Integer.parseInt(data[0].trim()));
                BookReader bookReader = new BookReader(librarySubscriber);
                bookReader.setAddress(data[1].trim());
                try {
                    bookReader.setBorrowedBooks(getBorrowedBooks(data[2].trim(), frmt));
                } catch (ParseException e) {
                    Logger.logOperation("Initialized book readers from csv file. - FAILED");
                    e.printStackTrace();
                }
                bookReader.setPenaltyPoints(Integer.parseInt(data[3].trim()));
                bookReader.setFidelityPoints(Integer.parseInt(data[4].trim()));
                return bookReader;
            }).collect(Collectors.toList());
            BookReaders.addAll(csvObjectList);
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized book readers from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Initialized book readers from csv file. - SUCCESS");
    }

    public BookReader getBookReader(Integer id){
        for (BookReader bookReader: BookReaders) {
            if (bookReader.getId().equals(id))
                return bookReader;
        }
        return null;
    }

    public Integer addBookReader(BookReader x) throws IOException {
        LibrarySubscriberRepository librarySubscriberRepository = new LibrarySubscriberRepository();
        x.setId(librarySubscriberRepository.addLibrarySubscriber(new LibrarySubscriber(x.getName(), x.getPhoneNumber(),
                x.getMembershipValidity(), x.getStudyLevel(), x.getDonation())));
        try {
            FileWriter filewriter = new FileWriter("data/book_readers.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getAddress(), formatBorrowedBooks(x.getBorrowedBooks()),
                    x.getPenaltyPoints().toString(), x.getFidelityPoints().toString()});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("New book reader added in csv file. - FAILED");
            throw e;
        }
        BookReaders.add(x);
        Logger.logOperation("New book reader added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteBookReader(Integer id) throws IOException {
        BookReaders.removeIf(bookReader -> bookReader.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/book_readers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"SubscriberId", "Address", "BorrowedBooks", "PenaltyPoints", "FidelityPoints"});
            for (BookReader bookReader: BookReaders) {
                writer.writeNext(new String[]{bookReader.getId().toString(), bookReader.getAddress(), formatBorrowedBooks(bookReader.getBorrowedBooks()),
                        bookReader.getPenaltyPoints().toString(), bookReader.getFidelityPoints().toString()});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Book reader removed from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Book reader removed from csv file. - SUCCESS");
    }

    public BookReader aboutBookReader(Integer id){
        for (BookReader bookReader : BookReaders)
            if (bookReader.getId().equals(id))
                return bookReader;
        return null;
    }

    public static ArrayList<BookReader> getBookReaders() {
        return BookReaders;
    }

    public void deleteBookReaders() throws IOException {
        BookReaders.clear();
        try {
            FileWriter filewriter = new FileWriter("data/book_readers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"SubscriberId", "Address", "BorrowedBooks", "PenaltyPoints", "FidelityPoints"});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Deleted all book readers. - FAILED");
            throw e;
        }
        Logger.logOperation("Deleted all book readers. - SUCCESS");
    }

    public void updateBookReader(Integer id, BookReader x) throws IOException {
        x.setId(id);
        LibrarySubscriberRepository librarySubscriberRepository = new LibrarySubscriberRepository();
        for (int i=0;i<BookReaders.size();i++)
        {
            if(BookReaders.get(i).getId().equals(id)) {
                BookReaders.set(i, x);
                librarySubscriberRepository.updateLibrarySubscriber(id, new LibrarySubscriber(x.getName(),
                        x.getPhoneNumber(), x.getMembershipValidity(), x.getStudyLevel(), x.getDonation()));
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/book_readers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"SubscriberId", "Address", "BorrowedBooks", "PenaltyPoints", "FidelityPoints"});
            for (BookReader bookReader: BookReaders) {
                writer.writeNext(new String[]{bookReader.getId().toString(), bookReader.getAddress(), formatBorrowedBooks(bookReader.getBorrowedBooks()),
                        bookReader.getPenaltyPoints().toString(), bookReader.getFidelityPoints().toString()});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Book reader updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Book reader updated in csv file. - SUCCESS");
    }

    public void sortBookReaders()
    {
        getBookReaders().sort(Comparator.comparing(BookReader::getName));
    }
}
