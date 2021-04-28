package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Book;
import entities.BookReader;
import entities.LibrarySubscriber;
import services.Logger;
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

    private static ArrayList<LibrarySubscriber> LibrarySubscribers;

    public static void initLibrarySubscribers() {
        LibrarySubscribers = new ArrayList<LibrarySubscriber>();
    }

    private Pair<Date, Date> getMembershipValidity(String validity, SimpleDateFormat formatter) throws ParseException {
        String[] arrOfStr = validity.split(";");
        Date d1 = formatter.parse(arrOfStr[0]), d2 = formatter.parse(arrOfStr[1]);
        return new Pair<>(d1, d2);
    }

    private ArrayList<Triplet<Book, Integer, Date>> getDonations(String donationsString, SimpleDateFormat formatter) throws ParseException {
        ArrayList<Triplet<Book, Integer, Date>> donations = new ArrayList<Triplet<Book, Integer, Date>>();
        if (!donationsString.equals("NaN")) {
            String[] arrOfStr = donationsString.substring(1, donationsString.length() - 1).split(",");
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

    private String formatDonations(ArrayList<Triplet<Book, Integer, Date>> donations) {
        String donationsString = "NaN";
        if (!donations.isEmpty()) {
            StringBuilder donnationsBuilder = new StringBuilder();
            donnationsBuilder.append("\"[");
            for (Triplet<Book, Integer, Date> don: donations) {
                donnationsBuilder.append("(").append(don.getFirst().getId().toString()).
                        append(";").append(don.getSecond().toString()).append(";").append(don.getThird().toString()).append(");");
            }
            donnationsBuilder.deleteCharAt(donnationsBuilder.length() - 1);
            donnationsBuilder.append("]\"");
            donationsString = donnationsBuilder.toString();
        }
        return donationsString;
    }

    public void initializeLibrarySubscribersFromCSV() throws IOException, CsvException {
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
                    Logger.logOperation("Initialized library subscribers from csv file. - FAILED");
                    e.printStackTrace();
                }
                librarySubscriber.setStudyLevel(data[4].trim());
                return librarySubscriber;
            }).collect(Collectors.toList());
            LibrarySubscribers.addAll(csvObjectList);
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized library subscribers from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Initialized library subscribers from csv file. - SUCCESS");
    }

    public LibrarySubscriber getLibrarySubscriber(Integer id) {
        for (LibrarySubscriber librarySubscriber: LibrarySubscribers) {
            if (librarySubscriber.getId().equals(id))
                return librarySubscriber;
        }
        return null;
    }

    public Integer addLibrarySubscriber(LibrarySubscriber x) throws IOException {
        List<Integer> ids = LibrarySubscribers.stream().map(LibrarySubscriber::getId).sorted(Comparator.comparing(Integer::valueOf)).
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
            FileWriter filewriter = new FileWriter("data/library_subscribers.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getName(), x.getPhoneNumber(),
                    x.getMembershipValidity().toStringForCsv(), x.getStudyLevel(), formatDonations(x.getDonation())});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("New library subscriber added in csv file. - FAILED");
            throw e;
        }
        LibrarySubscribers.add(x);
        Logger.logOperation("New library subscriber added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteLibrarySubscriber(Integer id) throws Exception {
        for (BookReader bookReader: BookReaderRepository.getBookReaders()) {
            if (bookReader.getId().equals(id)) {
                Logger.logOperation("Library subscriber removed from csv file. - FAILED");
                throw new Exception("This library subscriber is referenced in the BookReaders database. " +
                        "A referenced object cannot be removed");
            }
        }
        LibrarySubscribers.removeIf(librarySubscriber -> librarySubscriber.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/library_subscribers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "MembershipValidity", "StudyLevel", "Donation"});
            for (LibrarySubscriber librarySubscriber: LibrarySubscribers) {
                writer.writeNext(new String[]{librarySubscriber.getId().toString(), librarySubscriber.getName(),
                        librarySubscriber.getPhoneNumber(), librarySubscriber.getMembershipValidity().toStringForCsv(),
                        librarySubscriber.getStudyLevel(), formatDonations(librarySubscriber.getDonation())});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Library subscriber removed from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Library subscriber removed from csv file. - SUCCESS");
    }

    public String aboutLibrarySubscriber(Integer id){
        for (LibrarySubscriber librarySubscriber : LibrarySubscribers)
            if (librarySubscriber.getId().equals(id))
                return librarySubscriber.toString();
        return null;
    }

    public static ArrayList<LibrarySubscriber> getLibrarySubscribers() {
        return LibrarySubscribers;
    }

    public void deleteLibrarySubscribers() throws  Exception {
        boolean fullDelete = Boolean.TRUE;
        for (LibrarySubscriber librarySubscriber : LibrarySubscribers) {
            try {
                deleteLibrarySubscriber(librarySubscriber.getId());
            } catch (Exception e) {
                fullDelete = Boolean.FALSE;
            }
        }
        if (fullDelete) {
            Logger.logOperation("Delete all library subscribers. - SUCCESS");
        } else {
            Logger.logOperation("Delete all library subscribers. - FAILED");
            throw new Exception("Some of the library subscribers could not be deleted because they are referenced somewhere else. " +
                    "A referenced object cannot be removed");
        }
    }

    public void updateLibrarySubscriber(Integer id, LibrarySubscriber x) throws IOException {
        x.setId(id);
        for (int i=0;i<LibrarySubscribers.size();i++)
        {
            if(LibrarySubscribers.get(i).getId().equals(id)) {
                LibrarySubscribers.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/library_subscribers.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Name", "PhoneNumber", "MembershipValidity", "StudyLevel", "Donation"});
            for (LibrarySubscriber librarySubscriber: LibrarySubscribers) {
                writer.writeNext(new String[]{librarySubscriber.getId().toString(), librarySubscriber.getName(),
                        librarySubscriber.getPhoneNumber(), librarySubscriber.getMembershipValidity().toStringForCsv(),
                        librarySubscriber.getStudyLevel(), formatDonations(librarySubscriber.getDonation())});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Library subscriber updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Library subscriber updated in csv file. - SUCCESS");
    }

    public void sortLibrarySubscribers()
    {
        getLibrarySubscribers().sort(Comparator.comparing(LibrarySubscriber::getName));
    }
}
