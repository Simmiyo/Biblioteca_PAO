package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Book;
import entities.LibrarySubscriber;
import services.Pair;
import services.Triplet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LibrarySubscriberRepository {
    ArrayList<LibrarySubscriber> LibrarySubscribers = new ArrayList<LibrarySubscriber>();


    public LibrarySubscriberRepository(){

    }

    private Pair<Date, Date> getMembershipValidity(String validity, SimpleDateFormat formatter) throws ParseException {
        String[] arrOfStr = validity.split(";");
        Date d1 = formatter.parse(arrOfStr[0]), d2 = formatter.parse(arrOfStr[1]);
        return new Pair<>(d1, d2);
    }

    private List<Triplet<Book, Integer, Date>> getDonations(String donationsString, SimpleDateFormat formatter) throws ParseException {
        List<Triplet<Book, Integer, Date>> donations = null;
        if (!donationsString.equals("NaN")) {
            String[] arrOfStr = donationsString.substring(1, donationsString.length() - 1).split(";");
            BookRepository books = new BookRepository();
            for (String arr : arrOfStr) {
                arr = arr.substring(1, arr.length() - 1);
                String[] splitArr = arr.split(";");
                donations.add(new Triplet<>(books.getBook(Integer.parseInt(splitArr[0])), Integer.parseInt(splitArr[1]),
                        formatter.parse(splitArr[2])));
            }
        }
        return donations;
    }

    private String formatDonations(List<Triplet<Book, Integer, Date>> donations) {
        String donationsString = "NaN";
        if (!donations.isEmpty()) {
            StringBuilder donnationsBuilder = new StringBuilder();
            donnationsBuilder.append("[");
            for (Triplet<Book, Integer, Date> don: donations) {
                donnationsBuilder.append("(").append(don.getFirst().getId().toString()).
                        append(";").append(don.getSecond().toString()).append(";").append(don.getThird().toString()).append(");");
            }
            donnationsBuilder.deleteCharAt(donnationsBuilder.length() - 1);
            donnationsBuilder.append("]");
            donationsString = donnationsBuilder.toString();
        }
        return donationsString;
    }

    public void InitializeLibrarySubscribersFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/library_subscribers.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");

            List<LibrarySubscriber> csvObjectList = csvReader.readAll().stream().map(data-> {
                LibrarySubscriber librarySubscriber = new LibrarySubscriber();
                librarySubscriber.setId(Integer.parseInt(data[0].trim()));
                librarySubscriber.setName(data[1].trim());
                librarySubscriber.setPhoneNumber(data[2].trim());
                try {
                    librarySubscriber.setMembershipValidity(getMembershipValidity(data[3].trim(), frmt));
                    librarySubscriber.setDonation(getDonations(data[5].trim(), frmt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                librarySubscriber.setStudyLevel(data[4].trim());
                return librarySubscriber;
            }).collect(Collectors.toList());
            this.LibrarySubscribers.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public LibrarySubscriber getLibrarySubscriber(Integer id) {
        for (LibrarySubscriber librarySubscriber: LibrarySubscribers) {
            if (librarySubscriber.getId().equals(id))
                return librarySubscriber;
        }
        return null;
    }

    public void addLibrarySubscriber(LibrarySubscriber x){
        List<Integer> ids = LibrarySubscribers.stream().map(LibrarySubscriber::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
                x.setId(i);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/library_subscribers.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getName(), x.getPhoneNumber(),
                    x.getMembershipValidity().toStringForCsv(), x.getStudyLevel(), formatDonations(x.getDonation())});
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.LibrarySubscribers.add(x);
    }

    public void deleteLibrarySubscriber(LibrarySubscriber x){
        this.LibrarySubscribers.remove(x);
    }

    public LibrarySubscriber aboutLibrarySubscriber(Integer id){
        for (LibrarySubscriber librarySubscriber : this.LibrarySubscribers)
            if (librarySubscriber.getId().equals(id))
                return librarySubscriber;
        return null;
    }

    public ArrayList<LibrarySubscriber> getLibrarySubscribers() {
        return LibrarySubscribers;
    }

    public void deleteLibrarySubscribers(){
        while (!this.LibrarySubscribers.isEmpty())
            this.LibrarySubscribers.remove(0);
    }

    public void updateLibrarySubscriber(Integer id, LibrarySubscriber x){
        x.setId(id);
        for (int i=0;i<this.LibrarySubscribers.size();i++)
        {
            if(this.LibrarySubscribers.get(i).getId().equals(id)) {
                this.LibrarySubscribers.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/authors.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "MembershipValidity", "StudyLevel", "Donation"});
            for (LibrarySubscriber librarySubscriber: LibrarySubscribers) {
                writer.writeNext(new String[]{x.getId().toString(), x.getName(), x.getPhoneNumber(),
                        x.getMembershipValidity().toStringForCsv(), x.getStudyLevel(), formatDonations(x.getDonation())});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortLibrarySubscribers()
    {
        getLibrarySubscribers().sort(Comparator.comparing(LibrarySubscriber::getName));
    }
}
