package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Book;
import entities.Publisher;
import org.apache.commons.lang3.ObjectUtils;
import services.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PublisherRepository {

    private static ArrayList<Publisher> Publishers;

    public static void initPublishers() {
        Publishers = new ArrayList<Publisher>();
    }

    public void initializePublishersFromCSV() throws IOException, CsvException {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/publishers.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            List<Publisher> csvObjectList = csvReader.readAll().stream().map(data-> {
                Publisher publisher = new Publisher();
                publisher.setId(Integer.parseInt(data[0].trim()));
                publisher.setName(data[1].trim());
                publisher.setContractor(Boolean.parseBoolean(data[2].trim()));
                publisher.setBranchOffices(Arrays.copyOfRange(data[3].trim().split(","),0,data.length));
                return publisher;
            }).collect(Collectors.toList());
            Publishers.addAll(csvObjectList);
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized publishers from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Initialized publishers from csv file. - SUCCESS");
    }

    public Publisher getPublisher(Integer id){
        for (Publisher publisher: Publishers) {
            if (publisher.getId().equals(id))
                return publisher;
        }
        return null;
    }

    public Integer addPublisher(Publisher x) throws IOException {
        List<Integer> ids = Publishers.stream().map(Publisher::getId).sorted(Comparator.comparing(Integer::valueOf)).
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
            FileWriter filewriter = new FileWriter("data/publishers.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);

            StringBuilder branches = new StringBuilder();
            for (String branch: x.getBranchOffices()) {
                branches.append(branch).append(",");
            }
            writer.writeNext(new String[]{x.getId().toString(), x.getName(),
                    Boolean.toString(x.getContractor()), branches.toString()});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("New publisher added in csv file. - FAILED");
            throw e;
        }
        Publishers.add(x);
        Logger.logOperation("New publisher added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deletePublisher(Integer id) throws Exception {
        for (Book book: BookRepository.getBooks()) {
            if (book.getPublisher().getId().equals(id)) {
                Logger.logOperation("publisher removed from csv file. - FAILED");
                throw new Exception("This publisher is referenced in the Books database. " +
                        "A referenced object cannot be removed");
            }
        }
        Publishers.removeIf(publisher -> publisher.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/publishers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "IsContractor", "BranchOffices"});
            for (Publisher publisher: Publishers) {
                writer.writeNext(new String[]{publisher.getId().toString(), publisher.getName(),
                        String.valueOf(publisher.getContractor()), String.join(";", publisher.getBranchOffices())});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("publisher removed from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("publisher removed from csv file. - SUCCESS");
    }

    public Publisher aboutPublisher(Integer id){
        for (Publisher publisher : Publishers)
            if (publisher.getId().equals(id))
                return publisher;
        return null;
    }

    public static ArrayList<Publisher> getPublishers() {
        return Publishers;
    }

    public void deletePublishers() throws Exception {
        boolean fullDelete = Boolean.TRUE;
        for (Publisher publisher : Publishers) {
            try {
                deletePublisher(publisher.getId());
            } catch (Exception e) {
                fullDelete = Boolean.FALSE;
            }
        }
        if (fullDelete) {
            Logger.logOperation("Delete all publishers. - SUCCESS");
        } else {
            Logger.logOperation("Delete all publishers. - FAILED");
            throw new Exception("Some of the publishers could not be deleted because they are referenced somewhere else. " +
                    "A referenced object cannot be removed");
        }
    }

    public void updatePublisher(Integer id, Publisher x) throws IOException {
        x.setId(id);
        for (int i=0;i<Publishers.size();i++)
        {
            if(Publishers.get(i).getId().equals(id)) {
                Publishers.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/publishers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "IsContractor", "BranchOffices"});
            for (Publisher publisher: Publishers) {
                writer.writeNext(new String[]{publisher.getId().toString(), publisher.getName(),
                        String.valueOf(publisher.getContractor()), String.join(";", publisher.getBranchOffices())});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Publisher updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Publisher updated in csv file. - SUCCESS");
    }

    public void sortPublishers()
    {
        Collections.sort(Publishers);
    }
}
