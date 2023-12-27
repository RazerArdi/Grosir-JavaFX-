package PemprogramanLanjutTB;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utama extends Application {
    private Button loginButton, registerButton, forgotPasswordButton;
    private Scene loginScene, registerScene, forgotPasswordScene;
    private TextField emailField, passwordField;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("GTrade");
        stage.getIcons().add(new Image(Utama.class.getResourceAsStream("/Designer.png")));
        initializeScenes(stage);
        stage.setScene(loginScene);
        stage.show();
    }

    private void initializeScenes(Stage stage) {
        createLoginScene(stage);
        createRegisterScene(stage);
        createForgotPasswordScene(stage);
    }

    private void createLoginScene(Stage stage) {
        Label helloLabel = new Label("Hello");
        helloLabel.setStyle("-fx-font-size: 50; -fx-text-fill: linear-gradient(to right, blue, red)");

        Label signInLabel = new Label("Sign in to your account");
        signInLabel.setStyle("-fx-font-size: 10");

        emailField = new TextField();
        emailField.setPromptText("Email/Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14; -fx-background-color: #1877f2; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> handleLogin(stage, emailField.getText(), passwordField.getText()));

        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setOnAction(e -> stage.setScene(registerScene));

        Hyperlink forgotPasswordLink = new Hyperlink("Click here ");
        forgotPasswordLink.setOnAction(e -> stage.setScene(forgotPasswordScene));

        TextFlow textFlow = new TextFlow(
                new Text("\t\t\t\t\t"),
                new Text("Don't have an Account? "),
                registerLink,
                new Text("\n"),
                new Text("\t\t\t\t\t"),
                new Text("Forgot password? "),
                forgotPasswordLink
        );

        VBox loginLayout = new VBox(10);
        loginLayout.getChildren().addAll(helloLabel, signInLabel, emailField, passwordField, loginButton, textFlow);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #EEF5FF; -fx-padding: 20;");

        loginButton.setPrefSize(150, 30);

        loginScene = new Scene(loginLayout, 500, 600);
    }

    private void createRegisterScene(Stage stage) {
        Label registerHelloLabel = new Label("Hello");
        registerHelloLabel.setStyle("-fx-font-size: 25");

        Label registerLabel = new Label("Register for a new account");
        registerLabel.setStyle("-fx-font-size: 10");

        TextField registerNameField = new TextField();
        registerNameField.setPromptText("Name");

        TextField registerUsernameField = new TextField();
        registerUsernameField.setPromptText("Username");

        TextField registerEmailField = new TextField();
        registerEmailField.setPromptText("Email");

        PasswordField registerPasswordField = new PasswordField();
        registerPasswordField.setPromptText("Password");

        PasswordField confirmRegisterPasswordField = new PasswordField();
        confirmRegisterPasswordField.setPromptText("Confirm Password");

        Button registerSubmitButton = new Button("Register");
        registerSubmitButton.setOnAction(e -> handleRegistration(stage, registerNameField.getText(), registerUsernameField.getText(),
                registerEmailField.getText(), registerPasswordField.getText(), confirmRegisterPasswordField.getText()));

        Hyperlink backToLoginLink = new Hyperlink("Back to Log In");
        backToLoginLink.setOnAction(e -> stage.setScene(loginScene));

        VBox registerLayout = new VBox(20);
        registerLayout.getChildren().addAll(registerHelloLabel, registerLabel, registerNameField, registerUsernameField,
                registerEmailField, registerPasswordField, confirmRegisterPasswordField, registerSubmitButton, backToLoginLink);
        registerLayout.setAlignment(Pos.CENTER);

        registerScene = new Scene(registerLayout, 500, 600);
    }

    private void createForgotPasswordScene(Stage stage) {
        Label forgotPasswordLabel = new Label("Masukkan Alamat Email");
        TextField forgotPasswordEmailField = new TextField();
        forgotPasswordEmailField.setPromptText("Email");

        Button submitForgotPasswordButton = new Button("Submit");
        submitForgotPasswordButton.setOnAction(e -> handleForgotPassword(stage, forgotPasswordEmailField.getText()));

        VBox forgotPasswordLayout = new VBox(5);
        forgotPasswordLayout.getChildren().addAll(forgotPasswordLabel, forgotPasswordEmailField, submitForgotPasswordButton);
        forgotPasswordLayout.setAlignment(Pos.CENTER);

        forgotPasswordScene = new Scene(forgotPasswordLayout, 500, 600);
    }

    private void handleLogin(Stage stage, String emailOrUsername, String password) {
        List<String> userData;
        try {
            userData = Files.readAllLines(Paths.get("DataUser.txt"));
        } catch (IOException e) {
            showErrorDialog("Error reading user data.");
            return;
        }

        for (String line : userData) {
            String[] parts = line.split(",");
            String storedEmail = parts[2];
            String storedUsername = parts[1];
            String storedPassword = parts[3];

            if ((storedEmail.equals(emailOrUsername) || storedUsername.equals(emailOrUsername)) && storedPassword.equals(password)) {
                showSuccessDialog("Login successful!");

                HalamanUtama halamanUtama = new HalamanUtama(stage);
                stage.setScene(new Scene(halamanUtama, 500, 600));
                return;
            }
        }

        showErrorDialog("Invalid email/username or password.");
    }

    private void handleRegistration(Stage stage, String fullName, String username, String email, String password, String confirmPassword) {
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorDialog("Passwords do not match.");
            return;
        }

        String userData = fullName + "," + username + "," + email + "," + password;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("DataUser.txt", true))) {
            writer.write(userData);
            writer.newLine();
            showSuccessDialog("Registration successful!");
        } catch (IOException e) {
            showErrorDialog("Error saving user data.");
        }
    }

    private void handleForgotPassword(Stage stage, String email) {
        if (isValidEmail(email)) {
            saveRequestData(email);
            showSuccessDialog("Request submitted. You will be contacted by customer service.");
            stage.setScene(loginScene);
        } else {
            showErrorDialog("Invalid email address.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@(gmail\\.com|outlook\\.com|webmail\\.umm\\.ac\\.id)");
    }

    private void saveRequestData(String email) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PermintaanPerubahanData.txt", true))) {
            writer.write(email);
            writer.newLine();
        } catch (IOException e) {
            showErrorDialog("Error saving request data.");
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class HalamanUtama extends BorderPane {

        public HalamanUtama(Stage stage) {

            VBox body = new VBox();
            body.setSpacing(5);
            body.setPadding(new Insets(10));


            HBox pesananBaruBox = new HBox();
            pesananBaruBox.setStyle("-fx-background-color: #f0f0f0;");
            pesananBaruBox.setPadding(new Insets(10));
            pesananBaruBox.setSpacing(10);


            HBox siapDikirimBox = new HBox();
            siapDikirimBox.setStyle("-fx-background-color: #f0f0f0;");
            siapDikirimBox.setPadding(new Insets(10));
            siapDikirimBox.setSpacing(10);


            HBox analisisPenjualanBox = new HBox();
            analisisPenjualanBox.setStyle("-fx-background-color: #f0f0f0;");
            analisisPenjualanBox.setPadding(new Insets(10));
            analisisPenjualanBox.setSpacing(10);


            Separator separator = new Separator();


            HBox homeBox = new HBox();
            homeBox.setStyle("-fx-background-color: #f0f0f0;");
            homeBox.setPadding(new Insets(10));
            homeBox.setSpacing(10);

            Image homeImage = new Image(Utama.class.getResourceAsStream("/home.png"));
            ImageView homeImageView = new ImageView(homeImage);
            homeImageView.setFitWidth(30);
            homeImageView.setFitHeight(30);

            homeBox.getChildren().addAll(homeImageView, new Label("Home"), new Label("Product"), new Label("Invoice"), new Label("Warranty"));

            body.getChildren().addAll(pesananBaruBox, siapDikirimBox, analisisPenjualanBox, separator, homeBox);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), homeBox);
            slideIn.setFromY(600);
            slideIn.setToY(0);

            homeBox.setOnMouseClicked(event -> {
                if (slideIn.getStatus() == Animation.Status.RUNNING) {
                    slideIn.stop();
                } else {
                    slideIn.play();
                }
            });

            setCenter(body);
        }
    }
}
