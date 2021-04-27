package main;

import com.opencsv.exceptions.CsvException;
import services.Init;
import services.Library;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Init ob = null;
        try {
            ob = new Init();
            Library lib = Library.getInstance();
            Library.setRepository(ob);
            lib.setTransactions(ob.getAllTransactions());
            lib.setMaxDaysBorrow(60);
            lib.setSubscribers(ob.getAllSubscribers());
            lib.setLibrarians(ob.getAllLibrarians());
            lib.setSchedule(ob.getSchedule());
            lib.setSpecimens(ob.getAllSpecimens());
            lib.setAvailability(ob.getSpecimenAvailability());
            System.out.println(lib.toString());
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}

