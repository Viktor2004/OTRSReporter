package calculate;

import client.WebServiceClient;
import input.Params;
import input.Soap;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Виктор on 05.02.2017.
 */
public class DayResult extends CalcResult implements Comparable {
    private static final Logger log = Logger.getLogger(DayResult.class);
    private Date day;
    private String formatedDay;
    public Date getDay()  {
        return day;

    }

    public void setDay(Date day) {
        this.day = day;
    }

    public DayResult(Date day) {
        this.day = day;

    }

    public int compareTo(DayResult o) {
        int result = this.day.compareTo(o.day);

        return result;
    }

    public void fillFromBase () throws Exception {
        log.info("========>fill from base day result");
        while (Params.getSessionID() == null){
            Soap.makeSession();
            log.info("Создаем сессию");
        }



        //получаем дату без часов минут и секунд
        Date startDate = day;
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startDate);
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        startDateCalendar.set(Calendar.MILLISECOND, 0);
        startDate = startDateCalendar.getTime();

        Date endDate = day;
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(endDate);
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endDateCalendar.set(Calendar.MINUTE, 59);
        endDateCalendar.set(Calendar.SECOND, 59);
        endDateCalendar.set(Calendar.MILLISECOND, 999);
        endDate = endDateCalendar.getTime();

        CalcResult tempCalc = Calc.countSla(Soap.fillTicketFromSoapMessage(WebServiceClient.getTicketByIdAndSessionID(Soap.getAllOpenAndClosedTicketsInPeriod(startDate,endDate), Params.getSessionID())),startDate,endDate) ;

        this.setSlaLevel(tempCalc.getSlaLevel());
        this.setTotalOpened(tempCalc.getTotalOpened());
        this.setTotalClosed(tempCalc.getTotalClosed());
        this.setTotalClosedInSla(tempCalc.getTotalClosedInSla());
        this.setTotalUnclassified(tempCalc.getTotalUnclassified());
        log.info("<========fill from base day result");
    }
    public void fillFromBaseForUsergroup(HashSet<String> selectedUsers) throws Exception {
         fillFromBaseForUsergroup(selectedUsers,true);
    }
    public void fillFromBaseForUsergroup(HashSet<String> selectedUsers,boolean includeUsers) throws Exception {
        log.info("========>fill from base day group result");
        while (Params.getSessionID() == null){
            Soap.makeSession();
            log.info("Создаем сессию");
        }
        //получаем дату без часов минут и секунд
        Date startDate = day;
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startDate);
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        startDateCalendar.set(Calendar.MILLISECOND, 0);
        startDate = startDateCalendar.getTime();

        Date endDate = day;
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(endDate);
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endDateCalendar.set(Calendar.MINUTE, 59);
        endDateCalendar.set(Calendar.SECOND, 59);
        endDateCalendar.set(Calendar.MILLISECOND, 999);
        endDate = endDateCalendar.getTime();

        CalcResult tempCalc = Calc.countSlaForUserGroup(Soap.fillTicketFromSoapMessage(WebServiceClient.getTicketByIdAndSessionID(Soap.getAllOpenAndClosedTicketsInPeriod(startDate,endDate), Params.getSessionID())),selectedUsers,startDate,endDate, includeUsers) ;

        this.setSlaLevel(tempCalc.getSlaLevel());
        this.setTotalOpened(tempCalc.getTotalOpened());
        this.setTotalClosed(tempCalc.getTotalClosed());
        this.setTotalClosedInSla(tempCalc.getTotalClosedInSla());
        this.setTotalUnclassified(tempCalc.getTotalUnclassified());
        log.info("<========fill from base day group result");
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public String getFormatedDay() {
        return new SimpleDateFormat( "EE" ).format ( day );

    }

    public String getDayAndDate() {
        return new SimpleDateFormat( "EE(dd.MM)" ).format ( day );

    }


}
