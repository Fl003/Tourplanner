module org.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires static lombok;


    opens org.example.tourplanner to javafx.fxml;
    exports org.example.tourplanner;
    exports org.example.tourplanner.view;
    opens org.example.tourplanner.view to javafx.fxml;

    exports org.example.tourplanner.dto;
}