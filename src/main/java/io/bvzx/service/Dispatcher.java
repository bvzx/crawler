package io.bvzx.service;

import io.bvzx.service.spider.NHSpider;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.stage.Stage;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Dispatcher extends Application {


    public  static void main(String[] args){
        preInitGUI(args);
    }

    public static void preInitGUI(String[] args){
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        final Button button = new Button("查询");
        button.setOnAction(event -> NHSpider.main(null));
        Parent root = BorderPaneBuilder.create().center(button).build();
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("airline spider cawler");
        primaryStage.show();
    }
}
