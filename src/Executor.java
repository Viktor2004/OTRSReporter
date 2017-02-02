import calculate.Calc;
import client.SearchRequest;
import client.SearchTypes;
import client.WebServiceClient;
import input.Params;
import input.Soap;
import org.apache.log4j.Logger;
import ticket.TicketImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Виктор on 04.01.2017.
 */
public class Executor {
    // Инициализация логера
    private static final Logger log = Logger.getLogger(Executor.class);

    public static void main(String[] args) throws Exception {

        Params.loadProperties();
        log.info("Версия программы : " +Params.getVersion() );
        log.info("******************************");
        log.info("Начинаем получать список заявок");


        log.info("Получение списка заявок.");
        List<String> allTickets = Soap.getAllOpenAndClosedTicketsInPeriod(Params.getStartDateInDate(),Params.getEndDateInDate());
        log.info("Список заявок получен, загрузка заявок.");
        log.info("******************************");
        List<TicketImpl> allTicketsImpl = new ArrayList<>();
        int i = 0;


           String sessionID = Soap.getSessionID();
           while (i < 1) {
               //цикл на случай обрыва соединеия, чтобы посторно запросил.
               try {
                   TicketImpl tempTicket = new TicketImpl();
                   allTicketsImpl =Soap.fillTicketFromSoapMessage(WebServiceClient.getTicketByIdAndSessionID(allTickets,sessionID));

                   log.info("Всего получено " + allTickets.size() + " заявок.");
                   log.info("******************************");
                   i++;
               } catch (Exception e) {
                   log.info("Повторное соединение.");
               }
           }



        log.info ("\n***************************\n"+"Результат расчета :"+"\n"+Calc.countSla(allTicketsImpl));
        System.out.println();
        log.info ("\n***************************\n"+"Результат расчета для выбранной группы пользвоателей :" +"\n"+Calc.countSlaForUserGroup(allTicketsImpl));
        System.in.read();



    }
}
