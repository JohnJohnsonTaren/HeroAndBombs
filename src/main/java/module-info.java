module org.example.heroandbombs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens org.example.heroandbombs to javafx.fxml;
    exports org.example.heroandbombs;
}