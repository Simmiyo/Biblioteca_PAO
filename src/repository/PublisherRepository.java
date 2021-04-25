package repository;

import entities.Publisher;

import java.util.ArrayList;
import java.util.Comparator;

public class PublisherRepository {
    ArrayList<Publisher> Publishers = new ArrayList<Publisher>();


    public PublisherRepository(){

    }

    public Publisher getPublisher(int i){
        return this.Publishers.get(i);
    }

    public void addPublisher(Publisher x){
        this.Publishers.add(x);
    }
    public void deletePublisher(Publisher x){
        this.Publishers.remove(x);
    }

    public Publisher aboutPublisher(String name){
        for (int i=0;i<this.Publishers.size();i++)
            if(this.Publishers.get(i).getName()==name)
                return this.Publishers.get(i);
        return null;
    }

    public ArrayList<Publisher> getPublishers() {
        return Publishers;
    }

    public void deletePublishers(){
        while (this.Publishers.isEmpty()!=true)
            this.Publishers.remove(0);
    }

    public void updatePublisher(String name, Publisher x){
        for (int i=0;i<this.Publishers.size();i++)
        {
            if(this.Publishers.get(i).getName() == name) {
                this.Publishers.set(i, x);
                break;
            }
        }
    }

    public void sortPublishers()
    {
        getPublishers().sort(Comparator.comparing(Publisher::getName));
    }
}
