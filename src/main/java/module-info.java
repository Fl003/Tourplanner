module org.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires com.fasterxml.jackson.core;


    opens org.example.tourplanner to javafx.fxml;
    exports org.example.tourplanner;
    exports org.example.tourplanner.view;
    opens org.example.tourplanner.view to javafx.fxml;

    exports org.example.tourplanner.dto;
}