package com.github.gigazin.torwithselenium.application;

import java.security.SecureRandom;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * The main stage controller class.
 * Functions and methods here are used to control the interface and automate Tor with Selenium.
 *
 * @author gigazin
 * @version 1.0.1
 * @since 1.0.0-bt1
 */
public class AutomaTorController {

    @FXML
    private ImageView gigaLogo;
    @FXML
    private Label automatorText;
    @FXML
    private Label automatorDescriptionText;
    @FXML
    private Label gigaCopyrightText;
    @FXML
    private Pane leftPane;
    @FXML
    private Label instructionsTextFile;
    @FXML
    private Label instructionsTextFolder;
    @FXML
    private Label instructionsTextDriver;
    @FXML
    private TextField URLField;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button selectFolderButton;
    @FXML
    private Button selectDriverButton;
    @FXML
    private Button runButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label pathInjectedFile;
    @FXML
    private Label pathInjectedFolder;
    @FXML
    private Label pathInjectedDriver;
    @FXML
    private Label statusText;
    @FXML
    private Circle statusCircle;
    @FXML
    private Label automatorBuildText;

    private boolean action;
    private String torPath;
    private String profilePath;
    private String driverPath;
    private String URL;
    private FirefoxProfile profile;
    private WebDriver driver;
    private int timer;

    /**
     * Controls the "Close" button to close the application.
     *
     * @author gigazin
     * @since 1.0.0
     * @param e The event of the button click.
     */
    @FXML
    protected void onCloseButtonClick(ActionEvent e) {
        Stage stage;
        stage = (Stage)((Button)e.getSource()).getScene().getWindow();
        if (getActionStatus()) {
            driver.quit();
            setAction(false);
        }
        stage.close();
    }

