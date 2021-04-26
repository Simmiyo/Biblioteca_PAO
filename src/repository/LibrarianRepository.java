package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Librarian;
import entities.Publisher;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarianRepository {
    ArrayList<Librarian> Librarians = new ArrayList<Librarian>();


    public LibrarianRepository(){

    }

    private Map<Integer, String> getSchedule(String activity, SimpleDateFormat formatter) throws ParseException {
        String[] arrOfStr = activity.split(";");
        Date d1 = formatter.parse(arrOfStr[0]), d2 = formatter.parse(arrOfStr[1]);
        return new Pair<>(d1, d2);
    }

    public void InitializeLibrariansFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/librarians.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();

            List<Librarian> csv_objectList = csvReader.readAll().stream().map(data-> {
                Librarian librarian = new Librarian();
                librarian.setId(Integer.parseInt(data[0]));
                librarian.setName(data[1]);
                librarian.setPhoneNumber(data[2]);
                try {
                    author.setActivity(getActivity(data[4], frmt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return librarian;
            }).collect(Collectors.toList());
            this.Librarians.addAll(csv_objectList);
            SimpleDateFormat frmt = new SimpleDateFormat("dd-mm-yyyy");
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
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
