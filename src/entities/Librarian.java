package entities;

import java.util.Map;
import java.util.Objects;

public class Librarian {
    private Integer Id;
    private String Name;
    private String PhoneNumber;
    private Map<Integer, String> Schedule; // day of week & working hours interval

    public Librarian(String name, String phone, Map<Integer,String> schedule){
        Name = name;
        PhoneNumber = phone;
        Schedule = schedule;
    }

    public Integer getId() {return Id;}

    public String getName() { return Name; }

    public String getPhoneNumber() { return PhoneNumber; }

    public Map<Integer, String> getSchedule() { return Schedule; }

    public void setId(Integer id) {Id = id;}

    public void setName(String name) { Name = name; }

    public void setPhoneNumber(String phoneNumber) { PhoneNumber = phoneNumber; }

    public void setSchedule(Map<Integer, String> schedule) { Schedule = schedule; }

    @Override
    public String toString() {
        return "Librarian{" +
                "Id='" + Id.toString() + '\'' +
                "Name='" + Name + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Schedule=" + Schedule +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Librarian librarian = (Librarian) o;
        return Objects.equals(getId(), librarian.getId())
                &&Objects.equals(getName(), librarian.getName())
                && Objects.equals(getPhoneNumber(), librarian.getPhoneNumber())
                && Objects.equals(getSchedule(), librarian.getSchedule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPhoneNumber(), getSchedule());
    }
}