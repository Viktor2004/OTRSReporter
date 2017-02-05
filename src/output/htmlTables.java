package output;



import calculate.WeekResult;
import input.Params;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Виктор on 21.01.2017.
 */
public class htmlTables {
    private static final Logger log = Logger.getLogger(htmlTables.class);
    private static final String sFileName = "week_template.htm";
    private static String sDirSeparator = System.getProperty("file.separator");
    private static File currentDir = new File(".");
    /**
     * Метод для построения отчета за неделю.
     * @param date - конечная дата, от нее назад 7 дней отсчитывается.
     */

    public static void buildWeekReport (Date date) throws Exception {
        log.info("========>build week report");
        File sourse = new File(currentDir.getCanonicalPath() + sDirSeparator +"templates"+sDirSeparator+sFileName);
        File target = new File (new File(Params.getDirForTables()) + sDirSeparator+"week.html");
        copy (sourse,target);
        WeekResult weekResult = new WeekResult(date);

        replaceInFile(target,"<WeekResult>",weekResult.toString());
        replaceInFile(target,"<CurrentDate>",new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" ).format ( date ));
        log.info(weekResult);
        log.info("<======== build week report");
    }

    public static void replaceInFile(File file, String before, String after) throws IOException {

        String search = before;
        String replace = after;
        Charset charset = StandardCharsets.UTF_8;
        Path path = Paths.get(file.getPath());
        Files.write(path,
                new String(Files.readAllBytes(path), charset).replace(search, replace)
                        .getBytes(charset));

    }

    private static void copy(File source, File dest) throws IOException {
        if (dest.exists()) {
            dest.delete();

        }
        Files.copy(source.toPath(), dest.toPath());
    }
}