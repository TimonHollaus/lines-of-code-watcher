package filesystem;

import java.io.File;
import java.io.IOException;

public interface Observer {
    public void update(File file, boolean exists) throws IOException;
}
