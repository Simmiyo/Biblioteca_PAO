package Services;

import Entities.*;

import java.util.*;
import java.time.*;
import java.text.*;
import java.util.stream.Collectors;

public class Library {

    private Integer MaxDaysBorrow;
    private Map<Integer, String> Schedule;
    private List<LibrarySubscriber> Subscribers;
    private List<Librarian> Librarians;
    private List<Transaction> Transactions;
    private Map<Book, Long> Specimens;
    private Map<Book, Boolean> Availability;
    private static Init Repository;

    private Library(){}

    private static class SingletonLibrary{
        private static final Library INSTANCE = new Library();
    }

    public static Init getRepository() { return Repository; }

    public static void setRepository(Init repository) { Repository = repository; }

    public static Library getInstance(){
        return SingletonLibrary.INSTANCE;
    }
    public Integer getMaxDaysBorrow() {
        return MaxDaysBorrow;
    }
    public Map<Integer, String> getSchedule() {
        return Schedule;
    }
    public List<LibrarySubscriber> getSubscribers() {
        return Subscribers;
    }
    public List<Librarian> getLibrarians() {
        return Librarians;
    }
    public List<Transaction> getTransactions() {
        return Transactions;
    }
    public Map<Book, Long> getSpecimens() {
        return Specimens;
    }
    public Map<Book, Boolean> getAvailability() {
        return Availability;
    }

    public void setMaxDaysBorrow(Integer maxDaysBorrow) {
        MaxDaysBorrow = maxDaysBorrow;
    }
    public void setSchedule(Map<Integer, String> schedule) {
        Schedule = schedule;
    }
    public void setSubscribers(List<LibrarySubscriber> subscribers) {
        Subscribers = subscribers;
    }
    public void setLibrarians(List<Librarian> librarians) {
        Librarians = librarians;
    }
    public void setTransactions(List<Transaction> transactions) {
        Transactions = transactions;
    }
    public void setSpecimens(Map<Book, Long> specimens) {
        Specimens = specimens;
    }
    public void setAvailability(Map<Book, Boolean> availability) {
        Availability = availability;
    }

    @Override
    public String toString() {
        return "Library{" +
                "MaxDaysBorrow=" + MaxDaysBorrow +
                ", Schedule=" + Schedule +
                ", Subscribers=" + Subscribers +
                ", Librarians=" + Librarians +
                ", Transactions=" + Transactions +
                ", Specimens=" + Specimens +
                ", Availability=" + Availability +
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
            LocalDate dueDate = startDate.plusDays(MaxDaysBorrow);
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

        return Availability.get(book);
    }

}
