module com.batobleu.sae_201_202 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.batobleu.sae_201_202 to javafx.fxml;
    exports com.batobleu.sae_201_202.controller;
    opens com.batobleu.sae_201_202.controller to javafx.fxml;
    exports com.batobleu.sae_201_202.view;
    opens com.batobleu.sae_201_202.view to javafx.fxml;
}