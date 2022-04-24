package Game;

import Agent.*;
import Gencode.*;
import Materials.*;
import Field.*;
import Equipments.*;
import TestSets.TestInOutHandler;
import Virologist.Virologist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private static ArrayList<Steppable> steppables;
    private boolean testingFinished = false;
    private static boolean gameRunning = false;
    private static ArrayList<Virologist> virologists = new ArrayList<>();
    private ArrayList<Field> fields = new ArrayList<>();
    TestInOutHandler handler = new TestInOutHandler();

    public Game() {
        steppables = new ArrayList<>();
    }

    public static void addSteppable(Steppable s) {
        steppables.add(s);
    }

    /**
     * Initializes the map, and starts the game.
     * @param path path to the file that contains the map initialization
     */
    public void start(String path) {
        readFromFile(new File(path)); // reading from a file and initializing a map
        runGame(); // start running the game
    }

    public ArrayList<Virologist> getVirologists(){
        return virologists;
    }

    /**
     * Reads from a given file, and initializes a map
     * @param file the given file
     */
    private void readFromFile(File file) {
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                String input = scan.nextLine();

                // FIELD
                if(input.contains("fid")){
                    Field field = new Field();
                    fields.add(field);

                    // neighbours
                    String fieldNeighbors = scan.nextLine();
                    System.out.println(fieldNeighbors);
                    if(!fieldNeighbors.equals("0")) {
                        String[] split = fieldNeighbors.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            String parameter = split[i];
                            System.out.println("Creating connection: " + parameter);
                            checkAndAddNeighbors(parameter, field);
                        }
                    }

                    // virologists
                    readAfterField(scan, field);

                // LABORATORY
                } else if(input.contains("lab")){
                    Laboratory laboratory = new Laboratory();
                    fields.add(laboratory);

                    // neighbours
                    String fieldNeighbors = scan.nextLine();
                    if(!fieldNeighbors.equals("0")) {
                        String[] split = fieldNeighbors.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            String parameter = split[i];
                            checkAndAddNeighbors(parameter, laboratory);
                        }
                    }

                    // Bear agent on lab
                    String bearAgentOnLaboratory = scan.nextLine();
                    if(!bearAgentOnLaboratory.equals("0"))
                        if(bearAgentOnLaboratory.contains("baa"))
                            laboratory.add(new BearDanceAgent());

                    // Genetic Codes on lab
                    String codeOnLabor = scan.nextLine();
                    if(!codeOnLabor.equals("0")){
                        if(codeOnLabor.contains("amc")){
                            AmnesiaCode amnesiaCode = new AmnesiaCode(4, 4, laboratory);
                            laboratory.add(amnesiaCode);
                        } else if(codeOnLabor.contains("pac")){
                            ParalyseCode paralyseCode = new ParalyseCode(4, 4, laboratory);
                            laboratory.add(paralyseCode);
                        } else if(codeOnLabor.contains("cdc")){
                            CrazyDanceCode crazyDanceCode = new CrazyDanceCode(4, 4, laboratory);
                            laboratory.add(crazyDanceCode);
                        } else if(codeOnLabor.contains("prc")){
                            ProtecionCode protecionCode = new ProtecionCode(4, 4, laboratory);
                            laboratory.add(protecionCode);
                        }
                    }


                    // virologists
                    readAfterField(scan, laboratory);

                // WAREHOUSE
                } else if(input.contains("who")){
                    Warehouse warehouse = new Warehouse();
                    fields.add(warehouse);

                    // neighbours
                    String fieldNeighbors = scan.nextLine();
                    if(!fieldNeighbors.equals("0")){
                        String[] split = fieldNeighbors.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            String parameter = split[i];
                            checkAndAddNeighbors(parameter, warehouse);
                        }
                    }

                    // materials on warhouse
                    String materialOnWareHouse = scan.nextLine();
                    if(!materialOnWareHouse.equals("0")){
                        if(materialOnWareHouse.contains("aac")){
                            AminoAcid aminoAcid = new AminoAcid(warehouse);
                            warehouse.add(aminoAcid);
                        } else if(materialOnWareHouse.contains("ncd")){
                            Nucleotid nucleotid = new Nucleotid(warehouse);
                            warehouse.add(nucleotid);
                        }
                    }

                    // virologists
                    readAfterField(scan, warehouse);

                // SHELTER
                } else if(input.contains("shl")){
                    Shelter shelter = new Shelter();
                    fields.add(shelter);

                    // neighbours
                    String fieldNeighbors = scan.nextLine();
                    if(!fieldNeighbors.equals("0")) {
                        String[] split = fieldNeighbors.split(" ");
                        for (int i = 0; i < split.length; i++) {
                            String parameter = split[i];
                            checkAndAddNeighbors(parameter, shelter);
                        }
                    }

                    // equipment on shelter
                    String equipmentOnShelter = scan.nextLine();
                    if(!equipmentOnShelter.equals("0")){
                        if(equipmentOnShelter.contains("glv")){
                            Gloves gloves = new Gloves(shelter);
                            shelter.add(gloves);
                        } else if(equipmentOnShelter.contains("sck")){
                            Sack sack = new Sack(shelter);
                            shelter.add(sack);
                        } else if(equipmentOnShelter.contains("axe")){
                            Axe axe = new Axe(shelter);
                            shelter.add(axe);
                        } else if(equipmentOnShelter.contains("cpe")){
                            Cape cape = new Cape(shelter);
                            shelter.add(cape);
                        }
                    }

                    // virologists
                    readAfterField(scan, shelter);

                }
            }
            for(Field f: fields){
                System.out.println(f.toString()+"\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reading in virologists
     * @param scan
     * @param field
     */
    private void readAfterField(Scanner scan, Field field) {
        String viro = scan.nextLine();
        // repeatedly reading in virologists until the end of the field init
        do {
            // if there are virologists on the field
            if(!viro.equals("0")) {
                // create and add virologist to the game
                Virologist virologist = new Virologist(field);
                virologists.add(virologist); // adding it to the game
                field.addVirologist(virologist); // adding it to the field

                // materials in the inventory
                String materials = scan.nextLine();
                if (!materials.equals("0")) {
                    String splitMaterial[] = materials.split(" ");
                    for (int i = 0; i < splitMaterial.length; i++) {
                        if (splitMaterial[i].contains("aac")) {
                            AminoAcid aminoAcid = new AminoAcid();
                            virologist.getPropertyHandler().add(aminoAcid);
                        } else if (splitMaterial[i].contains(("ncd"))) {
                            Nucleotid nucleotid = new Nucleotid();
                            virologist.getPropertyHandler().add(nucleotid);
                        }
                    }
                }

                // agents in the inventory
                String agents = scan.nextLine();
                if (!agents.equals("0")) {
                    String splitAgents[] = agents.split(" ");
                    for (int i = 0; i < splitAgents.length; i++) {
                        if (splitAgents[i].contains("ama")) {
                            AmnesiaAgent amnesiaAgent = new AmnesiaAgent(4);
                            virologist.getPropertyHandler().add(amnesiaAgent);
                        } else if (splitAgents[i].contains("paa")) {
                            ParalyseAgent paralyseAgent = new ParalyseAgent();
                            virologist.getPropertyHandler().add(paralyseAgent);
                        } else if (splitAgents[i].contains("cda")) {
                            CrazyDanceAgent crazyDanceAgent = new CrazyDanceAgent(5);
                            virologist.getPropertyHandler().add(crazyDanceAgent);
                        } else if (splitAgents[i].contains("pra")) {
                            ProtectionAgent protectionAgent = new ProtectionAgent(5);
                            virologist.getPropertyHandler().add(protectionAgent);
                        }
                    }
                }

                // equipments in the inventory
                String equipments = scan.nextLine();
                if (!equipments.equals("0")) {
                    String splitEquipments[] = equipments.split(" ");
                    for (int i = 0; i < splitEquipments.length; i++) {
                        if (splitEquipments[i].contains("cpe")) {
                            Cape cape = new Cape();
                            virologist.getPropertyHandler().add(cape);
                        } else if (splitEquipments[i].contains("sck")) {
                            Sack sack = new Sack();
                            virologist.getPropertyHandler().add(sack);
                        } else if (splitEquipments[i].contains("glv")) {
                            Gloves gloves = new Gloves();
                            virologist.getPropertyHandler().add(gloves);
                        } else if (splitEquipments[i].contains("axe")) {
                            Axe axe = new Axe();
                            virologist.getPropertyHandler().add(axe);
                        }
                    }
                }

                // gencodes in the inventory
                String codes = scan.nextLine();
                if (!codes.equals("0")) {
                    String splitCodes[] = codes.split(" ");
                    for (int i = 0; i < splitCodes.length; i++) {
                        if (splitCodes[i].contains("amc")) {
                            AmnesiaCode amnesiaCode = new AmnesiaCode(4, 4);
                            virologist.getPropertyHandler().add(amnesiaCode);
                        } else if (splitCodes[i].contains("prc")) {
                            ProtecionCode protecionCode = new ProtecionCode(4, 4);
                            virologist.getPropertyHandler().add(protecionCode);
                        } else if (splitCodes[i].contains("cdc")) {
                            CrazyDanceCode crazyDanceCode = new CrazyDanceCode(1, 1);
                            virologist.getPropertyHandler().add(crazyDanceCode);
                        } else if (splitCodes[i].contains("pac")) {
                            ParalyseCode paralyseCode = new ParalyseCode(1, 2);
                            virologist.getPropertyHandler().add(paralyseCode);
                        }
                    }
                }

                scan.nextLine(); // reading in closing vir
            }

            viro = scan.nextLine(); //nextvir or fieldEnd

        } while(viro.startsWith("vir"));
    }

    private void initTest() {
        System.out.println("Give your commands fella");
        Scanner scan = new Scanner(System.in);
        while (!testingFinished) {
            String input = scan.nextLine();
            if (input.contains("CreateVirologist")) {
                String[] split = input.split(" ");
                String parameter = split[1];
                if (parameter.contains("lab") || parameter.contains("who") || parameter.contains("shl") || parameter.contains("fid")) {
                    createVirologistOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if (input.contains("CreateField")) {
                String[] parameters = input.split(" ");
                Field field = new Field();
                fields.add(field);
                for (int i = 1; i < parameters.length; i++) {
                    String parameter = parameters[i];
                    checkAndAddNeighbors(parameter, field);
                }
                System.out.println("Created Field: " + field.getName());
            } else if(input.contains("CreateLabor")){
                String[] parameters = input.split(" ");
                Laboratory laboratory = new Laboratory();
                fields.add(laboratory);
                for(int i = 1; i < parameters.length; i++){
                    String parameter = parameters[i];
                    checkAndAddNeighbors(parameter, laboratory);
                }
                System.out.println("Created Labor: " + laboratory.getName() + laboratory.getFields());
            } else if(input.contains("CreateShelter")){
                String[] parameters = input.split(" ");
                Shelter shelter = new Shelter();
                fields.add(shelter);
                for(int i = 1; i < parameters.length; i++){
                    String parameter = parameters[i];
                    checkAndAddNeighbors(parameter, shelter);
                }
                System.out.println("Created Shelter: " + shelter.getName());
            } else if(input.contains("CreateWareHouse")){
                String[] parameters = input.split(" ");
                Warehouse warehouse = new Warehouse();
                fields.add(warehouse);
                for(int i = 1; i < parameters.length; i++){
                    String parameter = parameters[i];
                    checkAndAddNeighbors(parameter, warehouse);
                }
                System.out.println("Created WareHouse: " + warehouse.getName());
            } else if(input.contains("CreateAmino")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if (parameter.contains("who")) {
                    createAminoOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateNucleo")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("who")){
                    createNucleoOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateGlove")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("shl")){
                    createGloveOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateCape")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("shl")){
                    createCapeOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateAxe")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("shl")){
                    createGloveOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateSack")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("shl")){
                    createSackOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateParalyseCode")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("lab")){
                    createParalyseCodeOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateAmnesiaCode")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("lab")){
                    createAmnesiaCodeOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateProtectionCode")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("lab")){
                    createProtectionCodeOnField(parameter);
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            } else if(input.contains("CreateCrazeyDanceCode")){
                String[] split = input.split(" ");
                String parameter = split[1];
                if(parameter.contains("lab")){
                    createCrazyDanceCodeOnField(parameter);
                } else if(input.contains("ListFields")){
                    System.out.println(Field.getFields());
                } else {
                    System.out.println("'" + parameter + "' is not a valid parameter");
                }
            }
        }
    }

    //TODO kiírásokat átnézni, mert szerintem nem az amit megadtunk
    private void createCrazyDanceCodeOnField(String parameter) {
        Laboratory laboratory = (Laboratory) findFieldByName(parameter);
        if (laboratory != null) {
            //TODO pontos értékek az amino, nucleonak
            ProtecionCode protecionCode = new ProtecionCode(4, 1, laboratory);
            laboratory.add(protecionCode);
            System.out.println("Created ProtectionCode with name " + protecionCode.toString() + ", on field: " + laboratory.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createProtectionCodeOnField(String parameter) {
        Laboratory laboratory = (Laboratory) findFieldByName(parameter);
        if (laboratory != null) {
            //TODO pontos értékek az amino, nucleonak
            CrazyDanceCode crazyDanceCode = new CrazyDanceCode(4, 1, laboratory);
            laboratory.add(crazyDanceCode);
            System.out.println("Created CrazyDanceCode with name " + crazyDanceCode.toString() + ", on field: " + laboratory.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createAmnesiaCodeOnField(String parameter) {
        Laboratory laboratory = (Laboratory) findFieldByName(parameter);
        if (laboratory != null) {
            //TODO pontos értékek az amino, nucleonak
            AmnesiaCode amnesiaCode = new AmnesiaCode(4, 1, laboratory);
            laboratory.add(amnesiaCode);
            System.out.println("Created AmnesiaCode with name " + amnesiaCode.toString() + ", on field: " + laboratory.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createParalyseCodeOnField(String parameter) {
        Laboratory laboratory = (Laboratory) findFieldByName(parameter);
        if (laboratory != null) {
            //TODO pontos értékek az amino, nucleonak
            ParalyseCode paralyseCode = new ParalyseCode(4, 1, laboratory);
            laboratory.add(paralyseCode);
            System.out.println("Created ParalyseCode with name " + paralyseCode.toString() + ", on field: " + laboratory.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createSackOnField(String parameter) {
        Shelter shelter = (Shelter) findFieldByName(parameter);
        if (shelter != null) {
            Sack sack = new Sack(shelter);
            shelter.add(sack);
            System.out.println("Created Sack with name " + sack.toString() + ", on field: " + shelter.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createCapeOnField(String parameter) {
        Shelter shelter = (Shelter) findFieldByName(parameter);
        if (shelter != null) {
            Cape cape = new Cape(shelter);
            shelter.add(cape);
            System.out.println("Created Cape with name " + cape.toString() + ", on field: " + shelter.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createGloveOnField(String parameter) {
        Shelter shelter = (Shelter) findFieldByName(parameter);
        if (shelter != null) {
            Gloves glove = new Gloves(shelter);
            shelter.add(glove);
            System.out.println("Created Glove with name " + glove.toString() + ", on field: " + shelter.toString());
        } else {
            System.out.println("Field does not exist with this name: " + parameter);
        }
    }

    private void createNucleoOnField(String fieldName) {
        Warehouse warehouse = (Warehouse)findFieldByName(fieldName);
        if (warehouse != null) {
            Nucleotid nucleotid = new Nucleotid(warehouse);
            warehouse.add(nucleotid);
            System.out.println("Created Nucleotid with name " + nucleotid.getName() + ", on field: " + warehouse.toString());
        } else {
            System.out.println("Field does not exist with this name: " + fieldName);
        }
    }

    private void createAminoOnField(String fieldName) {
        Warehouse warehouse = (Warehouse) findFieldByName(fieldName);
        if (warehouse != null) {
            AminoAcid aminoAcid = new AminoAcid(warehouse);
            warehouse.add(aminoAcid);
            System.out.println("Created AminoAcid with name " + aminoAcid.getName() + ", on field: " + warehouse.toString());
        } else {
            System.out.println("Field does not exist with this name: " + fieldName);
        }
    }

    private void checkAndAddNeighbors(String parameter, Field field){ //lab0 lab1
        if (parameter.contains("lab") || parameter.contains("fid") || parameter.contains("shl") || parameter.contains("who")) {
            for(Field f : fields){
                // if the given neighbour (parameter) is in the game already
                if(f.getName().equals(parameter))
                    field.addNeighbour(f); // just add it as a neighbour
            }
        } else if (!parameter.equals("")) {
            System.out.println("'" + parameter + "' is not a valid parameter.");
        }
    }

    private void createVirologistOnField(String fieldName) {
        Field field = findFieldByName(fieldName);
        if (field != null) {
            Virologist virologist = new Virologist(field);
            virologists.add(virologist);
            System.out.println("Created virologist with name " + virologist.getName() + ", on field: " + field.toString());
        } else {
            System.out.println("Field does not exist with this name: " + fieldName);
        }
    }
    public void addVirologistManually(Virologist virologist){
        virologists.add(virologist);
    }
    private Field findFieldByName(String nextString) {
        for (Field field : fields) {
            if (field.getName().equals(nextString)) {
                return field;
            }
        }
        return null;
    }

    /**
     * futtatja a játékot, sorban meghívja a virológusok yourTurn függvényét a fájl input paraméterekkel
     */
    public void runGame(){
        gameRunning = true;
        int roundCounter = 0;
        int currentCommandNum = 0;

        // gets the commands for the test
        String filecontent = handler.getInput();
        String [] allCommands = filecontent.split("\n"); // array with command lines

        // game loop
        while (gameRunning) {
            // 1 turn: goes through every virologist
            for (int i = 1; i <= virologists.size() && allCommands.length > i * roundCounter && gameRunning; ++i) {
                virologists.get(i-1).setActionCounter(2); // every virologist starts with 2 actions

                // TESTING - go through the commands
                while (virologists.get(i-1).getActionCounter() > 0 && allCommands.length > currentCommandNum) {//for(int j = 0; j < allCommands.length; ++j) {
                    // the current command
                    String[] currCommand = allCommands[currentCommandNum].split(" ");

                    // go through the virologists to find the one that has to the command
                    if (virologists.get(i - 1).getActionCounter() > 0 && currCommand[1].equals(virologists.get(i - 1).getName())) {
                        virologists.get(i - 1).yourTurn(allCommands[currentCommandNum]);
                        currentCommandNum++; // we use up a command
                    }
                }

                // stopping the game
                if (allCommands.length <= currentCommandNum + 1)
                    gameRunning = false;
            }
            stepSteppabbles();
            roundCounter++;
        }
    }
    public static void removeSteppable(Steppable s) {
        steppables.remove(s);
    }
    public static void removeVirologist(Virologist v){virologists.remove(v);}
    public void stepSteppabbles() {
        for (Steppable s : steppables) {
            s.step();
        }
    }

    public void endGame() {
        gameRunning=false;
        System.out.println("A jatek veget ert.");
        TestInOutHandler.appendToTestOutput("A jatek veget ert.");
    }
    public static void endGame(Virologist v) {
        gameRunning=false;
        System.out.println("A jatek veget ert.\tGyoztes: "+v.getName());
        TestInOutHandler.appendToTestOutput("A jatek veget ert.\tGyoztes: "+v.getName()+"\n");
    }

    public TestInOutHandler getHandler() { return handler; }

}
