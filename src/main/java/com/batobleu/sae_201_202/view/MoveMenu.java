package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class MoveMenu {

    BorderPane root;
    Group group;
    int numberMoveLeft = 0;
    int numberRound = 0;




    public MoveMenu(MainController mc) {
        this.root = mc.getRoot();
        this.group = new Group();
        //Container
        Rectangle rectangle = new Rectangle(50,50,300,600);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        //Turn display
        Rectangle display = new Rectangle(50+(60*3/2),50+30,120,120);
        display.setFill(new ImagePattern(new Image("image/Wolf.png")));

        display.setStroke(Color.BLACK);
        Label moveLeft = new Label("Move Left : "+ numberMoveLeft);
        moveLeft.setTranslateX(170);
        moveLeft.setTranslateY(220);

        Label round = new Label("Round : "+ numberRound);
        round.setTranslateX(180);
        round.setTranslateY(500);

        //Directional arrow
        Rectangle rectangleCenter = new Rectangle(200+60*2,50+60*2,60,60);
        rectangleCenter.setFill(Color.LIGHTBLUE);
        rectangleCenter.setStroke(Color.BLACK);

        Rectangle rectangleLeft = new Rectangle(50+60,50+60*2,60,60);
        rectangleLeft.setFill(new ImagePattern(new Image("Image/LeftArrow.png")));
        rectangleLeft.setStroke(Color.BLACK);

        Rectangle rectangleRight = new Rectangle(50+60*3,50+60*2,60,60);
        rectangleRight.setFill(new ImagePattern(new Image("Image/RightArrow.png")));
        rectangleRight.setStroke(Color.BLACK);

        Rectangle rectangleTop = new Rectangle(50+60*2,50+60,60,60);
        rectangleTop.setFill(new ImagePattern(new Image("Image/TopArrow.png")));
        rectangleTop.setStroke(Color.BLACK);

        Rectangle rectangleBottom = new Rectangle(50+60*2,50+60*3,60,60);
        rectangleBottom.setFill(new ImagePattern(new Image("Image/BottomArrow.png")));
        rectangleBottom.setStroke(Color.BLACK);

        //add to group
        group.getChildren().addAll(rectangle, rectangleCenter, rectangleLeft, rectangleRight, rectangleTop, rectangleBottom);
    }


    public void show(){
        this.group.setVisible(true);
    }

    public void hide(){
        this.group.setVisible(false);
    }

    public void update(){

    }
}
