package entities;

import services.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LibrarySubscriber {
    private Integer Id;
    private String Name;
    private String PhoneNumber;
    private Pair<Date, Date> MembershipValidity;
    private String StudyLevel; // pupil, high school student, college student, Bachelor, Master, PhD
    private ArrayList<Triplet<Book, Integer, Date>> Donation; // books brought & number of specimens & date they were donated

    public LibrarySubscriber(String name, String phone, Pair<Date,Date> member, String study,
                             ArrayList<Triplet<Book,Integer,Date>> donation){
        Name = name;
        PhoneNumber = phone;
        MembershipValidity = member;
        StudyLevel = study;
        Donation = donation;
    }

    public LibrarySubscriber() {

    }

    public Integer getId() { return Id; }

    public String getName() { return Name; }

    public String getPhoneNumber() { return PhoneNumber; }

    public Pair<Date, Date> getMembershipValidity() { return MembershipValidity; }

    public String getStudyLevel() { return StudyLevel; }

    public ArrayList<Triplet<Book, Integer, Date>> getDonation() { return Donation; }

    public void setId(Integer id) { Id = id; }

    public void setName(String name) { Name = name; }

    public void setPhoneNumber(String phoneNumber) { PhoneNumber = phoneNumber; }

    public void setMembershipValidity(Pair<Date, Date> membershipValidity) { MembershipValidity = membershipValidity; }

    public void setStudyLevel(String studyLevel) { StudyLevel = studyLevel; }

    public void setDonation(ArrayList<Triplet<Book, Integer, Date>> donation) { Donation = donation; }

    @Override
    public String toString() {
        return "LibrarySubscriber{" +
                "Id='" + Id + '\'' +
                "Name='" + Name + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", MembershipValidity=" + MembershipValidity +
                ", StudyLevel='" + StudyLevel + '\'' +
                ", Donation=" + Donation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibrarySubscriber that = (LibrarySubscriber) o;
        return  Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getPhoneNumber(), that.getPhoneNumber())
                && Objects.equals(getMembershipValidity(), that.getMembershipValidity())
                && Objects.equals(getStudyLevel(), that.getStudyLevel())
                && Objects.equals(getDonation(), that.getDonation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPhoneNumber(), getMembershipValidity(), getStudyLevel(), getDonation());
    }
}
