package com.nyutiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Scanner;

public class Database {
    private List<Data> dataList = new ArrayList<>();
    private List<String> descriptors = new ArrayList<>();
    static final String baseFile = "database.txt";
    static int key = 0;

    public static void main(String[] args) throws Exception {

        String password = "Nyutiz";
        key = 0;
        for (int i = 0; i < password.length(); i++) {
            int asciiValue = password.charAt(i);
            key = key + asciiValue;
        }
        System.out.println(key);
        Crypter.encryptFile(baseFile, key);
        Crypter.decryptFile(baseFile, key);
        Database.start();
    }

    private static void start() throws Exception {
        Database database = new Database();

        database.loadDatabase();
        Scanner scanner = new Scanner(System.in);

        System.out.println("1 Créer un compte");
        System.out.println("2 Modifier un compte");
        System.out.println("3 Afficher les comptes");
        int select;
        String id;
        select = scanner.nextInt();
        scanner.nextLine();

        switch (select) {
            case 1:
                id = database.generateRandomId();
                System.out.println("Id : " + id);
                Data newAccount = new Data();
                newAccount.setData("Id", id);

                for (String descriptor : database.getDescriptors()) {
                    if (!descriptor.equals("Id")) {
                        System.out.print(descriptor + " : ");
                        String value = scanner.nextLine();
                        newAccount.setData(descriptor, value);
                    }
                }

                database.addData(newAccount);
                database.displayData();
                start();
                break;
            case 2:
                System.out.print("Id : ");
                id = scanner.nextLine();
                System.out.println("Modifier ");
                for (int i = 1; i < database.getDescriptors().size(); i++) {
                    System.out.println((i) + " " + database.getDescriptors().get(i));
                }
                int modifyChoice = scanner.nextInt();
                scanner.nextLine();

                if (modifyChoice > 0 && modifyChoice < database.getDescriptors().size()) {
                    String fieldToModify = database.getDescriptors().get(modifyChoice);
                    System.out.print("Nouveau " + fieldToModify + ": ");
                    String newValue = scanner.nextLine();
                    database.updateData(id, fieldToModify, newValue);
                } else {
                    System.out.println("Choix invalide");
                }
                start();
                break;
            case 3:
                database.displayData();
                start();
                break;
            default:
                Crypter.encryptFile(baseFile, key);
                System.exit(0);
        }
    }

    public List<String> getDescriptors() {
        return descriptors;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public String generateRandomId(){
        Random random = new Random();
        while (true) {
            int id = 1000000 + random.nextInt(9000000);
            if (!isIdExists(String.valueOf(id))) {
                return String.valueOf(id);
            }
        }
    }

    public void loadDatabase() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(baseFile))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (i == 0) {
                    // Lire les descripteurs
                    for (String part : parts) {
                        descriptors.add(part);
                    }
                    i++;
                    continue;
                }
                Data data = new Data();
                for (int j = 0; j < parts.length; j++) {
                    data.setData(descriptors.get(j), parts[j]);
                }
                dataList.add(data);
                i++;
            }
        }
    }

    public void saveDatabase() throws Exception {
        try (FileWriter writer = new FileWriter(baseFile)) {
            writer.write(String.join(";", descriptors) + "\n");
            for (Data data : dataList) {
                List<String> values = new ArrayList<>();
                for (String descriptor : descriptors) {
                    values.add(data.getData(descriptor));
                }
                writer.write(String.join(";", values) + "\n");
            }
        }
    }

    public boolean isIdExists(String id) {
        return findClientById(id) != null;
    }


    public Data findClientById(String id) {
        for (Data data : dataList) {
            if (id.equals(data.getData(descriptors.get(0)))) {
                return data;
            }
        }
        return null;
    }

    public void addData(Data data) throws Exception {
        dataList.add(data);
        saveDatabase();
        System.out.println("Nouveau client ajouté.");
    }

    public void updateData(String id, String key, String newValue) throws Exception {
        Data client = findClientById(id);
        if (client != null) {
            client.setData(key, newValue);
            saveDatabase();
            System.out.println(key + " mis à jour.");
        } else {
            System.out.println("Aucun client avec l'ID '" + id + "' trouvé.");
        }
    }

    public void displayData() {
        for (Data data : dataList) {
            for (String descriptor : descriptors) {
                System.out.print(data.getData(descriptor) + "; ");
            }
            System.out.println();
        }
    }
}
