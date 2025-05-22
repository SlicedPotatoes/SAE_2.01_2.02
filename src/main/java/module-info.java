module com.batobleu.sae_201_202 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires jdk.compiler;
    requires javatuples;

    opens com.batobleu.sae_201_202 to javafx.fxml;

    exports com.batobleu.sae_201_202.controller;
    opens com.batobleu.sae_201_202.controller to javafx.fxml;

    exports com.batobleu.sae_201_202.view;
    opens com.batobleu.sae_201_202.view to javafx.fxml;

    exports com.batobleu.sae_201_202.view.Popup;
    opens com.batobleu.sae_201_202.view.Popup to javafx.fxml;

    exports com.batobleu.sae_201_202.view.leftMenu;
    opens com.batobleu.sae_201_202.view.leftMenu to javafx.fxml;

    exports com.batobleu.sae_201_202.model;
    opens com.batobleu.sae_201_202.model to javafx.fxml;

    exports com.batobleu.sae_201_202.model.tile;
    opens com.batobleu.sae_201_202.model.tile to javafx.fxml;

    exports com.batobleu.sae_201_202.model.entity;
    opens com.batobleu.sae_201_202.model.entity to javafx.fxml;

    exports com.batobleu.sae_201_202.model.exception;
    opens com.batobleu.sae_201_202.model.exception to javafx.fxml;

    exports com.batobleu.sae_201_202.model.exception.invalidMap;
    opens com.batobleu.sae_201_202.model.exception.invalidMap to javafx.fxml;
}