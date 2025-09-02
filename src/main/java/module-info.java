module org.example.strategysimulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.strategysimulation to javafx.fxml;
    exports com.example.strategysimulation;
}