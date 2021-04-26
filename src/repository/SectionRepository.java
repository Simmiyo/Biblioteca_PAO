package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Author;
import entities.Section;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SectionRepository {
    ArrayList<Section> Sections = new ArrayList<Section>();


    public SectionRepository(){

    }

    public void InitializeSectionsFromCSV() {
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
            List<String[]> allData = csvReader.readAll();

            List<Section> csv_objectList = csvReader.readAll().stream().map(data-> {
                Section section = new Section();
                section.setId(Integer.parseInt(data[0]));
                section.setLabel(data[1]);
                section.setBookshelf(data[2].charAt(0));
                return section;
            }).collect(Collectors.toList());
            this.Sections.addAll(csv_objectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Section getSection(int i){
        return this.Sections.get(i);
    }


    public void addSection(Section x){
        Boolean exists = Boolean.FALSE;
        ArrayList<Integer> ids = new ArrayList<>();
        for (Section sect: Sections
        ) {
            if (sect.equals(x)) {
                exists = Boolean.TRUE;
                break;
            }
            ids.add(sect.getId());
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
                FileWriter filewriter = new FileWriter("data/sections.csv", true);
                CSVWriter writer = new CSVWriter(filewriter);
                writer.writeNext(new String[]{x.getId().toString(), x.getLabel(), x.getBookshelf().toString()});
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.Sections.add(x);
        }
    }

    //ramas
    public void deleteSection(Section x){
        this.Sections.remove(x);
    }

    public Section aboutSection(String label){
        for (int i=0;i<this.Sections.size();i++)
            if(this.Sections.get(i).getLabel()==label)
                return this.Sections.get(i);
        return null;
    }

    public ArrayList<Section> getSections() {
        return Sections;
    }

    public void deleteSections(){
        while (this.Sections.isEmpty()!=true)
            this.Sections.remove(0);
    }

    public void updateSection(Integer id, Section x){
        x.setId(id);
        for (int i=0;i<this.Sections.size();i++)
        {
            if(this.Sections.get(i).getId() == id) {
                this.Sections.set(i, x);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortSections()
    {
        getSections().sort(Comparator.comparing(Section::getLabel));
    }
}