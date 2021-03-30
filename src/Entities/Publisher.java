package Entities;

import java.util.Objects;

public class Publisher {
    private String Name;
    private boolean isContractor;
    private String BranchOffice;

    public Publisher(String name, boolean contractor, String branch){
        Name = name;
        isContractor = contractor;
        BranchOffice = branch;
    }

    public String getName() { return Name; }

    public boolean getContractor() { return isContractor; }

    public String getBranchOffice() { return BranchOffice; }

    public void setName(String name) { Name = name; }

    public void setContractor(boolean contractor) { isContractor = contractor; }

    public void setBranchOffice(String branchOffice) { BranchOffice = branchOffice; }

    @Override
    public String toString() {
        return "Publisher{" +
                "Name='" + Name + '\'' +
                ", isContractor=" + isContractor +
                ", BranchOffice='" + BranchOffice + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return getContractor() == publisher.getContractor() && Objects.equals(getName(), publisher.getName()) && Objects.equals(getBranchOffice(), publisher.getBranchOffice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContractor(), getBranchOffice());
    }
}
