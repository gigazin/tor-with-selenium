module com.github.gigazin.torwithselenium {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.seleniumhq.selenium.java;
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.support;
    requires com.google.common;
    requires dev.failsafe.core;

    exports com.github.gigazin.torwithselenium.application;
    opens com.github.gigazin.torwithselenium.application to javafx.fxml;
}