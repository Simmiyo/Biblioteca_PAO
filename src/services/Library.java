package services;

import com.opencsv.exceptions.CsvException;
import entities.*;
import repository.*;

import java.io.IOException;
import java.util.*;
import java.time.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Library {

    private Integer maxDaysBorrow;
    private Map<Integer, String> schedule;
    private Map<Book, Long> specimens;
    private Map<Book, Boolean> availability;
    private AuthorRepository authorsRepository;
    private BookRepository booksRepository;
    private BookReaderRepository bookReadersRepository;
    private LibrarySubscriberRepository librarySubscribersRepository;
    private LibrarianRepository librariansRepository;
    private PublisherRepository publishersRepository;
    private SectionRepository sectionsRepository;
    private TransactionRepository transactionsRepository;

    private Library() {
        AuthorRepository.initAuthors();
        authorsRepository = new AuthorRepository();
        PublisherRepository.initPublishers();
        publishersRepository = new PublisherRepository();
        SectionRepository.initSections();
        sectionsRepository = new SectionRepository();
        LibrarianRepository.initLibrarians();
        librariansRepository = new LibrarianRepository();
        BookRepository.initBooks();
        booksRepository = new BookRepository();
        LibrarySubscriberRepository.initLibrarySubscribers();
        librarySubscribersRepository = new LibrarySubscriberRepository();
        BookReaderRepository.initBookReaders();
        bookReadersRepository = new BookReaderRepository();
        TransactionRepository.initTransactions();
        transactionsRepository = new TransactionRepository();
    }

    public void initializeMainRepositoryFromCSV() throws IOException, CsvException {
        authorsRepository.initializeAuthorsFromCSV();
        publishersRepository.initializePublishersFromCSV();
        sectionsRepository.initializeSectionsFromCSV();
        librariansRepository.initializeLibrariansFromCSV();
        booksRepository.initializeBooksFromCSV();
        librarySubscribersRepository.initializeLibrarySubscribersFromCSV();
        bookReadersRepository.initializeBookReadersFromCSV();
        transactionsRepository.initializeTransactionsFromCSV();
    }

    private static class SingletonLibrary{
        private static final Library INSTANCE = new Library();
    }

    public static Library getInstance() {
        return SingletonLibrary.INSTANCE;
    }

    public Integer getMaxDaysBorrow() {
        return maxDaysBorrow;
    }

    public Map<Integer, String> getSchedule() {
        return schedule;
    }

    public ArrayList<Author> getAuthors() { return AuthorRepository.getAuthors(); }

    public ArrayList<Book> getBooks() { return BookRepository.getBooks(); }

    public ArrayList<BookReader> getReaders() { return BookReaderRepository.getBookReaders(); }

    public ArrayList<LibrarySubscriber> getSubscribers() {
        return LibrarySubscriberRepository.getLibrarySubscribers();
    }

    public ArrayList<Librarian> getLibrarians() {
        return LibrarianRepository.getLibrarians();
    }

    public ArrayList<Publisher> getPublishers() { return PublisherRepository.getPublishers(); }

    public ArrayList<Section> getSections() {return SectionRepository.getSections(); }

    public ArrayList<Transaction> getTransactions() {
        return TransactionRepository.getTransactions();
    }

    public Map<Book, Long> getAllSpecimens()
    {
        return BookRepository.getBooks().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public void setSpecimens() {
        specimens = getAllSpecimens();
    }

    private Map<Book, Boolean> getSpecimenAvailability()
    {
        Map<Book, Long> allSpecimens = getAllSpecimens();
        Map<Book, Boolean> allSpecimensAvailability = new HashMap<>();
        for (Map.Entry<Book, Long> entry : allSpecimens.entrySet())
        {
            if (entry.getValue() > 0) {
                allSpecimensAvailability.put(entry.getKey(), true);
            }

        }
        return allSpecimensAvailability;
    }

    public void setAvailability() {
        availability = getSpecimenAvailability();
    }

    public void setMaxDaysBorrow(Integer maxDaysBorrow) {
        this.maxDaysBorrow = maxDaysBorrow;
    }
    public void setSchedule(Map<Integer, String> schedule) {
        this.schedule = schedule;
    }

    public Integer addAuthor(Author a) throws IOException { return authorsRepository.addAuthor(a); }
    public Integer addBook(Book b) throws Exception { return booksRepository.addBook(b); }
    public Integer addPublisher(Publisher p) throws IOException { return publishersRepository.addPublisher(p); }
    public Integer addSection(Section s) throws IOException { return sectionsRepository.addSection(s); }
    public Integer addTransaction(Transaction t) throws IOException { return transactionsRepository.addTransaction(t); }
    public Integer addReader(BookReader r) throws IOException { return bookReadersRepository.addBookReader(r); }
    public Integer addSubscriber(LibrarySubscriber ls) throws IOException { return librarySubscribersRepository.addLibrarySubscriber(ls); }
    public Integer addLibrarian(Librarian l) throws IOException { return librariansRepository.addLibrarian(l); }

    public void deleteAuthor(Integer id) throws Exception { authorsRepository.deleteAuthor(id); }
    public void deleteBook(Integer id) throws Exception { booksRepository.deleteBook(id); }
    public void deletePublisher(Integer id) throws Exception { publishersRepository.deletePublisher(id); }
    public void deleteSection(Integer id) throws Exception { sectionsRepository.deleteSection(id); }
    public void deleteTransaction(Integer id) throws IOException { transactionsRepository.deleteTransaction(id); }
    public void deleteReader(Integer id) throws IOException { bookReadersRepository.deleteBookReader(id); }
    public void deleteSubscriber(Integer id) throws Exception { librarySubscribersRepository.deleteLibrarySubscriber(id); }
    public void deleteLibrarian(Integer id) throws IOException { librariansRepository.deleteLibrarian(id); }

    public void updateAuthor(Integer id, Author a) throws IOException { authorsRepository.updateAuthor(id, a); }
    public void updateBook(Integer id, Book b) throws Exception { booksRepository.updateBook(id, b); }
    public void updatePublisher(Integer id, Publisher p) throws IOException { publishersRepository.updatePublisher(id, p); }
    public void updateSection(Integer id, Section s) throws IOException { sectionsRepository.updateSection(id, s); }
    public void updateTransaction(Integer id, Transaction t) throws IOException { transactionsRepository.updateTransaction(id, t); }
    public void updateReader(Integer id, BookReader r) throws IOException { bookReadersRepository.updateBookReader(id, r); }
    public void updateSubscriber(Integer id, LibrarySubscriber ls) throws IOException { librarySubscribersRepository.updateLibrarySubscriber(id, ls); }
    public void updateLibrarian(Integer id, Librarian l) throws IOException { librariansRepository.updateLibrarian(id, l); }

    public void deleteAuthors() throws Exception { authorsRepository.deleteAuthors();}
    public void deleteBooks() throws Exception { booksRepository.deleteBooks();}
    public void deletePublishers() throws Exception { publishersRepository.deletePublishers();}
    public void deleteSections() throws Exception { sectionsRepository.deleteSections();}
    public void deleteTransactions() throws Exception { transactionsRepository.deleteTransactions();}
    public void deleteReaders() throws Exception { bookReadersRepository.deleteBookReaders();}
    public void deleteSubscribers() throws Exception { librarySubscribersRepository.deleteLibrarySubscribers();}
    public void deleteLibrarians() throws Exception { librariansRepository.deleteLibrarians();}


    @Override
    public String toString() {
        return "Library{" +
                "MaxDaysBorrow=" + maxDaysBorrow +
                ", Schedule=" + schedule +
                ", Specimens=" + specimens +
                ", Availability=" + availability +
                '}';
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void calculatePenalties(BookReader person)
    {
        Integer penalties = 0;
        for(Triplet<Book, Date, Boolean> infoBook : person.getBorrowedBooks()){
            LocalDate startDate = convertToLocalDateViaInstant(infoBook.getSecond());
            LocalDate dueDate = startDate.plusDays(maxDaysBorrow);
            Boolean bookStatus = infoBook.getThird(); // bookstatus = false daca cititorul nu a returnat cartea inca
            Integer daysPassed = - dueDate.compareTo(LocalDate.now());
            if(daysPassed > 0 && !bookStatus){
                penalties += daysPassed * 10;
            }
        }
        person.setPenaltyPoints(penalties);
    }

    public void calculateFidelity(BookReader person)
    {
        Integer fidelityPoints = 0;
        if(person.getPenaltyPoints() == 0) {
             double points = person.getDonation().stream().collect(Collectors.summingInt(Triplet::getSecond))
                     * 0.8 + person.getBorrowedBooks().size() * 0.2;
             fidelityPoints = (int)(points);
        }
        person.setFidelityPoints(fidelityPoints);
    }

    public boolean isAvailable(Book book) {

        return availability.get(book);
    }

}
