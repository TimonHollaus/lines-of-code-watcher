package filesystem;

import java.io.File;
import java.io.IOException;

public class LoggingWatcher extends Watcher {
    public static void main(String[] args) {
        // Watcher kann mit try-with-resources verwendet werden:
//        try(
//                Watcher watcher = new LoggingWatcher(new File("./watched"));
//        ) {
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        // ... dann wird der Watcher automatisch wieder beendet

        // wenn der Watcher bestehen bleiben (und das Programm endlos weiterlaufen) soll,
        // kann ein Watcher auch ohne try-with-resources verwendet werden (trotz Warnung):
        Watcher watcher = new LoggingWatcher(new File("./watched"));
        Anzeige anzeige = new Anzeige();
        watcher.attach(anzeige);
    }

    public LoggingWatcher(File dir) {
        super(dir);
    }

    @Override
    public void handleChange(File file, boolean exists) {
        notifyObservers(file,exists);
    }
}
