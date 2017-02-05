package calculate;

import input.Params;

/**
 * Created by Виктор on 07.01.2017.
 */
public class CalcResult {
    private int totalOpened;
    private int totalClosed;
    private int totalClosedInSla;
    private float slaLevel;
    private int totalUnclassified;


    public CalcResult() {
        totalOpened = 0;
        totalClosed = 0;
        totalClosedInSla = 0;
        totalUnclassified = 0;
    }

    public int getTotalOpened() {
        return totalOpened;
    }

    public void setTotalOpened(int totalOpened) {
        this.totalOpened = totalOpened;
    }

    public int getTotalClosed() {
        return totalClosed;
    }

    public void setTotalClosed(int totalClosed) {
        this.totalClosed = totalClosed;
    }

    public int getTotalClosedInSla() {
        return totalClosedInSla;
    }

    public void setTotalClosedInSla(int totalClosedInSla) {
        this.totalClosedInSla = totalClosedInSla;
    }

    public float getSlaLevel() {
        return slaLevel;
    }

    public void setSlaLevel(float slaLevel) {
        this.slaLevel = slaLevel;
    }

    public int getTotalUnclassified() {
        return totalUnclassified;
    }

    public void setTotalUnclassified(int totalUnclassified) {
        this.totalUnclassified = totalUnclassified;
    }

    public void increaseTotalClosed() {
        totalClosed++;
    }
    public void increaseTotalOpened() {
        totalOpened++;
    }
    public void increaseTotalClosedInSla() {
        totalClosedInSla++;
    }
    public void increaseTotalUnclassified() {
        totalUnclassified++;
    }

    @Override
    public String toString() {
        if (totalClosedInSla>0 && totalClosed >0) {
            slaLevel = (float)totalClosedInSla/((float)totalOpened /100) ;
        } else {
            slaLevel = 0;
        }


        return
                " Начало периода = " + Params.getProps().getProperty("START_DATE") +",\n"+
                " конец периода = " + Params.getProps().getProperty("END_DATE") + ",\n"+
                " всего пришло заявок за период = " + totalOpened +",\n"+
                " всего закрыто за период = " + totalClosed +",\n"+
                " всего закрыто согласно SLA = " + totalClosedInSla +",\n"+
                " всего не рассчитан уровень сервиса = " + totalUnclassified +",\n"+
                " уровень сервиса = " + slaLevel +" %." +
                '}';
    }
}
