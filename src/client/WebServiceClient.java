package client;

import javax.xml.soap.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WebServiceClient {

    public static SOAPMessage getOpenedTickets(Date date) throws Exception {
        //create search request
        SearchRequest request = new SearchRequest();
        SimpleDateFormat formatedDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateStirng = formatedDate.format(date);
        request.setStartDate(dateStirng+" 00:00:00");
        request.setEndDate(dateStirng+" 23:59:59");
        request.setSearchType(SearchTypes.SEARCH_OPEN_TICKETS_REQUEST);
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        // Send SOAP Message to SOAP Server
        String url = request.getServerURI();
        SOAPMessage soapResponse = soapConnection.call(createSOAPSearchRequest(request), url);

// print SOAP Response
     //   System.out.print("Response SOAP Message:");
     //   soapResponse.writeTo(System.out);

        return soapResponse;

    }

    public static SOAPMessage getTicketByID (List<String> ticketIDs) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        //create search request
        SearchRequest request = new SearchRequest();
        request.setTicketIDs(ticketIDs);
        request.setSearchType(SearchTypes.GET_TICKET_REQUEST);
        // Send SOAP Message to SOAP Server
        String url = request.getServerURI();
        SOAPMessage soapResponse = soapConnection.call(createSOAPSearchRequest(request), url);
        return soapResponse;

    }
    public static SOAPMessage getTicketByIdAndSessionID (List<String> ticketIDs, String SessionID) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        //create search request
        SearchRequest request = new SearchRequest();
        request.setTicketIDs(ticketIDs);
        request.setSearchType(SearchTypes.GET_TICKET_REQUEST);
        request.setSessionID(SessionID);
        // Send SOAP Message to SOAP Server
        String url = request.getServerURI();
        SOAPMessage soapResponse = soapConnection.call(createSOAPSearchRequestWithSessionID(request), url);
      //  soapResponse.writeTo(System.out);

        return soapResponse;

    }

    public static SOAPMessage getClosedTickets(Date date) throws Exception {
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        //create search request
        SearchRequest request = new SearchRequest();
        SimpleDateFormat formatedDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateStirng = formatedDate.format(date);
        request.setStartDate(dateStirng+" 00:00:00");
        request.setEndDate(dateStirng+" 23:59:59");
        request.setSearchType(SearchTypes.SEARCH_CLOSED_TICKETS_REQUEST);

        // Send SOAP Message to SOAP Server
        String url = request.getServerURI();
        SOAPMessage soapResponse = soapConnection.call(createSOAPSearchRequest(request), url);
// print SOAP Response
        //   System.out.print("Response SOAP Message:");
        //   soapResponse.writeTo(System.out);

        return soapResponse;

    }

    /**
     * Создание запроса на поиск.
     * @param request
     * @return Либо списко тикетов, либо сами тикеты
     * @throws Exception
     */
    private static SOAPMessage createSOAPSearchRequest(SearchRequest request ) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tic", request.getServerURI());

        /*
        Constructed SOAP Request Message:
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
                <example:VerifyEmail>
                    <example:email>mutantninja@gmail.com</example:email>
                    <example:LicenseKey>123</example:LicenseKey>
                </example:VerifyEmail>
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem;
        if (!SearchTypes.GET_TICKET_REQUEST.equals(request.getSearchType())) {
             soapBodyElem = soapBody.addChildElement("TicketSearch", "tic");
        } else {
             soapBodyElem = soapBody.addChildElement("TicketGet", "tic");
        }
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("UserLogin", "tic");
        soapBodyElem1.addTextNode(request.getLogin());
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("Password", "tic");
        soapBodyElem2.addTextNode(request.getPassword());
        SOAPElement soapBodyElem3;
        SOAPElement soapBodyElem4;

        switch (request.getSearchType()) {
            case SEARCH_OPEN_TICKETS_REQUEST:

                soapBodyElem3 = soapBodyElem.addChildElement("TicketCreateTimeNewerDate", "tic");
                soapBodyElem3.addTextNode(request.getStartDate());

                soapBodyElem4 = soapBodyElem.addChildElement("TicketCreateTimeOlderDate", "tic");
                soapBodyElem4.addTextNode(request.getEndDate());

                break;
            case SEARCH_CLOSED_TICKETS_REQUEST:

                soapBodyElem3 = soapBodyElem.addChildElement("TicketCloseTimeNewerDate", "tic");
                soapBodyElem3.addTextNode(request.getStartDate());

                soapBodyElem4 = soapBodyElem.addChildElement("TicketCloseTimeOlderDate", "tic");
                soapBodyElem4.addTextNode(request.getEndDate());
                break;
            case GET_TICKET_REQUEST :
                soapBodyElem3 = soapBodyElem.addChildElement("TicketID", "tic");
                for (String ticket : request.getTicketIDs() ) {
                    soapBodyElem3.addTextNode(ticket);
                }
                soapBodyElem4 = soapBodyElem.addChildElement("Extended", "tic");
                soapBodyElem4.addTextNode("true");
                break;

        }


        MimeHeaders headers = soapMessage.getMimeHeaders();
        if (!SearchTypes.GET_TICKET_REQUEST.equals(request.getSearchType())) {
            headers.addHeader("SOAPAction", request.getServerURI()  + "TicketSearch");
        } else {
            headers.addHeader("SOAPAction", request.getServerURI()  + "TicketGet");
        }
        soapMessage.saveChanges();
        return soapMessage;
    }

    public static SOAPMessage getSessionUID () throws SOAPException {
        SearchRequest request = new SearchRequest();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tic", request.getServerURI());

        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem;
        soapBodyElem = soapBody.addChildElement("SessionCreate", "tic");

        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("UserLogin", "tic");
        soapBodyElem1.addTextNode(request.getLogin());
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("Password", "tic");
        soapBodyElem2.addTextNode(request.getPassword());
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", request.getServerURI()  + "SessionCreate");
        soapMessage.saveChanges();

        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = request.getServerURI();
        SOAPMessage soapResponse = soapConnection.call(soapMessage, url);

        return soapResponse;


    }

    public static SOAPMessage createSOAPSearchRequestWithSessionID(SearchRequest request) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tic", request.getServerURI());

        /*
        Constructed SOAP Request Message:
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
                <example:VerifyEmail>
                    <example:email>mutantninja@gmail.com</example:email>
                    <example:LicenseKey>123</example:LicenseKey>
                </example:VerifyEmail>
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
         */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem;
        if (!SearchTypes.GET_TICKET_REQUEST.equals(request.getSearchType())) {
            soapBodyElem = soapBody.addChildElement("TicketSearch", "tic");
        } else {
            soapBodyElem = soapBody.addChildElement("TicketGet", "tic");
        }
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("SessionID", "tic");
        soapBodyElem1.addTextNode(request.getSessionID());
        SOAPElement soapBodyElem2;
        SOAPElement soapBodyElem3;

        switch (request.getSearchType()) {
            case SEARCH_OPEN_TICKETS_REQUEST:

                soapBodyElem2 = soapBodyElem.addChildElement("TicketCreateTimeNewerDate", "tic");
                soapBodyElem2.addTextNode(request.getStartDate());

                soapBodyElem3 = soapBodyElem.addChildElement("TicketCreateTimeOlderDate", "tic");
                soapBodyElem3.addTextNode(request.getEndDate());

                break;
            case SEARCH_CLOSED_TICKETS_REQUEST:

                soapBodyElem2 = soapBodyElem.addChildElement("TicketCloseTimeNewerDate", "tic");
                soapBodyElem2.addTextNode(request.getStartDate());

                soapBodyElem3 = soapBodyElem.addChildElement("TicketCloseTimeOlderDate", "tic");
                soapBodyElem3.addTextNode(request.getEndDate());
                break;
            case GET_TICKET_REQUEST :


                for (String ticket :request.getTicketIDs()) {
                    soapBodyElem2 = soapBodyElem.addChildElement("TicketID", "tic");
                    soapBodyElem2.addTextNode(ticket);
                }
                soapBodyElem3 = soapBodyElem.addChildElement("Extended", "tic");
                soapBodyElem3.addTextNode("true");
                break;

        }


        MimeHeaders headers = soapMessage.getMimeHeaders();
        if (!SearchTypes.GET_TICKET_REQUEST.equals(request.getSearchType())) {
            headers.addHeader("SOAPAction", request.getServerURI()  + "TicketSearch");
        } else {
            headers.addHeader("SOAPAction", request.getServerURI()  + "TicketGet");
        }
        soapMessage.saveChanges();
        return soapMessage;


    }




}