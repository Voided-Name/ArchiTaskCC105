module com.mycompany.architask {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jbcrypt;
    requires java.base;
    requires webcam.capture;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    
    opens com.mycompany.architask to javafx.fxml;

    exports com.mycompany.architask;
}
