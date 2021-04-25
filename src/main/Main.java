package main;

import services.Init;
import services.Library;

import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws ParseException {
        Init ob = new Init();
        Library lib = Library.getInstance();
        Library.setRepository(ob);
        lib.setTransactions(ob.getAllTranscations());
        lib.setMaxDaysBorrow(60);
        lib.setSubscribers(ob.getAllSubscribers());
        lib.setLibrarians(ob.getAllLibrarians());
        lib.setSchedule(ob.getSchedule());
        lib.setSpecimens(ob.getAllSpecimens());
        lib.setAvailability(ob.getSpecimenAvailability());
        System.out.println(lib.toString());
    }
}

