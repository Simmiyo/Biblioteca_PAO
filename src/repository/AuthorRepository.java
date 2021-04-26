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
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");

            List<Author> csvObjectList = csvReader.readAll().stream().map(data-> {
                Author author = new Author();
                author.setId(Integer.parseInt(data[0].trim()));
                author.setName(data[1].trim());
                author.setNationality(data[2].trim());
                author.setMovement(data[3].trim());
                try {
                    author.setActivity(getActivity(data[4].trim(), frmt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return author;
            }).collect(Collectors.toList());
            this.Authors.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Author getAuthor(Integer id){
        for (Author author: Authors) {
            if (author.getId().equals(id))
                return author;
        }
        return null;
    }

    public void addAuthor(Author x) {
        List<Integer> ids = Authors.stream().map(Author::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
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

    // mai ramane delete
    public void deleteAuthor(Author x){
        this.Authors.remove(x);
    }

    public Author aboutAuthor(Integer id){
        for (Author author : this.Authors)
            if (author.getId().equals(id))
                return author;
        return null;
    }

    public ArrayList<Author> getAuthors() {
        return Authors;
    }

    public void deleteAuthors(){
        while (!this.Authors.isEmpty())
            this.Authors.remove(0);
    }

    public void updateAuthor(Integer id, Author x){
        x.setId(id);
        for (int i=0;i<this.Authors.size();i++)
        {
            if(this.Authors.get(i).getId().equals(id)) {
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
        Collections.sort(Authors);
    }
}
