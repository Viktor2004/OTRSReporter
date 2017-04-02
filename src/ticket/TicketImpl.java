package ticket;

import java.util.Date;

/**
 * Created by Виктор on 06.01.2017.
 */
public class TicketImpl implements Ticket {

    private String customerUserID;
    private String owner;
    private String queue;
    private String solutionDiffInMin;
    private String stateType;
    private String created;
    private String closed;
    private Date createDate;
    private Date closeDate;

    public String getCustomerUserID() {
        return customerUserID;
    }

    public void setCustomerUserID(String customerUserID) {
        this.customerUserID = customerUserID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getSolutionDiffInMin() {
        return solutionDiffInMin;
    }

    public void setSolutionDiffInMin(String solutionDiffInMin) {
        this.solutionDiffInMin = solutionDiffInMin;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public String toString() {
        return "Ticket "+customerUserID+" "+owner+" "+queue ;
    }
}
