package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Book;
import entities.Section;
import services.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SectionRepository {

    private static ArrayList<Section> Sections;

    public static void initSections() {
        Sections = new ArrayList<Section>();
    }

    public void initializeSectionsFromCSV() throws IOException, CsvException {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/sections.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            List<Section> csvObjectList = csvReader.readAll().stream().map(data-> {
                Section section = new Section();
                section.setId(Integer.parseInt(data[0].trim()));
                section.setLabel(data[1].trim());
                section.setBookshelf(data[2].trim().charAt(0));
                return section;
            }).collect(Collectors.toList());
            Sections.addAll(csvObjectList);
            csvReader.close();
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized sections from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Initialized sections from csv file. - SUCCESS");
    }

    public Section getSection(Integer id){
        for (Section section: Sections) {
            if (section.getId().equals(id))
                return section;
        }
        return null;
    }


    public Integer addSection(Section x) throws IOException {
        List<Integer> ids = Sections.stream().map(Section::getId).sorted(Comparator.comparing(Integer::valueOf)).
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
            FileWriter filewriter = new FileWriter("data/sections.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getLabel(), x.getBookshelf().toString()});
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("New section added in csv file. - FAILED");
            throw e;
        }
        Sections.add(x);
        Logger.logOperation("New section added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteSection(Integer id) throws Exception {
        for (Book book: BookRepository.getBooks()) {
            if (book.getSection().getId().equals(id)) {
                Logger.logOperation("Section removed from csv file. - FAILED");
                throw new Exception("This section is referenced in the Books database. " +
                        "A referenced object cannot be removed");
            }
        }
        Sections.removeIf(section -> section.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/sections.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Label", "Bookshelf"});
            for (Section sect: Sections) {
                writer.writeNext(new String[]{sect.getId().toString(), sect.getLabel(), sect.getBookshelf().toString()});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Section removed from csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Section removed from csv file. - SUCCESS");
    }

    public Section aboutSection(Integer id){
        for (Section section : Sections)
            if (section.getId().equals(id))
                return section;
        return null;
    }

    public static ArrayList<Section> getSections() {
        return Sections;
    }

    public void deleteSections() throws Exception {
        boolean fullDelete = Boolean.TRUE;
        for (Section section : Sections) {
            try {
                deleteSection(section.getId());
            } catch (Exception e) {
                fullDelete = Boolean.FALSE;
            }
        }
        if (fullDelete) {
            Logger.logOperation("Delete all sections. - SUCCESS");
        } else {
            Logger.logOperation("Delete all sections. - FAILED");
            throw new Exception("Some of the sections could not be deleted because they are referenced somewhere else. " +
                    "A referenced object cannot be removed");
        }
    }

    public void updateSection(Integer id, Section x) throws IOException {
        x.setId(id);
        for (int i=0;i<Sections.size();i++)
        {
            if(Sections.get(i).getId().equals(id)) {
                Sections.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/sections.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "Label", "Bookshelf"});
            for (Section sect: Sections) {
                writer.writeNext(new String[]{sect.getId().toString(), sect.getLabel(), sect.getBookshelf().toString()});
            }
            writer.close();
        } catch (IOException e) {
            Logger.logOperation("Section updated in csv file. - FAILED");
            throw e;
        }
        Logger.logOperation("Section updated in csv file. - SUCCESS");
    }

    public void sortSections()
    {
        Collections.sort(Sections);
    }
}