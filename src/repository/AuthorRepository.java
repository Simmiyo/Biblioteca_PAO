package repository;

import entities.Author;

import java.util.ArrayList;
import java.util.Comparator;

public class AuthorRepository {
    ArrayList<Author> Authors = new ArrayList<Author>();


    public AuthorRepository(){

    }

    public Author getAuthor(int i){
        return this.Authors.get(i);
    }

    public void addAuthor(Author x){
        this.Authors.add(x);
    }
    public void deleteAuthor(Author x){
        this.Authors.remove(x);
    }

    public Author aboutAuthor(String name){
        for (int i=0;i<this.Authors.size();i++)
            if(this.Authors.get(i).getName()==name)
                return this.Authors.get(i);
        return null;
    }

    public ArrayList<Author> getAuthors() {
        return Authors;
    }

    public void deleteAuthors(){
        while (this.Authors.isEmpty()!=true)
            this.Authors.remove(0);
    }

    public void updateAuthor(String name, Author x){
        for (int i=0;i<this.Authors.size();i++)
        {
            if(this.Authors.get(i).getName() == name) {
                this.Authors.set(i, x);
                break;
            }
        }
    }

    public void sortAuthors()
    {
        getAuthors().sort(Comparator.comparing(Author::getName));
    }
}
