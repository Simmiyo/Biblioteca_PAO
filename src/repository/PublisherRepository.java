package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Publisher;
import entities.Transaction;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PublisherRepository {
    ArrayList<Publisher> Publishers = new ArrayList<Publisher>();


    public PublisherRepository(){

    }

    public void InitializePublishersFromCSV() {
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
            List<String[]> allData = csvReader.readAll();

            List<Publisher> csv_objectList = csvReader.readAll().stream().map(data-> {
                Publisher publisher = new Publisher();
                publisher.setId(Integer.parseInt(data[0]));
                publisher.setName(data[1]);
                publisher.setContractor(Boolean.parseBoolean(data[2]));
                publisher.setBranchOffices(Arrays.copyOfRange(data,3,data.length));
                return publisher;
            }).collect(Collectors.toList());
            this.Publishers.addAll(csv_objectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Publisher getPublisher(int i){
        return this.Publishers.get(i);
    }

    public void addPublisher(Publisher x){
        Boolean exists = Boolean.FALSE;
        ArrayList<Integer> ids = new ArrayList<>();
        for (Publisher publisher: Publishers
        ) {
            if (publisher.equals(x)) {
                exists = Boolean.TRUE;
                break;
            }
            ids.add(publisher.getId());
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
                FileWriter filewriter = new FileWriter("data/publishers.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                String[] firstPart = new String[]{x.getId().toString(), x.getName(),
                        Boolean.toString(x.getContractor())};
                String[] bothParts = Stream.concat(Arrays.stream(firstPart), Arrays.stream(x.getBranchOffices()))
                        .toArray(String[]::new);
                writer.writeNext(bothParts);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.Publishers.add(x);
        }
    }

    //ramas
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

    public void updatePublisher(Integer id, Publisher x){
        x.setId(id);
        for (int i=0;i<this.Publishers.size();i++)
        {
            if(this.Publishers.get(i).getId() == id) {
                this.Publishers.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/publishers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "IsContractor", "BranchOffices"});
            for (Publisher publisher: Publishers) {
                String[] firstPart = new String[]{publisher.getId().toString(), publisher.getName(),
                        Boolean.toString(publisher.getContractor())};
                String[] bothParts = Stream.concat(Arrays.stream(firstPart), Arrays.stream(publisher.getBranchOffices()))
                        .toArray(String[]::new);
                writer.writeNext(bothParts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortPublishers()
    {
        getPublishers().sort(Comparator.comparing(Publisher::getName));
    }
}
