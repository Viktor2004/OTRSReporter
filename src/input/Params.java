package input;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Виктор on 02.01.2017.
 * Список всех параметров программы
 */
public class Params {
    private static final Logger log = Logger.getLogger(Params.class);

    /**
     * Начало периода
     */
    private static Date startDateInDate;
    /**
     * Конец периода
     */
    private static Date endDateInDate;
    /**
     * Местоположение файла со списком пользователей
     */
    private static String fileWithUsers;
    /**
     * Очередь, заявки из которой не учавствуют в рассчете
     */
    private static String withoutQueue;
    /**
     * Версия программы
     */
    private static String version = "1.0";
    private static Params ourInstance = new Params();


    public static Params getInstance() {
        return ourInstance;
    }

    private Params() {
}
    private static final String sFileName = "otrs.properties";

    private static String sDirSeparator = System.getProperty("file.separator");

    private static Properties props = new Properties();

    public static void loadProperties() throws ParseException {

        // определяем текущий каталог
        File currentDir = new File(".");
        try {
            // определяем полный путь к файлу
            String sFilePath = currentDir.getCanonicalPath() + sDirSeparator + sFileName;
            // создаем поток для чтения из файла
            FileInputStream ins = new FileInputStream(sFilePath);
            // загружаем свойства
            props.load(ins);
            ins.close();

        } catch (FileNotFoundException e) {
            log.error(e);
           /* System.out.println("File not found!");
            e.printStackTrace();*/
        } catch (IOException e) {
            log.error(e);
          /*  System.out.println("IO Error!");
            e.printStackTrace();*/

        }
        //сохраняем даты в формате Date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = props.getProperty("START_DATE")+" 00:00:00";
        startDateInDate = format.parse(dateString);
        dateString = props.getProperty("END_DATE")+" 23:59:59";
        endDateInDate = format.parse(dateString);
        //получаем очередь, которую не считаем
        withoutQueue = props.getProperty ("WITHOUT_QUEUE");
        // прописываем путь к файлу со списком пользователй
        fileWithUsers = props.getProperty ("LIST_OF_USERS");
        // выставляем параметр будем ли мы использовать сессии при загрузке тикетов


    }

    public static Properties getProps() {
        return props;
    }

    public static Date getEndDateInDate() {
        return endDateInDate;
    }


    public static void setEndDateInDate(Date endDateInDate) {
        Params.endDateInDate = endDateInDate;
    }

    public static Date getStartDateInDate() {
        return startDateInDate;
    }

    public static void setStartDateInDate(Date startDateInDate) {
        Params.startDateInDate = startDateInDate;
    }

    public static String getWithoutQueue() {
        return withoutQueue;
    }

    public static void setWithoutQueue(String withoutQueue) {
        Params.withoutQueue = withoutQueue;
    }

    public static String getFileWithUsers() {
        return fileWithUsers;
    }

    public static void setFileWithUsers(String fileWithUsers) {
        Params.fileWithUsers = fileWithUsers;
    }

    public static String getVersion() {
        return version;
    }
}
