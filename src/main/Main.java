package main;

import com.opencsv.exceptions.CsvException;
import entities.Publisher;
import entities.Section;
import entities.Transaction;
import services.Library;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<Integer,String> progBiblio = new HashMap<Integer,String>();
        progBiblio.put(1,"8:00 - 22:00");
        progBiblio.put(2,"8:00 - 22:00");
        progBiblio.put(3,"8:00 - 20:00");
        progBiblio.put(4,"9:00 - 20:00");
        progBiblio.put(5,"9:00 - 20:00");
        progBiblio.put(6,"10:00 - 21:00");
        progBiblio.put(7,"10:00 - 21:00");
        try {
            Library lib = Library.getInstance();
            lib.setSchedule(progBiblio);
            lib.setMaxDaysBorrow(60);
            lib.setSpecimens();
            lib.setAvailability();
            lib.initializeMainRepositoryFromCSV();
            System.out.println(lib.toString());
//            lib.addTransaction(new Transaction(new BigDecimal(4562), "RO49AAAA1B31227593840012", "RO49AAAA1B31007593840001", "bad book"));
//            lib.addSection(new Section("Fiction", 'R'));
//            lib.addPublisher(new Publisher("Jhon", true, new String[]{"Gustav Mistress", "Hector the Great"}));
//            lib.deletePublisher(new Integer(2));
            lib.updateSection(2, new Section("Science-Fiction", 'S'));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

