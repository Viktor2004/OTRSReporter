package calculate;

import input.Params;
import org.apache.log4j.Logger;
import ticket.TicketImpl;



import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Виктор on 07.01.2017.
 */
public class Calc {
    private static final Logger log = Logger.getLogger(Calc.class);

    /**
     * Рассчитывает SLA для списка тикетов в указанном диапазоне дат.
     * @param ticketList - список всех тикетов
     * @param startDate - начальная дата для подсчета
     * @param endDate - конечная дата для подсчета
     * @return
     */
    public static CalcResult countSla (List<TicketImpl> ticketList, Date startDate, Date endDate) {
        CalcResult result = new CalcResult();
        for (TicketImpl ticket : ticketList ){
            if (!ticket.getQueue().equals(Params.getWithoutQueue())&& !ticket.getStateType().equals(Params.getWihoutState())) {
                if (ticket.getStateType().equals("closed")) {
                    if (ticket.getCloseDate() != null && (ticket.getCloseDate().before(endDate))) {
                        result.increaseTotalClosed();
                    }
                    if (ticket.getSolutionDiffInMin() != null
                            && Integer.parseInt(ticket.getSolutionDiffInMin()) > 0
                            && (ticket.getCloseDate().before(endDate))) {
                        result.increaseTotalClosedInSla();
                    }
                    if (ticket.getCreateDate() != null && startDate.before(ticket.getCreateDate())) {
                        result.increaseTotalOpened();
                    }
                    if (ticket.getSolutionDiffInMin() == null) {
                        result.increaseTotalUnclassified();
                    }

                } else {
                    result.increaseTotalOpened();
                }
            }
        }
        return result;
    }


    /**
     * Рассчитывает SLA для тех тикетов, которые пришли от определенной группы лиц
     * @param ticketList - тикеты для подсчета
     * @param selectedUsers - группа пользователей, для которыйх считается
     * @param startDate - начальная дата для подсчета
     * @param endDate - конечная дата для подсчета
     * @return
     */
    public static CalcResult countSlaForUserGroup (List<TicketImpl> ticketList,  HashSet<String> selectedUsers, Date startDate, Date endDate) {
        CalcResult result = new CalcResult();
   /*     // загружаем список пользователей из файла
        HashSet<String> selectedUsers = new HashSet<>();
        try {
            File f = new File(Params.getFileWithUsers());
            BufferedReader fin = new BufferedReader(new FileReader(f));
            String line;
            while ((line = fin.readLine()) != null) {
                selectedUsers.add(line.trim());
            }
            fin.close();

        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);

        }*/
        for (TicketImpl ticket : ticketList ){
            if (!ticket.getQueue().equals(Params.getWithoutQueue())&& !ticket.getStateType().equals(Params.getWihoutState())) {
                if (selectedUsers.contains(ticket.getCustomerUserID())) {
                    if (ticket.getStateType().equals("closed")) {
                        if (ticket.getCloseDate() != null && (ticket.getCloseDate().before(endDate))) {
                            result.increaseTotalClosed();
                        }
                        if (ticket.getSolutionDiffInMin() != null && Integer.parseInt(ticket.getSolutionDiffInMin()) > 0) {
                            result.increaseTotalClosedInSla();
                        }
                        if (ticket.getCreateDate() != null && startDate.before(ticket.getCreateDate())) {
                            result.increaseTotalOpened();
                        }
                        if (ticket.getSolutionDiffInMin() == null) {
                            result.increaseTotalUnclassified();
                        }
                    } else {
                        result.increaseTotalOpened();
                    }
                }
            }
        }
        return result;
    }

}
