package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AuthorRepository {
    ArrayList<Author> Authors = new ArrayList<Author>();


    public AuthorRepository(){

    }

    private Pair<Date, Date> getActivity(String activity, SimpleDateFormat formatter) throws ParseException {
        String[] arrOfStr = activity.split(";");
        Date d1 = formatter.parse(arrOfStr[0]), d2 = formatter.parse(arrOfStr[1]);
        return new Pair<>(d1, d2);
    }

    public void InitializeAuthorsFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/authors.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();
            SimpleDateFormat frmt = new SimpleDateFormat("dd-mm-yyyy");

            List<Author> csv_objectList = csvReader.readAll().stream().map(data-> {
                Author author = new Author();
                author.setId(Integer.parseInt(data[0]));
                author.setName(data[1]);
                author.setNationality(data[2]);
                author.setMovement(data[3]);
                try {
                    author.setActivity(getActivity(data[4], frmt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return author;
            }).collect(Collectors.toList());
            this.Authors.addAll(csv_objectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Author getAuthor(int i){
        return this.Authors.get(i);
    }

    public void addAuthor(Author x){
        Boolean exists = Boolean.FALSE;
        ArrayList<Integer> ids = new ArrayList<>();
        for (Author autor: Authors
             ) {
            if (autor.equals(x)) {
                exists = Boolean.TRUE;
                break;
            }
            ids.add(autor.getId());
        }
        if (!exists) {
            ids.sort(Comparator.comparing(Integer::valueOf));
            for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
                if (i != ids.get(i)) {
                    x.setId(i);
                    break;
                }
            }
            try {
                FileWriter filewriter = new FileWriter("data/authors.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                writer.writeNext(new String[]{x.getId().toString(), x.getName(), x.getNationality(),
                        x.getMovement(), x.getActivity().toStringForCsv()});
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.Authors.add(x);
        }
    }

    // mai ramane delete
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

    public void updateAuthor(Integer id, Author x){
        x.setId(id);
        for (int i=0;i<this.Authors.size();i++)
        {
            if(this.Authors.get(i).getId() == id) {
                this.Authors.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/authors.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "Nationality", "Movement", "Activity"});
            for (Author author: Authors) {
                writer.writeNext(new String[]{author.getId().toString(), author.getName(), author.getNationality(),
                        author.getMovement(), author.getActivity().toStringForCsv()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortAuthors()
    {
        getAuthors().sort(Comparator.comparing(Author::getName));
    }
}
