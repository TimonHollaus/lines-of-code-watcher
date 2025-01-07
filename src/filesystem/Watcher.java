package filesystem;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

public abstract class Watcher implements Runnable, Closeable {
    private final Path path;
    private final WatchService ws;
    private final WatchKey key;
    private List<Observer> beobachter = new ArrayList();

    public Watcher(File dir) {
        FileSystem fs = FileSystems.getDefault();
        path = dir.toPath();

        try {
            ws = fs.newWatchService();
            key = path.register(ws, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        new Thread(this).start();
    }


    @Override
    public void close() throws IOException {
        key.cancel();
        ws.close();
    }

    @Override
    public void run() {
        try(Stream<Path> files = Files.list(path)) {
            files.forEach(p -> {
                try {
                    this.handleChange(p.toFile(), true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        while (true) {
            WatchKey key;
            try {
                key = ws.take();
            } catch (ClosedWatchServiceException e) {
                break;
            } catch (InterruptedException e) {
                continue;
            }
            assert key == this.key;
            for (WatchEvent<?> event : key.pollEvents()) {
                Path p = path.resolve((Path) event.context());
                try {
                    handleChange(p.toFile(), Files.exists(p));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            key.reset();
        }
    }

    public void notifyObservers(File file, boolean exists) throws IOException {
        for (Observer observer : beobachter) {
            observer.update(file, exists);
        }
    }

    public void attach(Observer observer) {
        beobachter.add(observer);
    }

    public void detach(Observer observer) {
        beobachter.remove(observer);
    }

    public abstract void handleChange(File file, boolean exists) throws IOException;
}
