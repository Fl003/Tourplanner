module org.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.tourplanner to javafx.fxml;
    exports org.example.tourplanner;
    exports org.example.tourplanner.view;
    opens org.example.tourplanner.view to javafx.fxml;
}