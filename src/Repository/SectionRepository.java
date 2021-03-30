package Repository;

import Entities.Section;

import java.util.ArrayList;
import java.util.Comparator;

public class SectionRepository {
    ArrayList<Section> Sections = new ArrayList<Section>();


    public SectionRepository(){

    }

    public Section getSection(int i){
        return this.Sections.get(i);
    }


    public void addSection(Section x){
        this.Sections.add(x);
    }
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

    public void updateSection(String label, Section x){
        for (int i=0;i<this.Sections.size();i++)
        {
            if(this.Sections.get(i).getLabel() == label) {
                this.Sections.set(i, x);
                break;
            }
        }
    }

    public void sortSections()
    {
        getSections().sort(Comparator.comparing(Section::getLabel));
    }
}
