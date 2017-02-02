package output;



import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;


/**
 * Created by Виктор on 21.01.2017.
 */
public class htmlTables {

    STGroup group = new STGroupDir("/templates");
    ST test = group.getInstanceOf("week");

    public void ReplaceInFile() throws IOException {

        String fileName = "file.txt";
        String search = "31415";
        String replace = "число ПИ";
        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(fileName);
        Files.write(path,
                new String(Files.readAllBytes(path), charset).replace(search, replace)
                        .getBytes(charset));

    }

    public static void copy(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
}