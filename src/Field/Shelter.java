package Field;

import Equipments.Equipment;

import java.util.ArrayList;

/**
 * A menedéket reprezentáló osztály
 */
public class Shelter extends Field{
    private static int id = 0;
    private ArrayList<Equipment> equipments = new ArrayList<>();

    /**
     * Shelter constructor
     */
    public Shelter(){
        this.name = "shl" + id++;
        fields.add(this);
    }

    /**
     * Hozzáadja a currEquipment-hez a paraméterként kapott felszerelést
     * @param equipment         Ezt adja hozzá
     */
    public void add(Equipment equipment){
        equipments.add(equipment);
    }

    /**
     * Eltávolítja a currEquipment-ből a paraméterként kapott felszerelést
     * @param equipment         Ezt veszi el
     */
    public void remove(Equipment equipment){
        equipments.remove(equipment);
    }

    /**
     * A currEquipment getterje
     * @return                  Visszaadja equipments-et
     */
    public ArrayList<Equipment> getEquipments(){
        return equipments;
    }
}
