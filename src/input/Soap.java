package input;

import client.WebServiceClient;
import org.apache.log4j.Logger;
import ticket.TicketImpl;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Виктор on 04.01.2017.
 */
public class Soap {
    private static final Logger log = Logger.getLogger(Soap.class);

    /**
     * Метод для получения одного параметра из SOAP Message. Возвращает первый найденный результат
     * @param message - само сообщение
     * @param parametr - тег по которому идет писк.
     * @return
     * @throws SOAPException
     */
    public static String getOneParametrFromSoapMessage(SOAPMessage message, String parametr) throws SOAPException {
        String result;
        String openSeparator = "<"+parametr+">";
        String closeSeparator = "</"+parametr+">";
        String messageString = convertSoapMessageToString(message);
        String[] firstSplit = messageString.split(openSeparator);
        if (firstSplit.length>2) throw new IllegalArgumentException();
       if (firstSplit.length == 1) {
           result = null;

       } else {
           String[] secondSplit = firstSplit[1].split(closeSeparator);
           result = secondSplit[0];
       }
        return result;
    }

    /**
     * Метод для получения всех параметра из SOAP Message. Возвращает все результаты списком.
     * @param message
     * @param parametr
     * @return
     * @throws SOAPException
     */
    public static List<String> getAllParametrsFromSoapMessage(SOAPMessage message, String parametr) throws SOAPException {
        List<String> result = new ArrayList<>();
        String openSeparator = "<"+parametr+">";
        String closeSeparator = "</"+parametr+">";
        String messageString = convertSoapMessageToString(message);
        String[] firstSplit = messageString.split(openSeparator);
        for (int i =1; i < firstSplit.length;i++) {
            String[] secondSplit = firstSplit[i].split(closeSeparator);
            result.add(secondSplit[0]);
        }

        return result;
    }

    /**
     * Возвращает списком все тикеты из сообщений. Тикеты уже заполнены. В дальнейшем будет заменено маршаллером.
     * @param message
     * @return
     * @throws SOAPException
     * @throws ParseException
     */
    public static List<TicketImpl> fillTicketFromSoapMessage (SOAPMessage message) throws SOAPException, ParseException {
       List<String> allTickets = getAllParametrsFromSoapMessage(message,"Ticket");
        List<TicketImpl> result = new ArrayList<TicketImpl>();

       for (String ticketInString : allTickets) {
           TicketImpl ticket = new TicketImpl();
           ticket.setClosed(getOneParametrFromString(ticketInString, "Closed"));
           ticket.setCreated(getOneParametrFromString(ticketInString, "Created"));
           ticket.setCustomerUserID(getOneParametrFromString(ticketInString, "CustomerUserID"));
           ticket.setOwner(getOneParametrFromString(ticketInString, "Owner"));
           ticket.setQueue(getOneParametrFromString(ticketInString, "Queue"));
           ticket.setSolutionDiffInMin(getOneParametrFromString(ticketInString, "SolutionDiffInMin"));
           ticket.setStateType(getOneParametrFromString(ticketInString, "StateType"));
           //сохраняем даты в формате Date
           DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String dateString = getOneParametrFromString(ticketInString, "Created");
           if (dateString != null) {
               Date createDate = format.parse(dateString);
               ticket.setCreateDate(createDate);
           }
           dateString = getOneParametrFromString(ticketInString, "Closed");
           if (dateString != null) {
               Date closeDate = format.parse(dateString);
               ticket.setCloseDate(closeDate);
           }
           result.add(ticket);
       }

        return result;
    }

    /**
     * Конвертит SOAP Message в строку.
     * @param message
     * @return
     * @throws SOAPException
     */
    private static String convertSoapMessageToString (SOAPMessage message) throws SOAPException {
        String result = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            message.writeTo(out);
            result = new String(out.toByteArray());

        } catch (FileNotFoundException e) {
           log.error(e);
        } catch (IOException e) {
            log.error(e);
        }

        return result;
    }

    /**
     * Метод для получения номеров всех открытых и закрытых тикетов за заданный период.
     * Даты учитываются с точностью до секунды.
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<String> getAllOpenAndClosedTicketsInPeriod (Date startDate, Date endDate) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        Date tempDate = startDate;
        while (tempDate.before(endDate)) {
            try {

                List<String> tempTickets = Soap.getAllParametrsFromSoapMessage(WebServiceClient.getOpenedTickets(tempDate), "TicketID");

                tempTickets.addAll(Soap.getAllParametrsFromSoapMessage(WebServiceClient.getClosedTickets(tempDate), "TicketID"));
                for (String ticketID : tempTickets) {
                    if (!result.contains(ticketID))
                        result.add(ticketID);
                }

                //увеличиваем дату на 1 день
                Calendar instance = Calendar.getInstance();
                instance.setTime(tempDate); //устанавливаем дату, с которой будет производить операции
                instance.add(Calendar.DAY_OF_MONTH, 1);// прибавляем 1 день к установленной дате
                tempDate = instance.getTime(); // получаем измененную дату
            } catch (Exception e) {
                log.error("Ошибка соединения. Повторное подключение.",e);
              //  System.out.println("Ошибка соединения. Повторное подключение.");
            }

        }

        return result;

    }

    /**
     * Метод для получения ID сессии (при этом создается новая сесстия)
     * @return
     * @throws SOAPException
     */
    public static String getSessionID() throws SOAPException {
       String result = Soap.getOneParametrFromSoapMessage(WebServiceClient.getSessionUID(),"SessionID");
        return result;
    }

    /**
     * По сути маршаллер для одного тега. Возвращает первый найденный результат. Потом сделаю красивее.
     * @param source
     * @param parametr
     * @return
     */
    private static String getOneParametrFromString (String source, String parametr) {
        String result;
        String openSeparator = "<"+parametr+">";
        String closeSeparator = "</"+parametr+">";

        String[] firstSplit = source.split(openSeparator);
        if (firstSplit.length>2) throw new IllegalArgumentException();
        if (firstSplit.length == 1) {
            result = null;

        } else {
            String[] secondSplit = firstSplit[1].split(closeSeparator);
            result = secondSplit[0];
        }
        return result;
    }

    /**
     * Устанавливает сессию и помещает ее ID в параметры.
     */
    public static void makeSession () throws SOAPException {
        Params.setSessionID(getSessionID());

    }
}
