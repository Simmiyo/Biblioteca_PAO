package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Librarian;
import org.apache.commons.lang3.StringUtils;
import services.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarianRepository {

    private static ArrayList<Librarian> Librarians;

    public static void initLibrarians() {
        Librarians = new ArrayList<Librarian>();
    }

    private Map<Integer, String> getSchedule(String scheduleString) {
        Map<Integer, String> schedule = new HashMap<Integer,String>();
        String[] arrOfStr = scheduleString.split(";");
        for (String arr: arrOfStr) {
            arr = arr.substring(1, arr.length() - 1);
            String[] splitArr = arr.split(";");
            schedule.put(Integer.parseInt(splitArr[0]), splitArr[1]);
        }
        return schedule;
    }

    private String scheduleToString(Map<Integer, String> schedule) {
        StringBuilder scheduleString = new StringBuilder();
        for (Map.Entry<Integer, String> entry: schedule.entrySet()) {
            scheduleString.append("(").append(entry.getKey().toString()).append(";").append(entry.getValue()).append(");");
        }
        return StringUtils.chop(scheduleString.toString()); // remove last ; with chop
    }

    public void initializeLibrariansFromCSV() {
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

            List<Librarian> csvObjectList = csvReader.readAll().stream().map(data-> {
                Librarian librarian = new Librarian();
                librarian.setId(Integer.parseInt(data[0].trim()));
                librarian.setName(data[1].trim());
                librarian.setPhoneNumber(data[2].trim());
                librarian.setSchedule(getSchedule(data[3].trim()));
                return librarian;
            }).collect(Collectors.toList());
            Librarians.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized librarians from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Initialized librarians from csv file. - SUCCESS");
    }

    public Librarian getLibrarian(Integer id){
        for (Librarian librarian: Librarians) {
            if (librarian.getId().equals(id))
                return librarian;
        }
        return null;
    }

    public Integer addLibrarian(Librarian x){
        List<Integer> ids = Librarians.stream().map(Librarian::getId).sorted(Comparator.comparing(Integer::valueOf)).
                collect(Collectors.toList());
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
                x.setId(i);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), scheduleToString(x.getSchedule())});
        } catch (IOException e) {
            Logger.logOperation("New librarian added in csv file. - FAILED");
            e.printStackTrace();
        }
        Librarians.add(x);
        Logger.logOperation("New librarian added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteLibrarian(Integer id){
        Librarians.removeIf(librarian -> librarian.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "Schedule"});
            for (Librarian librarian: Librarians) {
                writer.writeNext(new String[]{librarian.getId().toString(), librarian.getName(), librarian.getPhoneNumber(),
                        scheduleToString(librarian.getSchedule())});
            }
        } catch (IOException e) {
            Logger.logOperation("Librarian removed from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Librarian removed from csv file. - SUCCESS");
    }

    public Librarian aboutLibrarian(Integer id){
        for (Librarian librarian : Librarians)
            if (librarian.getId().equals(id))
                return librarian;
        return null;
    }

    public static ArrayList<Librarian> getLibrarians() {
        return Librarians;
    }

    public void deleteLibrarians(){
       Librarians.clear();
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "Schedule"});
        } catch (IOException e) {
            Logger.logOperation("Deleted all librarians. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Deleted all librarians. - SUCCESS");
    }

    public void updateLibrarian(Integer id, Librarian x){
        x.setId(id);
        for (int i=0;i<Librarians.size();i++)
        {
            if(Librarians.get(i).getId().equals(id)) {
                Librarians.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "Schedule"});
            for (Librarian librarian: Librarians) {
                writer.writeNext(new String[]{librarian.getId().toString(), librarian.getName(), librarian.getPhoneNumber(),
                        scheduleToString(librarian.getSchedule())});
            }
        } catch (IOException e) {
            Logger.logOperation("Librarian updated in csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Librarian updated in csv file. - SUCCESS");
    }

    public void sortLibrarians()
    {
        getLibrarians().sort(Comparator.comparing(Librarian::getName));
    }
}
