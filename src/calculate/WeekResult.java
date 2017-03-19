package calculate;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Виктор on 05.02.2017.
 */
public class WeekResult extends CalcResult {
    private static final Logger log = Logger.getLogger(WeekResult.class);
    List<DayResult> results = new ArrayList<>();

    public List<DayResult> getResults() {
        return results;
    }

    public void setResults(List<DayResult> results) {
        this.results = results;
    }

    public WeekResult(List<DayResult> results) {
        this.results = results;
    }

    public WeekResult (Date date) throws Exception {
        int i = 0;
        while (i<7) {
            try {
                DayResult dayResult = new DayResult(date);
                dayResult.fillFromBase();
                i++;
                results.add(dayResult);
                //уменьшаем дату на 1 день
                Calendar instance = Calendar.getInstance();
                instance.setTime(date); //устанавливаем дату, с которой будет производить операции
                instance.add(Calendar.DAY_OF_MONTH, -1);// отнимаем 1 день от установленной даты
                date = instance.getTime(); // получаем измененную дату
            }catch (Exception e) {
                log.error("Скорее всего ошибка соединения с базой.",e);
            }

       }
    }

    public WeekResult (Date date, HashSet<String> selectedUsers) throws Exception {
        int i = 0;
        while (i<7) {
            try {
                DayResult dayResult = new DayResult(date);
                dayResult.fillFromBaseForUsergroup(selectedUsers);
                i++;
                results.add(dayResult);
                //уменьшаем дату на 1 день
                Calendar instance = Calendar.getInstance();
                instance.setTime(date); //устанавливаем дату, с которой будет производить операции
                instance.add(Calendar.DAY_OF_MONTH, -1);// отнимаем 1 день от установленной даты
                date = instance.getTime(); // получаем измененную дату
            }catch (Exception e) {
                log.error("Скорее всего ошибка соединения с базой.",e);
            }

        }
    }
    @Override
    public String toString() {
        java.util.Collections.sort(results);
        java.util.Collections.reverse(results);
        String result = "";
        String formatString = "['%s', %d, %d, %d], \n";
        for (DayResult element :results ) {
            result = result+ String.format(formatString,
                                            element.getFormatedDay(),
                                            element.getTotalOpened(),
                                            element.getTotalClosed(),
                                            element.getTotalClosedInSla());
        }

        return result;
    }
    public int totalOpened(){
        int result = 0;
        for (DayResult item : results) {
            result = result+item.getTotalOpened();
        }
        return result;
    }
    public int totalClosed(){
        int result = 0;
        for (DayResult item : results) {
            result = result+item.getTotalClosed();
        }
        return result;
    }
    public int totalClosediinSLA(){
        int result = 0;
        for (DayResult item : results) {
            result = result+item.getTotalClosedInSla();
        }
        return result;
    }
    public int totalUncallsified(){
        int result = 0;
        for (DayResult item : results) {
            result = result+item.getTotalUnclassified();
        }
        return result;
    }
}
