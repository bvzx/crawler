package io.bvzx.service.spider;

import io.bvzx.service.base.Event;
import io.bvzx.service.bean.QueryNHVo;
import io.bvzx.service.dto.QueryAirlineRet;
import io.bvzx.service.utils.Patterns;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/9/13.
 */
public class NHSpider extends AbstractSpider {

    public static final String INDEX_NH_URL = "http://www.shandongair.com.cn/";//主页
    public static final String QUERY_INDEX_NH_URL = "http://sc.travelsky.com";//主页
    public static final String QUERY_NH_URL = "http://sc.travelsky.com/scet/queryAv.do?lan=cn";//跳转页面
    public static final String QUERY_RESULT_NH_URL = "http://sc.travelsky.com/scet/airAvail.do";//航班结果接口
    public static final String ORDER_NH_URL = "";//4
    public static final String CANCEL_ORDER_NH_URL = "";//5

    public static final HashMap<String, Cookie> accessCookies = new HashMap<>();

    /**
     * 使用方法：先从跳转页面获取[]
     *
     * @param queryCityBean
     * @throws IOException
     */
    public Event<List<QueryAirlineRet>> query(QueryNHVo queryCityBean) throws IOException {

        HttpPost httpPost = new HttpPost(QUERY_NH_URL);
        httpPost.addHeader("Referer", INDEX_NH_URL);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        httpPost.addHeader("Origin", INDEX_NH_URL);
        httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpPost.addHeader("Cache-Control", "max-age=0");
        httpPost.addHeader("Host", "sc.travelsky.com");

        String queryString = queryCityBean.buildEncodeUrl(true);
        httpPost.setEntity(new StringEntity(queryString));

        HttpResponse httpResponse = getClient().execute(httpPost);



        StringBuffer orginHtml = readHttpResponse(httpResponse, "utf-8");

        httpPost.releaseConnection();

        Document document = Jsoup.parse(orginHtml.toString());
        Elements submitForm = document.select("form input");
        StringBuffer stringBuffer = new StringBuffer();
        submitForm.forEach((element) -> {
                    stringBuffer.append("&");
                    stringBuffer.append(element.attr("name"));
                    stringBuffer.append("=");
                    stringBuffer.append((Patterns.isChineseChar(element.attr("value")) ?
                            URLEncoder.encode(element.attr("value")) : element.attr("value")));
        });

        HttpPost post = new HttpPost(QUERY_RESULT_NH_URL);
        StringBuffer cookieArr = new StringBuffer("");
        for (Header header : httpResponse.getAllHeaders()) {
            if (header.getName().equals("Set-Cookie")) {
                cookieArr.append(header.getValue() + ";");
            }
        }
        post.addHeader("Cookie", cookieArr.toString());
        post.addHeader("Referer", QUERY_NH_URL);
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.addHeader("Origin", QUERY_INDEX_NH_URL);
        post.setEntity(new StringEntity(stringBuffer.toString().substring(1)));

        HttpResponse queryRet = getClient().execute(post);

        StringBuffer resultHtml = readHttpResponse(queryRet, "gbk");

        post.releaseConnection();

        Document retDoc = Jsoup.parse(resultHtml.toString());
        List<String> cabinLevelList=new ArrayList<>();
        retDoc.select(".cabinleveldiv td span").forEach(v -> cabinLevelList.add(v.text()));
        Map<String,List<QueryAirlineRet>> cabinInfosList=new HashMap<>();
        final Integer[] index = {new Integer(0)};
        retDoc.select(".cabintable td ").forEach(v -> {
            List<QueryAirlineRet> queryAirlineRets=new ArrayList<QueryAirlineRet>();
            Field[] fields=QueryAirlineRet.class.getDeclaredFields();
            v.select(".flight-content").forEach(s -> {
                Element ticket=s.select(".rdo-trigger").first();
                QueryAirlineRet ret=new QueryAirlineRet();
                for (Field f:fields){
                    f.setAccessible(true);
                    try {
                        if (f.getName().equals("remaintickt")){
                            String content=ticket.select("p").text();
                            ret.setRemaintickt(content.substring(2));
                            continue;
                        }
                        if (f.getName().equals("carbin")){
                            ret.setCarbin(cabinLevelList.get(index[0]));
                            continue;
                        }
                        if (f.getName().equals("departuredatetime")){
                            ret.setDeparturedatetime(ticket.attr("data-"+f.getName()).substring(11,16));
                            continue;
                        }
                        if (f.getName().equals("arrivaldatetime")){
                            ret.setArrivaldatetime(ticket.attr("data-"+f.getName()).substring(11,16));
                            continue;
                        }
                        f.set(ret,ticket.attr("data-"+f.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                queryAirlineRets.add(ret);
            });
            try{
                if (index[0]==cabinLevelList.size()){
                    index[0]=0;
                }
                if (cabinInfosList.get(cabinLevelList.get(index[0]))==null){
                    cabinInfosList.put(cabinLevelList.get(index[0]),queryAirlineRets);
                }else{
                    cabinInfosList.get(cabinLevelList.get(index[0])).addAll(queryAirlineRets);
                }
            }catch (Exception e){

            }
            index[0]++;
        });

        List<QueryAirlineRet> queryAirlineRets=new ArrayList<>();

        cabinInfosList.forEach((k,v)->queryAirlineRets.addAll(v));

        Event<List<QueryAirlineRet>> events=new Event<>();
        events.setObj(queryAirlineRets);
        if (queryAirlineRets.size()==0){
            events.setCode(-1);
            String errMsg=retDoc.select(".failure-check-in p").text();
            events.setMsg(errMsg);
            return events;
        }else{
            events.setCode(0);
            events.setMsg("成功");
        }
        return events;
    }





    public static void main(String[] args) {
        NHSpider spiderBase = new NHSpider();

        QueryNHVo queryCityBean = new QueryNHVo();
        queryCityBean.setCountrytype("0");
        queryCityBean.setTravelType("0");
        queryCityBean.setCityNameOrg("杭州");
        queryCityBean.setCityCodeOrg("HGH");
        queryCityBean.setCityNameDes("大连");
        queryCityBean.setCityCodeDes("DLC");
        queryCityBean.setTakeoffDate("2016-09-17");
        queryCityBean.setReturnDate("2016-09-17");
        queryCityBean.setCabinStage("0");
        queryCityBean.setAdultNum("1");
        queryCityBean.setChildNum("0");

        System.out.println(queryCityBean.buildEncodeUrl(true));

        try {
            spiderBase.query(queryCityBean);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(Event event) {

    }

    @Override
    public void analysis(Event event) {

    }

    @Override
    public void setEvent(Event event) {

    }
}
