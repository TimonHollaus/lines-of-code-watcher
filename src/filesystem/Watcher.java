package filesystem;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

public abstract class Watcher implements Runnable, Closeable {
    private final Path path;
    private final WatchService ws;
    private final WatchKey key;

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
            files.forEach(p -> this.handleChange(p.toFile(), true));
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
                handleChange(p.toFile(), Files.exists(p));
            }
            key.reset();
        }
    }

    public abstract void handleChange(File file, boolean exists);
}
