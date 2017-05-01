package input;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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
    private static String version = "1.1.10";
    /**
     * ID сессии, нужна для подключения
     */
    private static String sessionID;
    /**
     * Папка, куда будут складываться файлы с таблицами и графиками аналитических рассчетов
     */
    private static String dirForTables = ".";
    /**
     * Параметр показывает запускать ли программу в "серверном" режиме, когда постоянно висит в трее и регулярно перестраивает результаты.
     */
    private static boolean ServerMode = false;
    /**
     * Частота обновления таблиц.
     */
    private static int timeToRefreshChartInMins = 10;
    /**
     * Статусы тикетов, которые не будут обрабатываться
     */
    private static String wihoutState ;
    /**
     * Путь к bat-нику, который будет исполняться
     */
    private static String batFilePath;
    /**
     * Текущая директория. Получаем ее первым параметром при запуске программы.
     */
    private static String curDir = null;

    public static String getBatFilePath() {
        return batFilePath;
    }

    private static Params ourInstance = new Params();


    public static Params getInstance() {
        return ourInstance;
    }

    private Params() {
}
    private static final String sFileName = "otrs.properties";

    private static String sDirSeparator = System.getProperty("file.separator");

    private static Properties props = new Properties();

    public static void loadProperties(String propertDir) throws ParseException {
        // определяем текущий каталог
        File currentDir = null;
        if (propertDir != null) {
            currentDir = new File(propertDir);
            curDir = propertDir;
        } else {
            currentDir = new File(".");
        }
        try {

            // определяем полный путь к файлу
            String sFilePath = currentDir.getCanonicalPath() + sDirSeparator + sFileName;
            // создаем поток для чтения из файла
            FileInputStream ins = new FileInputStream(sFilePath);
            // загружаем свойства
            props.loadFromXML(ins);
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
        //получаем очередь, которую не считаем
        wihoutState = props.getProperty ("WITHOUT_STATE");
        // прописываем путь к файлу со списком пользователй
        fileWithUsers = props.getProperty ("LIST_OF_USERS");
        // выставляем параметр будем ли мы использовать сессии при загрузке тикетов
      //уже не выставляем и почти всегда используем сессии.
       if (props.getProperty("SERVER_MODE").equals("true")) {
            ServerMode = true;
        }
        //выставляем куда будем складывать графики с результатами расчетов.
        if(props.getProperty("DIRECTORY_FOR_CHARTS") != null) {
            dirForTables = props.getProperty("DIRECTORY_FOR_CHARTS");
        }
        if (props.getProperty("TIME_TO_REFRESG_CHARTS_MINS") != null) {
            timeToRefreshChartInMins = Integer.parseInt(props.getProperty("TIME_TO_REFRESG_CHARTS_MINS"));
        }
        batFilePath = props.getProperty("BAT_FILE_PATH");
    }
    public static HashSet<String> getUserListFromFile() {
        // загружаем список пользователей из файла
        HashSet<String> result = new HashSet<>();
        try {
            File f = new File(Params.getFileWithUsers());
            BufferedReader fin = new BufferedReader(new FileReader(f));
            String line;
            while ((line = fin.readLine()) != null) {
                line = line.replace("\"","");
                result.add(line.trim());
            }
            fin.close();

        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);

        }
        return result;
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

    public static String getSessionID() {
        return sessionID;
    }

    public static void setSessionID(String sessionID) {
        Params.sessionID = sessionID;
    }

    public static String getDirForTables() {
        return dirForTables;
    }

    public static void setDirForTables(String dirForTables) {
        Params.dirForTables = dirForTables;
    }

    public static boolean isServerMode() {
        return ServerMode;
    }

    public static int getTimeToRefreshChartInMins() {
        return timeToRefreshChartInMins;
    }

    public static String getWihoutState() {
        return wihoutState;
    }

    public static void setWihoutState(String wihoutState) {
        Params.wihoutState = wihoutState;
    }

    public static String getCurDir() {
        return curDir;
    }
}
