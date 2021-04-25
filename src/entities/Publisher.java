package entities;

import java.util.Arrays;
import java.util.Objects;

public class Publisher {
    private String Name;
    private boolean isContractor;
    private String[] BranchOffices;

    public Publisher(String name, boolean contractor, String[] branches){
        Name = name;
        isContractor = contractor;
        BranchOffices = Arrays.copyOf(branches,branches.length);
    }

    public String getName() { return Name; }

    public boolean getContractor() { return isContractor; }

    public String[] getBranchOffices() { return BranchOffices; }

    public void setName(String name) { Name = name; }

    public void setContractor(boolean contractor) { isContractor = contractor; }

    public void setBranchOffices(String[] branches) { BranchOffices = Arrays.copyOf(branches,branches.length); }

    @Override
    public String toString() {
        return "Publisher{" +
                "Name='" + Name + '\'' +
                ", isContractor=" + isContractor +
                ", BranchOffice='" + BranchOffices + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return getContractor() == publisher.getContractor() && Objects.equals(getName(), publisher.getName()) && Arrays.equals(getBranchOffices(), publisher.getBranchOffices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContractor(), Arrays.hashCode(getBranchOffices()));
    }
}
