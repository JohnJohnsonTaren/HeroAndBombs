module org.example.heroandbombs {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.heroandbombs to javafx.fxml;
    exports org.example.heroandbombs;
}