module com.github.gigazin.youbot {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.seleniumhq.selenium.java;
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.support;
    requires com.google.common;
    requires dev.failsafe.core;

    exports com.github.gigazin.youbot.application;
    opens com.github.gigazin.youbot.application to javafx.fxml;
}