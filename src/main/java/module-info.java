module com.example.assignment1_421_kell {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.assignment1_421_kell to javafx.fxml;
    exports com.example.assignment1_421_kell;
}