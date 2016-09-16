package io.bvzx.service.dto;

/**
 * Created by Administrator on 2016/9/14.
 */
public class QueryAirlineRet {

    private String departuredatetime;

    private String arrivaldatetime;

    private String price;

    private String orgcity;

    private String dstcity;

    private String flightnumber;

    private String airline;

    private String classcode;

    private String tax;

    private String remaintickt;

    private String carbin;

    public String getCarbin() {
        return carbin;
    }

    public void setCarbin(String carbin) {
        this.carbin = carbin;
    }

    public String getDeparturedatetime() {
        return departuredatetime;
    }

    public void setDeparturedatetime(String departuredatetime) {
        this.departuredatetime = departuredatetime;
    }

    public String getArrivaldatetime() {
        return arrivaldatetime;
    }

    public void setArrivaldatetime(String arrivaldatetime) {
        this.arrivaldatetime = arrivaldatetime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrgcity() {
        return orgcity;
    }

    public void setOrgcity(String orgcity) {
        this.orgcity = orgcity;
    }

    public String getDstcity() {
        return dstcity;
    }

    public void setDstcity(String dstcity) {
        this.dstcity = dstcity;
    }

    public String getFlightnumber() {
        return flightnumber;
    }

    public void setFlightnumber(String flightnumber) {
        this.flightnumber = flightnumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getRemaintickt() {
        return remaintickt;
    }

    public void setRemaintickt(String remaintickt) {
        this.remaintickt = remaintickt;
    }
}
