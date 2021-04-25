package repository;


import entities.Book;

import java.util.ArrayList;
import java.util.Comparator;

public class BookRepository {
    ArrayList<Book> Books = new ArrayList<Book>();


    public BookRepository(){

    }

    public Book getBook(int i){
        return this.Books.get(i);
    }

    public void addBook(Book x){
        this.Books.add(x);
    }
    public void deleteBook(Book x){
        this.Books.remove(x);
    }

    public Book aboutBook(String title){
        for (int i=0;i<this.Books.size();i++)
            if(this.Books.get(i).getTitle()==title)
                return this.Books.get(i);
        return null;
    }

    public ArrayList<Book> getBooks() {
        return Books;
    }

    public void deleteBooks(){
        while (this.Books.isEmpty()!=true)
            this.Books.remove(0);
    }

    public void updateBook(String title, Book x){
        for (int i=0;i<this.Books.size();i++)
        {
            if (this.Books.get(i).getTitle() == title) {
                this.Books.set(i, x);
            }
        }
    }

    public void sortBooks()
    {
        getBooks().sort(Comparator.comparing(Book::getTitle));
    }
}
