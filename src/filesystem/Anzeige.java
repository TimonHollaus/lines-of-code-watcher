package filesystem;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Anzeige implements Observer {

    private List<File> files;
    private Map<String, Integer> fileEndingCount;

    public Anzeige() {
        this.files = new ArrayList<>();
        this.fileEndingCount = new HashMap<>();
    }

    @Override
    public void update(File file, boolean exists) throws IOException {
        if (exists && !files.contains(file)) {
            // Datei existiert und ist noch nicht in der Liste, also hinzufügen
            this.files.add(file);
        } else if (!exists) {
            // Datei existiert nicht mehr, also entfernen
            this.files.remove(file);
        }
        // Ausgabe aktualisieren
        anzeigen();
    }

    public String getFileEnding(File file) {
        String name = file.getName();
        String[] parts = name.split("\\.");

        return parts[parts.length - 1];
    }

    private void anzeigen() throws IOException {
        System.out.println("\n\n        Dateien  Zeilen  Code  Leerzeilen  Kommentare");
        System.out.println("-----------------------------------------------------");

        fileEndingCount.clear();
        Map<String, int[]> fileStats = new LinkedHashMap<>();

        for (File file : files) {
            String fileEnding = getFileEnding(file);
            int[] counts = countLines(file);

            fileStats.merge(fileEnding, counts, (oldCounts, newCounts) -> new int[]{
                    oldCounts[0] + newCounts[0],
                    oldCounts[1] + newCounts[1],
                    oldCounts[2] + newCounts[2],
                    oldCounts[3] + newCounts[3]
            });

            fileEndingCount.put(fileEnding, fileEndingCount.getOrDefault(fileEnding, 0) + 1);
        }

        for (Map.Entry<String, int[]> entry : fileStats.entrySet()) {
            String fileEnding = entry.getKey();
            int fileCount = fileEndingCount.getOrDefault(fileEnding, 0);
            int[] counts = entry.getValue();

            String output = auffüllen(fileEnding, 10) +
                    auffüllen(String.valueOf(fileCount), 10) +
                    auffüllen(String.valueOf(counts[0]), 10) +
                    auffüllen(String.valueOf(counts[1]), 10) +
                    auffüllen(String.valueOf(counts[2]), 13) +
                    auffüllen(String.valueOf(counts[3]), 10);
            System.out.println(output);
        }
    }


    private String auffüllen(String text,int length){
        String back = text;
        while(back.length() < length){
            back += " ";
        }
        return back;
    }

    public static int[] countLines(File file) throws IOException {
        String fileExtension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        CommentChecker checker = CommentTypeChooser.getcheckertype(fileExtension);

        int totalLines = 0;
        int codeLines = 0;
        int emptyLines = 0;
        int commentLines = 0;
        boolean inMultiLineComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                line = line.trim();

                if (line.isEmpty()) {
                    emptyLines++;
                } else if (inMultiLineComment) {
                    commentLines++;
                    if (checker.endsMultiLineComment(line)) {
                        inMultiLineComment = false;
                    }
                } else if (checker.isComment(line)) {
                    commentLines++;
                } else if (checker.startsMultiLineComment(line)) {
                    commentLines++;
                    inMultiLineComment = true;
                } else {
                    codeLines++;
                }
            }
        }

        return new int[]{totalLines, codeLines, emptyLines, commentLines};
    }

}


