package filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anzeige implements Observer {

    private List<File> files;
    private Map<String, Integer> fileEndingCount;

    public Anzeige() {
        this.files = new ArrayList<>();
        this.fileEndingCount = new HashMap<>();
    }

    @Override
    public void update(File file, boolean exists) {
        if (exists && !files.contains(file)) {
            this.files.add(file);
            anzeigen();
        } else {
            this.files.remove(file);
            anzeigen();
        }
    }

    public String getFileEnding(File file) {
        String name = file.getName();
        String[] parts = name.split("\\.");

        return parts[parts.length - 1];
    }

    private void anzeigen() {
        System.out.println("\n\nDateien");
        System.out.println("-----------------------------------");

        // Aktualisiere die Map basierend auf der aktuellen Liste von Dateien
        fileEndingCount.clear();
        for (File file : files) {
            String fileEnding = getFileEnding(file);
            fileEndingCount.put(fileEnding, fileEndingCount.getOrDefault(fileEnding, 0) + 1);
        }

        // Gib die Inhalte der Map aus
        for (Map.Entry<String, Integer> entry : fileEndingCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}


