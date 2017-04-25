import calculate.Calc;
import client.SearchRequest;
import client.SearchTypes;
import client.WebServiceClient;
import input.Cmd;
import input.Params;
import input.Soap;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import output.htmlTables;
import org.apache.log4j.Logger;
import ticket.TicketImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Виктор on 04.01.2017.
 */
public class Executor {
    // Инициализация логера
    private static final Logger log = Logger.getLogger(Executor.class);

    public static void main(String[] args) throws Exception {
        if (args!=null && args.length>0) {
            log.info("Загружаем параметры из внешнего файла в папке "+ args[0]);
            Params.loadProperties(args[0]);
        } else {
            Params.loadProperties(null);
        }
       //исполняем скрипт, через который получим список пользователей
        if (Params.getBatFilePath() != null) {
            log.info("Запускаем bat файл "+Params.getBatFilePath());
            Cmd.executeCommand(Params.getBatFilePath());
        }

        // Cmd.executeCommand("D:\\Sert\\1.bat");
        if (Params.isServerMode()) {
            log.info("Программа запущена в серверном режиме.");
            log.info("Версия программы : " + Params.getVersion());
            log.info("******************************");
            while (true) {
                if (Params.getBatFilePath() != null) {
                    log.info("Запускаем bat файл "+Params.getBatFilePath());
                    Cmd.executeCommand(Params.getBatFilePath());
                }
                log.info("Строим график.");
                htmlTables.buildWeekReport(new Date());
                log.info("График построили.");
                Thread.sleep(Params.getTimeToRefreshChartInMins()*1000*60);
                Params.setSessionID(null);
            }

        } else {
            log.info("Версия программы : " + Params.getVersion());
            log.info("******************************");
            log.info("Начинаем получать список заявок");


            log.info("Получение списка заявок.");
            List<String> allTickets = Soap.getAllOpenAndClosedTicketsInPeriod(Params.getStartDateInDate(), Params.getEndDateInDate());
            log.info("Список заявок получен, загрузка заявок.");
            log.info("******************************");
            List<TicketImpl> allTicketsImpl = new ArrayList<>();
            int i = 0;


            Soap.makeSession();
            while (i < 1) {
                //цикл на случай обрыва соединеия, чтобы посторно запросил.
                try {
                    TicketImpl tempTicket = new TicketImpl();
                    allTicketsImpl = Soap.fillTicketFromSoapMessage(WebServiceClient.getTicketByIdAndSessionID(allTickets, Params.getSessionID()));

                    log.info("Всего получено " + allTickets.size() + " заявок.");
                    log.info("******************************");
                    i++;
                } catch (Exception e) {
                    log.info("Повторное соединение.");
                }
            }


            log.info("\n***************************\n" + "Результат расчета :" + "\n" + Calc.countSla(allTicketsImpl, Params.getStartDateInDate(), Params.getEndDateInDate()));
            System.out.println();
            log.info("\n***************************\n" + "Результат расчета для выбранной группы пользвоателей :" + "\n" + Calc.countSlaForUserGroup(allTicketsImpl,Params.getUserListFromFile() , Params.getStartDateInDate(),Params.getEndDateInDate(),true));
            System.in.read();

            if (Params.isServerMode()) {
                htmlTables.buildWeekReport(new Date());
            }

        }
    }
}
