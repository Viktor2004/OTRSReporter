package client;

import input.Params;

import java.util.List;

/**
 * Created by Виктор on 04.01.2017.
 */
public class SearchRequest {

    private String login;
    private String password;
    private String serverURI;
    private String startDate;
    private String endDate;
    private SearchTypes searchType;

    private String sessionID;
    private List<String> ticketIDs;

    public SearchRequest() {
        login = Params.getProps().getProperty("LOGIN");
        password = Params.getProps().getProperty("PASSWORD");
        serverURI = Params.getProps().getProperty("SOAP_URL");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public SearchTypes getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchTypes searchType) {
        this.searchType = searchType;
    }



    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public List<String> getTicketIDs() {
        return ticketIDs;
    }

    public void setTicketIDs(List<String> ticketIDs) {
        this.ticketIDs = ticketIDs;
    }
}
