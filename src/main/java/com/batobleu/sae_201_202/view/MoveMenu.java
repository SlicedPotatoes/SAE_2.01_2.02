package com.batobleu.sae_201_202.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MoveMenu {

    Scene scene;
    Group root;
    Group group;


    public MoveMenu() {
        this.group = new Group();

        Rectangle rectangle = new Rectangle(50,50,300,600);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        Rectangle rectangleCenter = new Rectangle(50+60*2,50+60*2,60,60);
        rectangleCenter.setFill(Color.LIGHTBLUE);
        rectangleCenter.setStroke(Color.BLACK);
        Rectangle rectangleLeft = new Rectangle(50+60,50+60*2,60,60);
        rectangleLeft.setFill(new ImagePattern(new Image("LeftArrow.png")));
        rectangleLeft.setStroke(Color.BLACK);
        Rectangle rectangleRight = new Rectangle(50+60*3,50+60*2,60,60);
        rectangleRight.setFill(new ImagePattern(new Image("RightArrow.png")));
        rectangleRight.setStroke(Color.BLACK);
        Rectangle rectangleTop = new Rectangle(50+60*2,50+60,60,60);
        rectangleTop.setFill(new ImagePattern(new Image("TopArrow.png")));
        rectangleTop.setStroke(Color.BLACK);
        Rectangle rectangleBottom = new Rectangle(50+60*2,50+60*3,60,60);
        rectangleBottom.setFill(new ImagePattern(new Image("BottomArrow.png")));
        rectangleBottom.setStroke(Color.BLACK);
        Label label = new Label("Hello World");
        group.getChildren().addAll(rectangle, rectangleCenter, rectangleLeft, rectangleRight, rectangleTop, rectangleBottom);
    }

    public void show(){
        root.getChildren().add(group);
    }
}
