package at.ac.hcw.alertaprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

public class WebAlertManager {
    private static ArrayList<WebAlert> webAlerts = new ArrayList<>();
    private static int nextId = 1; // Class variable for automatic id distribution
    static final String FILE_PATH = "webalerts.json";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
//            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // NEU!
            .create();

    public static void createWebAlert(String name, String url, String cssSelector) {
        try {
            loadFromFile(); //das ist wichtig! Es lädt die bestehende webalerts.json file und setzt nextId auf den richtigen Wert

            WebAlert webAlert = new WebAlert(nextId, name, url, cssSelector);
            webAlerts.add(webAlert);
            System.out.println("WebAlert '" + name + "' erfolgreich erstellt und gespeichert!");
            nextId++; // ensure that next id will be +1
            saveToFile(webAlerts);
            System.out.println("Next ID: " + nextId);
        } catch (IOException e) {
            System.out.println("Fehler bei '" + name + "': " + e.getMessage());
        }
    }

    public static boolean updateWebAlert(int id, String newName, String newUrl, String newCssSelector) {
        loadFromFile(); // IMPORTANT

        for (WebAlert webAlert : webAlerts) {
            if (webAlert.getId() == id) {
                try {
                    webAlert.setName(newName);
                    webAlert.setUrl(newUrl);
                    webAlert.setCssSelector(newCssSelector);
                    webAlert.setPreviousValue(webAlert.getCurrValue()); //LL: damit man den vorherigen Wert für Vergleiche heranziehen kann
                    webAlert.setCurrentValue(webAlert.getCurrentValue());
                    saveToFile(webAlerts);
                    System.out.println("WebAlert ID " + id + " erfolgreich aktualisiert!");
                    return true;
                } catch (IOException e) {
                    System.out.println("Fehler beim Aktualisieren von WebAlert ID " + id + ": " + e.getMessage());
                    return false;
                }
            }
        }

        System.out.println("WebAlert mit ID " + id + " nicht gefunden!");
        return false;
    }

    public static void deleteAllAlerts(){
        webAlerts.clear();
        saveToFile(webAlerts);
        nextId = 1;
    }

    public static void deleteWebAlert(int id) {
        for(int i = 0; i < webAlerts.size() ;i++) {
            if(id == webAlerts.get(i).getId()) {
                webAlerts.remove(i);
                saveToFile(webAlerts);
                break;
            }
        }
    }

    public static void showAllAlerts() {
        System.out.println("Gespeicherte WebAlerts:");
        for(int i = 0; i < webAlerts.size() ;i++) {
            WebAlert webAlert = webAlerts.get(i);
            try {
                System.out.println("ID: " + webAlert.getId());
                System.out.println("Name: " + webAlert.getName());
                System.out.println("URL: " + webAlert.getUrl());
                System.out.println("CSS-Selector: " + webAlert.getCssSelector());
                System.out.println("Original: " + webAlert.getPreviousValue());
                System.out.println("Aktuell: " + webAlert.getCurrentValue());
                System.out.println("Erstellt am: " + webAlert.getStringCreatedAt());
            } catch (IOException e) {
                System.out.println("Fehler beim Abrufen!");
            }
        }
    }

    public static void loadFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("Keine gespeicherten WebAlerts gefunden.");
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<WebAlert>>(){}.getType();
            webAlerts = GSON.fromJson(reader, listType);

            if (webAlerts == null) {
                webAlerts = new ArrayList<>();
            } else {
                // Update nextId to be higher than any existing ID
                for (WebAlert webAlert : webAlerts) {
                    if (webAlert.getId() >= nextId) {
                        nextId = webAlert.getId() + 1;
                    }
                }
                System.out.println(webAlerts.size() + " WebAlerts erfolgreich geladen!");
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }
    }

    public static void saveToFile(ArrayList<WebAlert> webAlerts) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(webAlerts, writer);
            System.out.println("WebAlerts erfolgreich gespeichert!");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public static boolean updatePrices() throws IOException {
        boolean newPriceFound = false;
        for(WebAlert alert : webAlerts){
            if (!alert.getCurrentValue().equals(alert.getPreviousValue())){
                alert.setCurrentValue(alert.getCurrentValue());
                newPriceFound = true;
            }
        }
        return newPriceFound;
    }

//we need here to ensure some data is pulled from the jason to be edited...it was empty previously
    public static WebAlert getWebAlertById(int id) {
        loadFromFile(); // make sure webAlerts is in sync with webalerts.json
        for (WebAlert a : webAlerts) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}
