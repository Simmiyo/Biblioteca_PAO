package entities;

import java.util.*;

import services.Pair;
import services.Triplet;

public class BookReader extends LibrarySubscriber{
    private String Address;
    private List<Triplet<Book, Date, Boolean>> BorrowedBooks; // borrowed books & date they were borrowed & the return status
    private Integer PenaltyPoints;
    private Integer FidelityPoints;

    public BookReader(String name, String phone, Pair<Date, Date> member, String study,
                      List<Triplet<Book, Integer, Date>> donation, String address, List<Triplet<Book, Date, Boolean>> borrowed,
                      Integer penaltyPoints, Integer fidelityPoints) {
        super(name, phone, member, study, donation);
        Address = address;
        BorrowedBooks = borrowed;
        PenaltyPoints = penaltyPoints;
        FidelityPoints = fidelityPoints;
    }

    public String getAddress() { return Address; }

    public List<Triplet<Book, Date, Boolean>> getBorrowedBooks() { return BorrowedBooks; }

    public Integer getPenaltyPoints() { return PenaltyPoints; }

    public Integer getFidelityPoints() { return FidelityPoints; }

    public void setAddress(String address) { Address = address; }

    public void setBorrowedBooks(List<Triplet<Book, Date, Boolean>> borrowedBooks) { BorrowedBooks = borrowedBooks; }

    public void setPenaltyPoints(Integer penaltyPoints) { PenaltyPoints = penaltyPoints; }

    public void setFidelityPoints(Integer fidelityPoints) { FidelityPoints = fidelityPoints; }

    @Override
    public String toString() {
        return "BookReader{" +
                "Address='" + Address + '\'' +
                ", BorrowedBooks=" + BorrowedBooks +
                ", PenaltyPoints=" + PenaltyPoints +
                ", FidelityPoints=" + FidelityPoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookReader that = (BookReader) o;
        return Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getBorrowedBooks(), that.getBorrowedBooks()) && Objects.equals(getPenaltyPoints(), that.getPenaltyPoints()) && Objects.equals(getFidelityPoints(), that.getFidelityPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAddress(), getBorrowedBooks(), getPenaltyPoints(), getFidelityPoints());
    }
}