    /**
     * Controls the "Minimize" button to minimize the application.
     *
     * @author gigazin
     * @since 1.0.0
     * @param e The event of the button click.
     */
    @FXML
    protected void onMinimizeButtonClick(ActionEvent e) {
        Stage stage;
        stage = (Stage)((Button)e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Controls the "Select File" button to open a file chooser dialog and save the selected file path.
     * File selection is required to run the marionette.
     *
     * @author gigazin
     * @since 1.0.0-bt1
     */
    @FXML
    protected void onSelectFileButtonClick() {

        /*
        * Setting Look & Feel of the file search window to match the
        * current system being used instead of standard Java L&F.
        */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to set JFileChooser look and feel: " + e.getMessage()).show();
        }

        // Creating a fileChooser with drag & drop enabled and All Files filter disabled.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDragEnabled(true);

        /*
        * Checks if the file path is not null and sets the file chooser current directory
        * to the file path for QoL sake.
        */
        if (getTorPath() != null) {
            fileChooser.setCurrentDirectory(new File(getTorPath()));
        }

        // Setting the file chooser filter to allow only .exe files selection.
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                else return f.getName().toLowerCase().endsWith(".exe");
            }

            @Override
            public String getDescription() {
                return "Executable Files (*.exe)";
            }
        });

        // Open the file dialog and save the response.
        int fileOpenResponse = fileChooser.showOpenDialog(null);

        /*
        * Checks if the return of showOpenDialog was 0 (APPROVE_OPTION), saves the
        * file path and sets the pathInjectedFile text to show the file path.
        *
        * OBS: You don't need setTorPath and getTorPath, they're mainly used to
        * change the interface text only. Also note that I'm using JFileChooser to select
        * the files and save their path. Instead, you can just type their path directly.
        *
        * You can set the path as follows:
        *
        * Directly:
        * binary = "path\to\your\Tor Browser\Browser\firefox.exe";
        *
        * Or,
        *
        * Using JFileChooser:
        * String binary = fileChooser.getSelectedFile().getAbsolutePath();
        *
        * Directly:
        * String binary = "path\to\your\Tor Browser\Browser\firefox.exe";
        *
        * If the return was not APPROVE_OPTION, sets the pathInjectedFile text to
        * "File not imported!".
        */
        if (fileOpenResponse == JFileChooser.APPROVE_OPTION) {
            setTorPath(fileChooser.getSelectedFile().getAbsolutePath()); // The requested path to firefox.exe file.
            pathInjectedFile.setText("File path: " + getTorPath());
        } else {
            pathInjectedFile.setText("File not imported!");
        }

    }

    /**
     * Controls the "Select Folder" button to open a folder chooser dialog and save the selected folder path.
     * Folder selection is required to run the marionette.
     *
     * @author gigazin
     * @since 1.0.0-bt1
     */
    @FXML
    protected void onSelectFolderButtonClick() {

        /*
         * Setting Look & Feel of the file search window to match the
         * current system being used instead of standard Java L&F.
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to set JFileChooser look and feel: " + e.getMessage()).show();
        }

        /*
        * Creating a fileChooser with drag & drop enabled and file selection
        * mode set to allow directories only.
        */
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDragEnabled(true);

        /*
         * Checks if the Tor file path is not null and sets the file chooser current directory
         * to the file path for QoL sake.
         */
        if (getTorPath() != null) {
            fileChooser.setCurrentDirectory(new File(getTorPath()));
        }

        // Open the file dialog and save the response.
        int fileOpenResponse = fileChooser.showOpenDialog(null);

        /*
         * Checks if the return of showOpenDialog was 0 (APPROVE_OPTION), saves the
         * folder path and sets the pathInjectedFolder text to show the folder path.
         * Additionaly sets the file chooser current directory to the folder path.
         *
         * OBS: You don't need setProfilePath and getProfilePath, they're mainly used to
         * change the interface text only. I'm using getProfilePath on setProfile for less
         * typing sake. Also note that I'm using JFileChooser to select the files and save
         * their path. Instead, you can just type their path directly.
         *
         * You can set the path as follows:
         *
         * Using JFileChooser:
         * setProfile(new File(fileChooser.getSelectedFile().getAbsolutePath()));
         *
         * Directly:
         * setProfile(new File("path\to\your\Tor Browser\Browser\TorBrowser\Data\Browser\profile.default"));
         *
         * Or,
         *
         * Using JFileChooser:
         * FirefoxProfile profile = new FirefoxProfile(new File(fileChooser.getSelectedFile().getAbsolutePath()));
         *
         * Directly:
         * FirefoxProfile profile = new FirefoxProfile(new File("path\to\your\Tor Browser\Browser\TorBrowser\Data\Browser\profile.default"));
         *
         * If the return was not APPROVE_OPTION, sets the pathInjectedFolder text to
         * "Folder not imported!".
         */
        if (fileOpenResponse == JFileChooser.APPROVE_OPTION) {
            setProfilePath(fileChooser.getSelectedFile().getAbsolutePath());
            fileChooser.setCurrentDirectory(new File(getProfilePath()));
            setProfile(new File(getProfilePath())); // The requested path to profile.default folder.
            pathInjectedFolder.setText("Folder path: " + getProfilePath());
        } else {
            pathInjectedFolder.setText("Folder not imported!");
        }

    }

    /**
     * Controls the "Select Driver" button to open a file chooser dialog and save the selected file path.
     *
     * @author gigazin
     * @since 1.0.0-bt3
     */
    @FXML
    protected void onSelectDriverButtonClick() {

        /*
         * Setting Look & Feel of the file search window to match the
         * current system being used instead of standard Java L&F.
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to set JFileChooser look and feel: " + e.getMessage()).show();
        }

        // Creating a fileChooser with drag & drop enabled and All Files filter disabled.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDragEnabled(true);

        // Setting the file chooser filter to allow only .exe files selection.
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                else return f.getName().toLowerCase().endsWith(".exe");
            }

            @Override
            public String getDescription() {
                return "Executable Files (*.exe)";
            }
        });

        // Open the file dialog and save the response.
        int fileOpenResponse = fileChooser.showOpenDialog(null);

        /*
        * Checks if the return of showOpenDialog was 0 (APPROVE_OPTION), saves the
        * file path and sets the pathInjectedDriver text to show the driver path.
        *
        * If the return was not APPROVE_OPTION, sets the pathInjectedDriver text to
        * "Driver not imported!".
        */
        if (fileOpenResponse == JFileChooser.APPROVE_OPTION) {
            setDriverPath(fileChooser.getSelectedFile().getAbsolutePath()); // The requested path to driver.
            pathInjectedDriver.setText("Driver path: " + getDriverPath());
        } else {
            pathInjectedDriver.setText("Driver not imported!");
        }

    }

    /**
     * Controls the "run" button to show errors regarding the requirements to run the marionette.
     * Shows a confirmation dialog when all the requirements are satisfied and runs the marionette.
     *
     * @author gigazin
     * @since 1.0.0-bt1
     */
    @FXML
    protected void onRunButtonClick() {

        // Sets the URL to URLField content.
        setURL(URLField.getCharacters().toString());

        /*
        * Checks if the file is not imported;
        * Checks if the folder is not imported;
        * Checks if the URL is empty;
        * Shows an error accordingly and don't allow the marionette to run.
        *
        * If all requirements are satisfied, shows a confirmation dialog
        * with instructions, sets action to true (allow run loop)
        * and runs the marionette.
        */
        if (getTorPath() == null || pathInjectedFile.getText().equalsIgnoreCase("File not imported!")) {
            new Alert(Alert.AlertType.ERROR, "File path is null. Please select file before attempting to run.")
            .show();
        } else if (getProfilePath() == null || pathInjectedFolder.getText().equalsIgnoreCase("Folder not imported!")) {
            new Alert(Alert.AlertType.ERROR, "Folder path is null. Please select the folder before attempting to run" +
                    ".").show();
        } else if (getDriverPath() == null || pathInjectedDriver.getText().equalsIgnoreCase("Driver not imported!")) {
            new Alert(Alert.AlertType.ERROR, "Driver path is null. Please select the driver before attempting to run" +
                    ".").show();
        } else if (getURL() == null || getURL().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "URL is null. Please insert the URL before attempting to run.")
            .show();
        } else {
            new Alert(Alert.AlertType.CONFIRMATION, "Remember to click the Stop button before making " +
                    "any changes and attempting to run again.", ButtonType.OK)
                    .showAndWait();
            setAction(true);
            run();
        }

    }

    /**
     <p>Controls the "stop" button to set action to false and stop marionette looping run.</p>
     *
     * @author gigazin
     * @since 1.0.0-bt1
     */
    @FXML
    protected void onStopButtonClick() {
        if (getActionStatus()) driver.quit();
        setAction(false);
        this.statusText.setText("AutomaTor Offline");
        this.statusCircle.setFill(javafx.scene.paint.Color.RED);
    }

    private void setAction(boolean newAction) {
        this.action = newAction;
    }

    private boolean getActionStatus() {
        return this.action;
    }

    private void setTorPath(String newTorPath) {
        this.torPath = newTorPath;
    }

    private String getTorPath() {
        return this.torPath;
    }

    private void setProfilePath(String newProfilePath) {
        this.profilePath = newProfilePath;
    }

    private String getProfilePath() {
        return this.profilePath;
    }

    private void setProfile(File path) {
        this.profile = new FirefoxProfile(path);
    }

    private void setDriverPath(String newDriverPath) {
        this.driverPath = newDriverPath;
    }

    private String getDriverPath() {
        return this.driverPath;
    }

    private FirefoxProfile getProfile() {
        return this.profile;
    }

    private void setURL(String newURL) {
        this.URL = newURL;
    }

    private String getURL() {
        return this.URL;
    }

    private void setTimer(int newTimer) {
        this.timer = newTimer;
    }

    private int getTimer() {
        return this.timer;
    }

    /**
     <p>
     If you found this repository by searching "Tor with Selenium", here's what you came here for. This function is
     creating and setting up the driver in order for the marionette to be able to run.<br>
     <br>
     Variable "driver" is declared as attribute at the very beginning of the class in case you're lost trying to find
     its declaration.<br>
     <br>
     Check all getters and setters as well as onSelectFileButtonClick() and onSelectFolderButtonClick() if you're in doubt on how Tor path and Profile are set.</p>
     * @see #onSelectFileButtonClick()
     * @see #onSelectFolderButtonClick()
     * @author gigazin
     * @since 1.0.0-bt1
     */
    private void setupBeforeRun() {

        // Run in IDE
//            System.setProperty("webdriver.gecko.driver",
//            "src/main/resources/com/github/gigazin/torwithselenium/driver/geckodriver.exe");

        SecureRandom r = new SecureRandom();
        int min = 90000;
        int max = 300000;
        setTimer(r.nextInt((max + 1 - min) + min));

        System.setProperty("webdriver.gecko.driver", getDriverPath());
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(getTorPath());
        options.setProfile(getProfile());
        this.driver = new FirefoxDriver(options);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusText.setText("AutomaTor Online");
                statusCircle.setFill(javafx.scene.paint.Color.GREEN);
            }
        });

    }

    /**
     <p>
     Infinitely loops the marionette until user clicks the Stop button.</p>
     *
     * @author gigazin
     * @since 1.0.0-bt1
     */
    private void run() {

        Thread exec = new Thread(() -> {
            System.out.println("Thread is running...");
            while (getActionStatus()) {
                try {
                    System.out.println("Thread loop is running...");
                    setupBeforeRun();
                    Thread.sleep(5000); // Waiting for Tor to connect to the network.
                    driver.get(getURL());
                    Thread.sleep(10000); // Waiting for page to load.
                    try {
                        // Checking if YouTube cookie consent dialog is present and clicking on "Accept all" button.
                        if (!driver.findElements(By.xpath("//*[@id=\"dialog\"]")).isEmpty()) {
                            driver.findElement(By.xpath("/html/body/ytd-app/ytd-consent-bump-v2-lightbox/tp-yt-paper" +
                                    "-dialog/div[4]/div[2]/div[6]/div[1]/ytd-button-renderer[2]/yt-button-shape" +
                                    "/button")).click();
                            Thread.sleep(10000); // Waiting for a possible page reload.
                        }

                        // Clicking on the video play button.
                        driver.findElement(By.xpath("/html/body/ytd-app/div[1]/ytd-page-manager/ytd-watch-flexy/div[5" +
                                "]/div[1]/div/div[1]/div[2]/div/div/ytd-player/div/div/div[30]/div[2]/div[1]/button")).click();

                        Thread.sleep(getTimer()); // Waiting for a random generated time before closing the driver.
                        driver.quit();
                    } catch (NoSuchElementException e) {
                        System.out.println("Error while trying to find element: " + e.getMessage());
                        System.out.println("This may be due to the page not loading in time for the element to be found.");
                        System.out.println("Trying again in 10 seconds...");

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("AutomaTor Offline");
                                statusCircle.setFill(javafx.scene.paint.Color.RED);
                            }
                        });

                        driver.quit();
                    }
                    // Giving a 2 seconds gap between each loop to avoid Tor trying to run 2 instances at the same time.
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            statusText.setText("AutomaTor Offline");
                            statusCircle.setFill(javafx.scene.paint.Color.RED);
                            new Alert(Alert.AlertType.ERROR, "Error while running WebDriver: " + e.getMessage()).showAndWait();
                        }
                    });
                }
            }
        });

        exec.setName("Marionette Loop");
        exec.start();

    }

}