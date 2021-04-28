package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Book;
import services.Logger;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AuthorRepository {

    private static ArrayList<Author> Authors;

    public static void initAuthors() {
        Authors = new ArrayList<Author>();
    }

    private Pair<Date, Date> getActivity(String activity, SimpleDateFormat formatter) throws ParseException {
        String[] arrOfStr = activity.split(";");
        Date d1 = formatter.parse(arrOfStr[0]), d2 = formatter.parse(arrOfStr[1]);
        return new Pair<>(d1, d2);
    }

    public void initializeAuthorsFromCSV() throws IOException, CsvException {
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
                    Logger.logOperation("Initialized authors from csv file. - FAILED");
                    e.printStackTrace();
                }
                return author;
            }).collect(Collectors.toList());
            Authors.addAll(csvObjectList);
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized authors from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Initialized authors from csv file. - SUCCESS");
    }

    public Author getAuthor(Integer id){
        for (Author author: Authors) {
            if (author.getId().equals(id))
                return author;
        }
        return null;
    }

    public Integer addAuthor(Author x) throws IOException {
        List<Integer> ids = Authors.stream().map(Author::getId).sorted(Comparator.comparing(Integer::valueOf)).
                collect(Collectors.toList());
        if (!ids.isEmpty()) {
            x.setId(-1);
            for (Integer i = 0, j = 0; i < ids.size() ; i += 1, j+= 1) {
                if (!j.equals(ids.get(i))) {
                    x.setId(j);
                    break;
                }
            }
            if (x.getId().equals(-1)) x.setId(ids.get(ids.size() - 1) + 1);
        } else {
            x.setId(0);
        }
        try {
            FileWriter filewriter = new FileWriter("data/authors.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getName(), x.getNationality(),
                    x.getMovement(), x.getActivity().toStringForCsv()});
            writer.close();

        } catch (IOException e) {
            Logger.logOperation("New author added in csv file. - FAILED");
            throw e;
        }
        Authors.add(x);
        Logger.logOperation("New author added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteAuthor(Integer id) throws Exception {
        for (Book book: BookRepository.getBooks()) {
            if (book.getAuthor().getId().equals(id)) {
                Logger.logOperation("Author removed from csv file. - FAILED");
                throw new Exception("This author is referenced in the Books database. " +
                        "A referenced object cannot be removed");
            }
        }
        Authors.removeIf(author -> author.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/authors.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "Nationality", "Movement", "Activity"});
            for (Author author: Authors) {
                writer.writeNext(new String[]{author.getId().toString(), author.getName(), author.getNationality(),
                        author.getMovement(), author.getActivity().toStringForCsv()});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Author removed from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Author removed from csv file. - SUCCESS");
    }

    public String aboutAuthor(Integer id){
        for (Author author : Authors)
            if (author.getId().equals(id))
                return author.toString();
        return null;
    }

    public static ArrayList<Author> getAuthors() {
        return Authors;
    }

    public void deleteAuthors() throws Exception {
        boolean fullDelete = Boolean.TRUE;
        for (Author author : Authors) {
            try {
                deleteAuthor(author.getId());
            } catch (Exception e) {
                fullDelete = Boolean.FALSE;
            }
        }
        if (fullDelete) {
            Logger.logOperation("Delete all authors. - SUCCESS");
        } else {
            Logger.logOperation("Delete all authors. - FAILED");
            throw new Exception("Some of the authors could not be deleted because they are referenced somewhere else. " +
                    "A referenced object cannot be removed");
        }
    }

    public void updateAuthor(Integer id, Author x) throws IOException {
        x.setId(id);
        for (int i=0;i<Authors.size();i++)
        {
            if(Authors.get(i).getId().equals(id)) {
                Authors.set(i, x);
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
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Author updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Author updated in csv file. - SUCCESS");
    }

    public void sortAuthors()
    {
        Collections.sort(Authors);
    }
}
