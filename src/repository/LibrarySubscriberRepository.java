package repository;

import entities.LibrarySubscriber;

import java.util.ArrayList;
import java.util.Comparator;

public class LibrarySubscriberRepository {
    ArrayList<LibrarySubscriber> LibrarySubscribers = new ArrayList<LibrarySubscriber>();


    public LibrarySubscriberRepository(){

    }

    public LibrarySubscriber getLibrarySubscriber(int i){
        return this.LibrarySubscribers.get(i);
    }

    public void addLibrarySubscriber(LibrarySubscriber x){
        this.LibrarySubscribers.add(x);
    }
    public void deleteLibrarySubscriber(LibrarySubscriber x){
        this.LibrarySubscribers.remove(x);
    }

    public LibrarySubscriber aboutLibrarySubscriber(String phone){
        for (int i=0;i<this.LibrarySubscribers.size();i++)
            if(this.LibrarySubscribers.get(i).getPhoneNumber()==phone)
                return this.LibrarySubscribers.get(i);
        return null;
    }

    public ArrayList<LibrarySubscriber> getLibrarySubscribers() {
        return LibrarySubscribers;
    }

    public void deleteLibrarySubscribers(){
        while (this.LibrarySubscribers.isEmpty()!=true)
            this.LibrarySubscribers.remove(0);
    }

    public void updateLibrarySubscriber(String name, LibrarySubscriber x){
        for (int i=0;i<this.LibrarySubscribers.size();i++)
        {
            if (this.LibrarySubscribers.get(i).getName() == name) {
                this.LibrarySubscribers.set(i, x);
                break;
            }
        }
    }

    public void sortLibrarySubscribers()
    {
        getLibrarySubscribers().sort(Comparator.comparing(LibrarySubscriber::getName));
    }
}
