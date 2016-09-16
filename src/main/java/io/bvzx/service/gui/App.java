package io.bvzx.service.gui;

import io.bvzx.service.base.Event;
import io.bvzx.service.bean.QueryNHVo;
import io.bvzx.service.dto.QueryAirlineRet;
import io.bvzx.service.spider.NHSpider;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/9/13.
 */
public class App extends Application {


    private List<QueryAirlineRet> result;
    private TableView<QueryAirlineRet> tableView;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Text queryTip;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(event -> {
            executorService.shutdown();
            primaryStage.close();
        });

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setHgap(10);
        layout.setVgap(10);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(8);
        gridPane.setHgap(8);

        //form
        Label cityNameOrgLB = new Label("出发城市:");
        TextField cityNameOrgTF = new TextField("杭州");

        Label cityCodeOrgLB = new Label("编号:");
        TextField cityCodeOrgTF = new TextField("HGH");

        Label cityNameDesLB = new Label("目的城市:");
        TextField cityNameDesTF = new TextField("大连");

        Label cityCodeDesLB = new Label("编号:");
        TextField cityCodeDesTF = new TextField("DLC");

        Label takeoffDateLB = new Label("时间:");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(simpleDateFormat.format(new Date(System.currentTimeMillis() + 24 * 3600 * 1000)));
        DatePicker takeoffDateDP = new DatePicker(localDate);

        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        takeoffDateDP.setConverter(converter);
        takeoffDateDP.setPromptText("yyyy-MM-dd".toLowerCase());

        Button search = new Button("搜索");
        search.setOnAction((event) -> {
            NHSpider spiderBase = new NHSpider();

            QueryNHVo queryCityBean = new QueryNHVo();
            queryCityBean.setCountrytype("0");
            queryCityBean.setTravelType("0");
            queryCityBean.setCityNameOrg(cityNameOrgTF.getText());
            queryCityBean.setCityCodeOrg(cityCodeOrgTF.getText());
            queryCityBean.setCityNameDes(cityNameDesTF.getText());
            queryCityBean.setCityCodeDes(cityCodeDesTF.getText());
            queryCityBean.setTakeoffDate(takeoffDateDP.getEditor().getText());
            queryCityBean.setReturnDate(takeoffDateDP.getEditor().getText());
            queryCityBean.setCabinStage("0");
            queryCityBean.setAdultNum("1");
            queryCityBean.setChildNum("0");

            System.out.println(queryCityBean.buildEncodeUrl(true));

            executorService.execute(() -> {
                try {
                    queryTip.setFill(Color.BLUE);
                    queryTip.setText("[查询中...]");
                    long preTime = System.currentTimeMillis();
                    Event<List<QueryAirlineRet>> ret = spiderBase.query(queryCityBean);
                    long aftTime = System.currentTimeMillis();
                    if (ret.getCode() == 0) {
                        result = ret.getObj();
                        tableView.setItems(FXCollections.observableArrayList(result));
                        queryTip.setFill(Color.GREEN);
                        queryTip.setText("[查询成功 " + (aftTime - preTime) / 1000 + "s]");
                    } else {
                        queryTip.setFill(Color.RED);
                        queryTip.setText("[ " + ret.getMsg() + " " + (aftTime - preTime) / 1000 + "s]");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    queryTip.setFill(Color.RED);
                    queryTip.setText("[查询失败]");
                }
            });
        });


        queryTip = new Text("[开始搜索]");
        queryTip.setFill(Color.GREEN);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(search, queryTip);

        gridPane.add(cityNameOrgLB, 0, 0);
        gridPane.add(cityNameOrgTF, 1, 0);
        gridPane.add(cityCodeOrgLB, 2, 0);
        gridPane.add(cityCodeOrgTF, 3, 0);
        gridPane.add(cityNameDesLB, 0, 1);
        gridPane.add(cityNameDesTF, 1, 1);
        gridPane.add(cityCodeDesLB, 2, 1);
        gridPane.add(cityCodeDesTF, 3, 1);
        gridPane.add(takeoffDateLB, 0, 2);
        gridPane.add(takeoffDateDP, 1, 2);
        gridPane.add(hBox, 3, 2);

        layout.add(gridPane, 0, 0);

        tableView = new TableView();
        tableView.setPadding(new Insets(0, 0, 0, 0));

        TableColumn<QueryAirlineRet, String> carbinCol = new TableColumn<>("仓位名称");
        carbinCol.setPrefWidth(160);
        carbinCol.setCellValueFactory(new PropertyValueFactory<>("carbin"));


        TableColumn<QueryAirlineRet, String> departuredatetimeCol = new TableColumn<>("开始时间");
        departuredatetimeCol.setPrefWidth(80);
        departuredatetimeCol.setCellValueFactory(new PropertyValueFactory<>("departuredatetime"));

        TableColumn<QueryAirlineRet, String> arrivaldatetimeCol = new TableColumn<>("结束时间");
        arrivaldatetimeCol.setPrefWidth(80);
        arrivaldatetimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivaldatetime"));

        TableColumn<QueryAirlineRet, String> priceCol = new TableColumn<>("票价");
        priceCol.setPrefWidth(80);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<QueryAirlineRet, String> orgcityCol = new TableColumn<>("出发");
        orgcityCol.setPrefWidth(140);
        orgcityCol.setCellValueFactory(new PropertyValueFactory<>("orgcity"));

        TableColumn<QueryAirlineRet, String> dstcityCol = new TableColumn<>("目的");
        dstcityCol.setPrefWidth(140);
        dstcityCol.setCellValueFactory(new PropertyValueFactory<>("dstcity"));

        TableColumn<QueryAirlineRet, String> flightnumberCol = new TableColumn<>("航班号");
        flightnumberCol.setPrefWidth(60);
        flightnumberCol.setCellValueFactory(new PropertyValueFactory<>("flightnumber"));

        TableColumn<QueryAirlineRet, String> airlineCol = new TableColumn<>("航线");
        airlineCol.setPrefWidth(40);
        airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));

        TableColumn<QueryAirlineRet, String> classcodeCol = new TableColumn<>("舱号");
        classcodeCol.setPrefWidth(40);
        classcodeCol.setCellValueFactory(new PropertyValueFactory<>("classcode"));

        TableColumn<QueryAirlineRet, String> taxCol = new TableColumn<>("税");
        taxCol.setPrefWidth(40);
        taxCol.setCellValueFactory(new PropertyValueFactory<>("tax"));

        TableColumn<QueryAirlineRet, String> remainticktCol = new TableColumn<>("余票");
        remainticktCol.setPrefWidth(60);
        remainticktCol.setCellValueFactory(new PropertyValueFactory<>("remaintickt"));

        result = new ArrayList<>();
        tableView.setItems(FXCollections.observableArrayList(result));
        tableView.getColumns().addAll(carbinCol, departuredatetimeCol, arrivaldatetimeCol,
                priceCol, orgcityCol, dstcityCol, flightnumberCol, airlineCol, classcodeCol, taxCol, remainticktCol);

        layout.add(tableView, 0, 1);

        GridPane options = new GridPane();
        options.setHgap(10);
        options.setVgap(10);

        layout.add(options, 1, 0, 1, 1);

        primaryStage.setTitle("票务占位 - v1.1");
        primaryStage.setScene(new Scene(layout));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
