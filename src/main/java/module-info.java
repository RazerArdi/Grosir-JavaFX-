module pro.pemprogramanlanjuttb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens pro.pemprogramanlanjuttb to javafx.fxml;
    exports PemprogramanLanjutTB;
    opens PemprogramanLanjutTB to javafx.fxml;
}