import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class ScanM2 {

    public static void main(String... args) throws IOException {
        FileTime oneMonthAgo = FileTime.from(Instant.now().minus(30, ChronoUnit.DAYS));
        AtomicInteger nbFiles = new AtomicInteger();
        AtomicLong size = new AtomicLong();
        try (Stream<Path> stream = Files.walk(Paths.get(System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository"))) {
            stream
                    .filter(path -> !Files.isDirectory(path))
                    .filter(file -> {
                        try {
                            boolean rc = Files.getFileAttributeView(file, BasicFileAttributeView.class).readAttributes().lastAccessTime().compareTo(oneMonthAgo) < 0;
                            if (rc) {
                                nbFiles.getAndIncrement();
                                size.addAndGet(Files.getFileAttributeView(file, BasicFileAttributeView.class).readAttributes().size());
                            }
                            return rc;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Error while deleting " + path + " :" + e.getLocalizedMessage());
                        }
                    });
        }
        System.out.println("Deleted " + nbFiles.get() + " files freed " + size.get() + " bytes");
    }
}
