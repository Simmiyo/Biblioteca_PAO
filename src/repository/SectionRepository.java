package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Section;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

            List<Section> csvObjectList = csvReader.readAll().stream().map(data-> {
                Section section = new Section();
                section.setId(Integer.parseInt(data[0].trim()));
                section.setLabel(data[1].trim());
                section.setBookshelf(data[2].trim().charAt(0));
                return section;
            }).collect(Collectors.toList());
            this.Sections.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Section getSection(Integer id){
        for (Section section: Sections) {
            if (section.getId().equals(id))
                return section;
        }
        return null;
    }


    public void addSection(Section x){
        List<Integer> ids = Sections.stream().map(Section::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
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

    //ramas
    public void deleteSection(Section x){
        this.Sections.remove(x);
    }

    public Section aboutSection(Integer id){
        for (Section section : this.Sections)
            if (section.getId().equals(id))
                return section;
        return null;
    }

    public ArrayList<Section> getSections() {
        return Sections;
    }

    public void deleteSections(){
        while (!this.Sections.isEmpty())
            this.Sections.remove(0);
    }

    public void updateSection(Integer id, Section x){
        x.setId(id);
        for (int i=0;i<this.Sections.size();i++)
        {
            if(this.Sections.get(i).getId().equals(id)) {
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
        Collections.sort(Sections);
    }
}