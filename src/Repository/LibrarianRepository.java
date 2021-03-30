package Repository;

import Entities.Librarian;

import java.util.ArrayList;
import java.util.Comparator;

public class LibrarianRepository {
    ArrayList<Librarian> Librarians = new ArrayList<Librarian>();


    public LibrarianRepository(){

    }

    public Librarian getLibrarian(int i){
        return this.Librarians.get(i);
    }

    public void addLibrarian(Librarian x){
        this.Librarians.add(x);
    }
    public void deleteLibrarian(Librarian x){
        this.Librarians.remove(x);
    }

    public Librarian aboutLibrarian(String name){
        for (int i=0;i<this.Librarians.size();i++)
            if(this.Librarians.get(i).getName()==name)
                return this.Librarians.get(i);
        return null;
    }

    public ArrayList<Librarian> getLibrarians() {
        return Librarians;
    }

    public void deleteLibrarians(){
        while (this.Librarians.isEmpty()!=true)
            this.Librarians.remove(0);
    }

    public void updateLibrarian(String name, Librarian x){
        for (int i=0;i<this.Librarians.size();i++)
        {
            if (this.Librarians.get(i).getName() == name) {
                this.Librarians.set(i, x);
                break;
            }
        }
    }

    public void sortLibrarians()
    {
        getLibrarians().sort(Comparator.comparing(Librarian::getName));
    }
}
