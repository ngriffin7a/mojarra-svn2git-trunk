package characterCombat;

import javax.faces.model.SelectItem;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ModelBean {

    public ModelBean() {
        //create List of Map instances for pre-defined characters
        populate();
    }


    List dataTable = null;

    public List getDataTable() {
System.out.println("getDataTable...");
        //TO DO: this method needs to be intelligent enough to omit
        //the first pick and second pick from the tables.

        return dataTable;
    }


    public List getCharactersToSelect() {
        List selectItemList = new ArrayList();
        Iterator iter = dataTable.iterator();
        SelectItem selectItem = null;

        while(iter.hasNext()) {
            CharacterBean item = (CharacterBean) iter.next();
            if (!item.getName().equals(firstSelection)) {
                selectItem = new SelectItem(item.getName());
                selectItemList.add(selectItem);
            }
        }

        return selectItemList;
    }


    public void setDataTable(List dataTable) {
        this.dataTable = dataTable;
    }
    

    String firstSelection = null;

    public String getFirstSelection() {
        if (firstSelection == null) {
            //We know our table will not be empty because we pre-populate

            //TO DO: make this more intelligent
            return ((CharacterBean) dataTable.get(0)).getName();
        }
        return firstSelection;
    }

    public void setFirstSelection(String firstSelection) {
System.out.println("setFirstSelection: " + firstSelection);
        this.firstSelection = firstSelection;
    }


    String secondSelection = null;

    public String getSecondSelection() {
        if (secondSelection == null) {
            //We know our table will not be empty because we pre-populate

            //TO DO: make this more intelligent
            return ((CharacterBean) dataTable.get(0)).getName();
        }
        return secondSelection;
    }

    public void setSecondSelection(String secondSelection) {
System.out.println("setSecondSelection: " + secondSelection);
        this.secondSelection = secondSelection;
    }


    private void populate() {
System.out.println("POPULATING......");
         dataTable = new ArrayList();

         CharacterBean item = new CharacterBean();
         item.setName("Gandalf");
         item.setSpecies("Istari");
         item.setLanguage("Common Speech");
         item.setImmortal(true);
         dataTable.add(item);

         item = new CharacterBean();
         item.setName("Frodo");
         item.setSpecies("Hobbit");
         item.setLanguage("Common Speech");
         item.setImmortal(false);
         dataTable.add(item);

         item = new CharacterBean();
         item.setName("Legolas");
         item.setSpecies("Elf");
         item.setLanguage("Quenya/Sindarin");
         item.setImmortal(false);
         dataTable.add(item);
    }
}
