package io.bvzx.service.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by bvzx on 2016/9/15.
 */
public class VCodeAlert {


    public static String display(Image vcodeImg){
        Stage alertWinodw=new Stage();
        alertWinodw.initModality(Modality.APPLICATION_MODAL);
        alertWinodw.setOnCloseRequest((event -> event.consume()));

        //验证码
        Label curLabel=new Label("当前验证码:");
        ImageView vcImg=new ImageView(vcodeImg);
        vcImg.setFitHeight(20);
        vcImg.setFitWidth(60);

        //验证码输入框
        Label vcLabel=new Label("验证码:");
        TextField vcInput=new TextField();
        vcInput.setPrefWidth(100);

        //submit
        Button btn=new Button("提交");
        btn.setOnAction(event -> alertWinodw.close());

        GridPane layout=new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(8);
        layout.setHgap(8);

        layout.add(curLabel,0,0);
        layout.add(vcImg,1,0);

        layout.add(vcLabel,0,1);
        layout.add(vcInput,1,1);

        layout.add(btn,1,2);

        Scene scene=new Scene(layout);
        alertWinodw.setScene(scene);
        alertWinodw.showAndWait();
        return vcInput.getText();
    }

    public static void main(String[] args) throws FileNotFoundException {

        Image image=new Image(new FileInputStream(new File("C:\\Users\\bvzx\\Desktop\\image.jpg")));

        VCodeAlert.display(image);
    }

}
