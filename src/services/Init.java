package services;

import entities.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import repository.*;

public class Init {
    private AuthorRepository AuthorsRepo = new AuthorRepository();
    private PublisherRepository PublishersRepo = new PublisherRepository();
    private LibrarianRepository LibrariansRepo = new LibrarianRepository();
    private BookRepository BooksRepo = new BookRepository();
    private LibrarySubscriberRepository LibrarySubscribersRepo = new LibrarySubscriberRepository();
    private BookReaderRepository BookReadersRepo = new BookReaderRepository();
    private TransactionRepository TransactionsRepo = new TransactionRepository();
    private SectionRepository SectionsRepo = new SectionRepository();

    private Map<Integer, String> Schedule;

    public Init() throws ParseException {
        String date1 = "20-01-1950";
        String date2 = "11-08-2000";
        SimpleDateFormat frmt = new SimpleDateFormat("dd-mm-yyyy");
        Date d1 = frmt.parse(date1);
        Date d2 = frmt.parse(date2);
        AuthorsRepo.deleteAuthors();
        AuthorsRepo.addAuthor(new Author("Umberto Eco","italian","medieval",new Pair(d1,d2)));
        d1 = frmt.parse("20-09-1842");
        d2 = frmt.parse("02-12-1900");
        AuthorsRepo.addAuthor(new Author("Lev Tolstoi","russian","realism",new Pair(d1,d2)));

        PublishersRepo.deletePublishers();
        PublishersRepo.addPublisher(new Publisher("Valeria",true, new String[]{"Elizabeth I Street"}));
        PublishersRepo.addPublisher(new Publisher("Time",false, new String[]{"Euler Street"}));

        Map<Integer,String> prog = new HashMap<Integer,String>();
        prog.put(1,"8:00 - 12:00");
        prog.put(3,"14:00 - 20:00");
        prog.put(5,"9:00 - 16:00");
        prog.put(6,"17:00 - 21:00");

        LibrariansRepo.deleteLibrarians();
        LibrariansRepo.addLibrarian(new Librarian("Molly","1234567890", prog));

        SectionsRepo.deleteSections();
        SectionsRepo.addSection(new Section("Literature",'B'));
        SectionsRepo.addSection(new Section("Science",'K'));

        Date d0 = frmt.parse("18-05-1999");
        Date d00 = frmt.parse("10-02-2001");
        BooksRepo.deleteBooks();
        BooksRepo.addBook(new Book("978-3-16-148410-0", "War and Peace", AuthorsRepo.getAuthor(1),
                PublishersRepo.getPublisher(0), SectionsRepo.getSection(0), d0, "hardcover"));
        BooksRepo.addBook(new Book("118-9-32-148410-0", "The Name Of The Rose", AuthorsRepo.getAuthor(0),
                PublishersRepo.getPublisher(1), SectionsRepo.getSection(0), d00, "hardcover"));

        Date d3 = frmt.parse("01-01-2020");
        Date d4 = frmt.parse("01-01-2021");
        LibrarySubscribersRepo.deleteLibrarySubscribers();
        LibrarySubscribersRepo.addLibrarySubscriber(new LibrarySubscriber("Jane Doe", "1112223330", new Pair(d3,d4), "Student", null));

        Date d5 = frmt.parse("24-07-2019");
        Date d6 = frmt.parse("24-07-2022");
        Triplet<Book, Integer, Date> donat1 = new Triplet(BooksRepo.getBook(1),100, d5);
        Triplet<Book, Integer, Date> donat2 = new Triplet(BooksRepo.getBook(0),80, d5);
        List<Triplet<Book, Integer, Date>> donats = new ArrayList<>();
        donats.add(donat1);
        donats.add(donat2);
        Triplet<Book, Date, Boolean> bor = new Triplet(BooksRepo.getBook(0), frmt.parse("10-04-2021"), false);
        List<Triplet<Book, Date, Boolean>> bors = new ArrayList<>();
        bors.add(bor);
        BookReadersRepo.deleteBookReaders();
        BookReadersRepo.addBookReader(new BookReader("John Doe", "9998887776", new Pair(d5,d6), "PhD", donats,
                "Baker Street 23A", bors,0, 0));

        for (BookReader reader:
             BookReadersRepo.getBookReaders()) {
            LibrarySubscribersRepo.addLibrarySubscriber(reader);
        }

        TransactionsRepo.deleteTransactions();
        TransactionsRepo.addTransaction(new Transaction(new BigDecimal(1000),"RO49AAAA1B31007593840000","RO49AAAA1B31007593840001","lost book"));

        Map<Integer,String> progBiblio = new HashMap<Integer,String>();
        progBiblio.put(1,"8:00 - 22:00");
        progBiblio.put(2,"8:00 - 22:00");
        progBiblio.put(3,"8:00 - 20:00");
        progBiblio.put(4,"9:00 - 20:00");
        progBiblio.put(5,"9:00 - 20:00");
        progBiblio.put(6,"10:00 - 21:00");
        progBiblio.put(7,"10:00 - 21:00");
        Schedule = progBiblio;
    }

    public ArrayList<Author> getAllAuthors() {
        return AuthorsRepo.getAuthors();
    }

    public ArrayList<Book> getAllBooks() {
        return BooksRepo.getBooks();
    }

    public ArrayList<BookReader> getAllReaders() {
        return BookReadersRepo.getBookReaders();
    }

    public ArrayList<Librarian> getAllLibrarians() {
        return LibrariansRepo.getLibrarians();
    }

    public ArrayList<LibrarySubscriber> getAllSubscribers() {
        return LibrarySubscribersRepo.getLibrarySubscribers();
    }

    public ArrayList<Publisher> getAllPublishers() {
        return PublishersRepo.getPublishers();
    }

    public ArrayList<Section> getAllScetions() {
        return SectionsRepo.getSections();
    }

    public ArrayList<Transaction> getAllTranscations() {
        return TransactionsRepo.getTransactions();
    }

    public Map<Integer, String> getSchedule() {
        return Schedule;
    }

    public Map<Book, Long> getAllSpecimens()
    {
      return BooksRepo.getBooks().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<Book, Boolean> getSpecimenAvailability()
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

    @Override
    public String toString() {
        return "Init{" +
                "Authors=" + AuthorsRepo.getAuthors() +
                ", Publishers=" + PublishersRepo.getPublishers() +
                ", Librarians=" + LibrariansRepo.getLibrarians() +
                ", Books=" + BooksRepo.getBooks() +
                ", Subscribers=" + LibrarySubscribersRepo.getLibrarySubscribers() +
                ", Readers=" + BookReadersRepo.getBookReaders() +
                ", Transactions=" + TransactionsRepo.getTransactions() +
                ", Sections=" + SectionsRepo.getSections() +
                ", Schedule=" + Schedule +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Init init = (Init) o;
        return Objects.equals(AuthorsRepo, init.AuthorsRepo) && Objects.equals(PublishersRepo, init.PublishersRepo) && Objects.equals(LibrariansRepo, init.LibrariansRepo)
                && Objects.equals(BooksRepo, init.BooksRepo) && Objects.equals(LibrarySubscribersRepo, init.LibrarySubscribersRepo)
                && Objects.equals(BookReadersRepo, init.BookReadersRepo) && Objects.equals(TransactionsRepo, init.TransactionsRepo)
                && Objects.equals(SectionsRepo, init.SectionsRepo) && Objects.equals(Schedule, init.Schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(AuthorsRepo, PublishersRepo, LibrariansRepo, BooksRepo, LibrarySubscribersRepo, BookReadersRepo,
                TransactionsRepo, SectionsRepo, Schedule);
    }
}
