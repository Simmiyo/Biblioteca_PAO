package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Librarian;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarianRepository {
    ArrayList<Librarian> Librarians = new ArrayList<Librarian>();


    public LibrarianRepository(){

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

            List<Librarian> csvObjectList = csvReader.readAll().stream().map(data-> {
                Librarian librarian = new Librarian();
                librarian.setId(Integer.parseInt(data[0].trim()));
                librarian.setName(data[1].trim());
                librarian.setPhoneNumber(data[2].trim());
                librarian.setSchedule(getSchedule(data[3].trim()));
                return librarian;
            }).collect(Collectors.toList());
            this.Librarians.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Librarian getLibrarian(Integer id){
        for (Librarian librarian: Librarians) {
            if (librarian.getId().equals(id))
                return librarian;
        }
        return null;
    }

    public void addLibrarian(Librarian x){
        List<Integer> ids = Librarians.stream().map(Librarian::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
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
            e.printStackTrace();
        }
        this.Librarians.add(x);
    }

    public void deleteLibrarian(Librarian x){
        this.Librarians.remove(x);
    }

    public Librarian aboutLibrarian(Integer id){
        for (Librarian librarian : this.Librarians)
            if (librarian.getId().equals(id))
                return librarian;
        return null;
    }

    public ArrayList<Librarian> getLibrarians() {
        return Librarians;
    }

    public void deleteLibrarians(){
        while (!this.Librarians.isEmpty())
            this.Librarians.remove(0);
    }

    public void updateLibrarian(Integer id, Librarian x){
        x.setId(id);
        for (int i=0;i<this.Librarians.size();i++)
        {
            if(this.Librarians.get(i).getId().equals(id)) {
                this.Librarians.set(i, x);
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
            e.printStackTrace();
        }
    }

    public void sortLibrarians()
    {
        getLibrarians().sort(Comparator.comparing(Librarian::getName));
    }
}
