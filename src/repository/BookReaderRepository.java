package repository;

import entities.BookReader;

import java.util.ArrayList;
import java.util.Comparator;

public class BookReaderRepository {
    ArrayList<BookReader> BookReaders = new ArrayList<BookReader>();


    public BookReaderRepository(){

    }

    public BookReader getBookReader(int i){
        return this.BookReaders.get(i);
    }

    public void addBookReader(BookReader x){
        this.BookReaders.add(x);
    }
    public void deleteBookReader(BookReader x){
        this.BookReaders.remove(x);
    }

    public BookReader aboutBookReader(String phone){
        for (int i=0;i<this.BookReaders.size();i++)
            if(this.BookReaders.get(i).getPhoneNumber()==phone)
                return this.BookReaders.get(i);
        return null;
    }

    public ArrayList<BookReader> getBookReaders() {
        return BookReaders;
    }

    public void deleteBookReaders(){
        while (this.BookReaders.isEmpty()!=true)
            this.BookReaders.remove(0);
    }

    public void updateBookReader(String name, BookReader x){
        for (int i=0;i<this.BookReaders.size();i++)
        {
            if (this.BookReaders.get(i).getName() == name) {
                this.BookReaders.set(i, x);
                break;
            }
        }
    }

    public void sortBookReaders()
    {
        getBookReaders().sort(Comparator.comparing(BookReader::getName));
    }
}
