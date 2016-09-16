package io.bvzx.service.bean;

/**
 * Created by Administrator on 2016/9/13.
 * lan=cn&countrytype=0&travelType=0&cityNameOrg=杭州&cityCodeOrg=HGH&cityNameDes=大连
 * &cityCodeDes=DLC&takeoffDate=2016-09-14&returnDate=2016-09-14&cabinStage=0&adultNum=1&childNum=0
 */
public class QueryNHVo extends CommonVo {

    private String countrytype;   //0
    private String travelType;   //0
    private String cityNameOrg;   //杭州
    private String cityCodeOrg;   //HGH
    private String cityNameDes;   //大连
    private String cityCodeDes;   //DLC
    private String takeoffDate;   //2016-09-14
    private String returnDate;   //2016-09-14
    private String cabinStage;   //0
    private String adultNum;   //1
    private String childNum;   //0

    public String getCountrytype() {
        return countrytype;
    }

    public void setCountrytype(String countrytype) {
        this.countrytype = countrytype;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getCityNameOrg() {
        return cityNameOrg;
    }

    public void setCityNameOrg(String cityNameOrg) {
        this.cityNameOrg = cityNameOrg;
    }

    public String getCityCodeOrg() {
        return cityCodeOrg;
    }

    public void setCityCodeOrg(String cityCodeOrg) {
        this.cityCodeOrg = cityCodeOrg;
    }

    public String getCityNameDes() {
        return cityNameDes;
    }

    public void setCityNameDes(String cityNameDes) {
        this.cityNameDes = cityNameDes;
    }

    public String getCityCodeDes() {
        return cityCodeDes;
    }

    public void setCityCodeDes(String cityCodeDes) {
        this.cityCodeDes = cityCodeDes;
    }

    public String getTakeoffDate() {
        return takeoffDate;
    }

    public void setTakeoffDate(String takeoffDate) {
        this.takeoffDate = takeoffDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getCabinStage() {
        return cabinStage;
    }

    public void setCabinStage(String cabinStage) {
        this.cabinStage = cabinStage;
    }

    public String getAdultNum() {
        return adultNum;
    }

    public void setAdultNum(String adultNum) {
        this.adultNum = adultNum;
    }

    public String getChildNum() {
        return childNum;
    }

    public void setChildNum(String childNum) {
        this.childNum = childNum;
    }

    public Class getClazz() {
        return QueryNHVo.class;
    }


    public static void main(String[] args){
        QueryNHVo queryCityBean=new QueryNHVo();

        queryCityBean.setCountrytype("0");
        queryCityBean.setTravelType("0");
        queryCityBean.setCityNameOrg("杭州");
        queryCityBean.setCityCodeOrg("HGH");
        queryCityBean.setCityNameDes("大连");
        queryCityBean.setCityCodeDes("DLC");
        queryCityBean.setTakeoffDate("2016-09-14");
        queryCityBean.setReturnDate("2016-09-14");
        queryCityBean.setCabinStage("cn");
        queryCityBean.setAdultNum("1");
        queryCityBean.setChildNum("0");

        String queryString=queryCityBean.buildEncodeUrl(true);

        System.out.println(queryString);
    }
}
