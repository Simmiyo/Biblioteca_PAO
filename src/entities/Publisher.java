package entities;

import java.util.Arrays;
import java.util.Objects;

public class Publisher implements Comparable<Publisher> {
    private Integer Id;
    private String Name;
    private boolean IsContractor;
    private String[] BranchOffices;

    public Publisher(String name, boolean contractor, String[] branches){
        Name = name;
        IsContractor = contractor;
        BranchOffices = Arrays.copyOf(branches,branches.length);
    }

    public Publisher(){

    }

    public Integer getId() {return Id;}

    public String getName() { return Name; }

    public boolean getContractor() { return IsContractor; }

    public String[] getBranchOffices() { return BranchOffices; }

    public void setId(Integer id) {Id = id;}

    public void setName(String name) { Name = name; }

    public void setContractor(boolean contractor) { IsContractor = contractor; }

    public void setBranchOffices(String[] branches) { BranchOffices = Arrays.copyOf(branches,branches.length); }

    public int compareTo(Publisher p)
    {
        return this.Name.compareTo(p.Name);
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "Id='" + Id.toString() + '\'' +
                "Name='" + Name + '\'' +
                ", isContractor=" + IsContractor +
                ", BranchOffice='" + BranchOffices + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(getId(), publisher.getId())
                &&getContractor() == publisher.getContractor()
                && Objects.equals(getName(), publisher.getName())
                && Arrays.equals(getBranchOffices(), publisher.getBranchOffices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getContractor(), Arrays.hashCode(getBranchOffices()));
    }
}
