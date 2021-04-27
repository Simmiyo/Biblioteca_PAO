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
        String[] arrOfStr = scheduleString.substring(1, scheduleString.length() - 1).split(",");
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
            scheduleString.append("\"(").append(entry.getKey().toString()).append(";").append(entry.getValue()).append(");");
        }
        return StringUtils.chop(scheduleString.toString()) + "\""; // remove last ; with chop
    }

    public void initializeLibrariansFromCSV() throws IOException, CsvException {
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
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized librarians from csv file. - FAILED");
            throw e;
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

    public Integer addLibrarian(Librarian x) throws IOException {
        List<Integer> ids = Librarians.stream().map(Librarian::getId).sorted(Comparator.comparing(Integer::valueOf)).
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
            FileWriter filewriter = new FileWriter("data/librarians.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), scheduleToString(x.getSchedule())});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("New librarian added in csv file. - FAILED");
            throw e;
        }
        Librarians.add(x);
        Logger.logOperation("New librarian added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteLibrarian(Integer id) throws IOException {
        Librarians.removeIf(librarian -> librarian.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "Schedule"});
            for (Librarian librarian: Librarians) {
                writer.writeNext(new String[]{librarian.getId().toString(), librarian.getName(), librarian.getPhoneNumber(),
                        scheduleToString(librarian.getSchedule())});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Librarian removed from csv file. - FAILED");
            throw e;
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

    public void deleteLibrarians() throws IOException {
       Librarians.clear();
        try {
            FileWriter filewriter = new FileWriter("data/librarians.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "Schedule"});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Deleted all librarians. - FAILED");
            throw e;
        }
        Logger.logOperation("Deleted all librarians. - SUCCESS");
    }

    public void updateLibrarian(Integer id, Librarian x) throws IOException {
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
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Librarian updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Librarian updated in csv file. - SUCCESS");
    }

    public void sortLibrarians()
    {
        getLibrarians().sort(Comparator.comparing(Librarian::getName));
    }
}
