package com.github.gigazin.torwithselenium.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
 * @version 1.0.0-bt1
 * @since 10/15/2023
 */
public class YouBotController {

    @FXML
    private TextArea instructionsTextFile;
    @FXML
    private TextArea instructionsTextFolder;
    @FXML
    private TextField URLField;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button selectFolderButton;
    @FXML
    private Button runButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label pathInjectedFile;
    @FXML
    private Label pathInjectedFolder;

    private boolean action;
    private String torPath;
    private String profilePath;
    private String URL;
    private FirefoxProfile profile;
    private WebDriver driver;

    /**
     * Controls the "Select File" button to open a file chooser dialog and save the selected file path.
     * File selection is required to run the marionette.
     *
     * @author gigazin
     * @since 10/15/2023
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
     * @since 15/10/2023
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
     * Controls the "run" button to show errors regarding the requirements to run the marionette.
     * Shows a confirmation dialog when all the requirements are satisfied and runs the marionette.
     *
     * @author gigazin
     * @since 10/15/2023
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
        * If every requirement is satisfied, shows a confirmation dialog
        * with instructions, sets action to true (allow run loop)
        * and runs the marionette.
        */
        if (getTorPath() == null || pathInjectedFile.getText().equalsIgnoreCase("File not imported!")) {
            new Alert(Alert.AlertType.ERROR, "File path is null. Please select file before attempting to run.")
            .show();
        } else if (getProfilePath() == null || pathInjectedFolder.getText().equalsIgnoreCase("Folder not imported!")) {
            new Alert(Alert.AlertType.ERROR, "Folder path is null. Please select the folder before attempting to run" +
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
     <p>Controls the "stop" button to set action to false and stop marionette looping run.<br>
     This button may not be working as intended (version 1.0.0-bt1): crash caused by run() doesn't allow the button
     to be clicked.</p>
     *
     * @author gigazin
     * @since 10/15/2023
     */
    @FXML
    protected void onStopButtonClick() {
        setAction(false);
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

    private FirefoxProfile getProfile() {
        return this.profile;
    }

    private void setURL(String newURL) {
        this.URL = newURL;
    }

    private String getURL() {
        return this.URL;
    }

    /**
     <p>
     If you found this repository by searching "Tor with Selenium", here's what you came here for. This method is
     creating and setting up the driver in order for the marionette to be able to run.<br>
     <br>
     Variable "driver" is declared as attribute at the very beginning of the class in case you're lost trying to find
     its declaration.<br>
     <br>
     Check all getters and setters as well as onSelectFileButtonClick() and onSelectFolderButtonClick() if you're in doubt on how Tor path and Profile are set.</p>
     *
     * @author gigazin
     * @since 10/15/2023
     */
    private void setupBeforeRun() {

        System.setProperty("webdriver.gecko.driver", "src/main/resources/com/github/gigazin/torwithselenium/geckodriver-v0.33.0-win64/geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(getTorPath());
        options.setProfile(getProfile());
        this.driver = new FirefoxDriver(options);

    }

    /**
     <p>
     Infinitely loops the marionette until user clicks the Stop button.<br>
     This method is working but not as intended (version 1.0.0-bt1): loop is running as intended but application
     interface stops responding.</p>
     *
     * @author gigazin
     * @since 10/15/2023
     */
    private void run() {

        while (getActionStatus()) {
            try {
                setupBeforeRun();
                driver.get("about:blank");
                Thread.sleep(3000);
                driver.get(getURL());
                Thread.sleep(20000);
                driver.quit();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                new Alert(Alert.AlertType.ERROR, "Error while running WebDriver: " + e.getMessage()).showAndWait();
            }
        }

    }

}