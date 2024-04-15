module com.mycompany.architask {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jbcrypt;
    requires java.base;

    opens com.mycompany.architask to javafx.fxml;
    exports com.mycompany.architask;
}
