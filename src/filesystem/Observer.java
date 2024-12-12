package filesystem;

import java.io.File;

public interface Observer {
    public void update(File file, boolean exists);
}
