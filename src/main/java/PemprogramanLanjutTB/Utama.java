package PemprogramanLanjutTB;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javafx.scene.shape.Line;

import static java.awt.Color.*;


public class Utama extends Application {
    private Button loginButton, registerButton, forgotPasswordButton;
    private Scene loginScene, registerScene, forgotPasswordScene;
    private TextField emailField, passwordField;
    private static VBox imeiContentBox;
    private static TextField imeiField;
    private static Button searchImeiButton;
    private static Label statusLabel;
    private static Label productLabel;
    private static Label imeiResultLabel;
    private static Label activationDateLabel;



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

        loginScene = new Scene(loginLayout, 650, 700);
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

        registerScene = new Scene(registerLayout, 650, 700);
    }

    private void createForgotPasswordScene(Stage stage) {
        Hyperlink registerLink = new Hyperlink("Kembali");
        registerLink.setOnAction(e -> stage.setScene(loginScene));
        registerLink.setLayoutX(10);
        registerLink.setLayoutY(10);
        VBox back = new VBox(5);
        back.getChildren().addAll(registerLink);
        back.setAlignment(Pos.TOP_LEFT);

        Label forgotPasswordLabel = new Label("Masukkan Alamat Email");
        TextField forgotPasswordEmailField = new TextField();
        forgotPasswordEmailField.setPromptText("Email");

        Button submitForgotPasswordButton = new Button("Submit");
        submitForgotPasswordButton.setOnAction(e -> handleForgotPassword(stage, forgotPasswordEmailField.getText()));

        VBox forgotPasswordLayout = new VBox(5);
        forgotPasswordLayout.getChildren().addAll(back,forgotPasswordLabel, forgotPasswordEmailField, submitForgotPasswordButton);
        forgotPasswordLayout.setAlignment(Pos.CENTER);
        forgotPasswordLayout.setStyle("-fx-background-color: #EEF5FF; -fx-padding: 20;");

        forgotPasswordScene = new Scene(forgotPasswordLayout, 650, 700);
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

                Beranda halamanUtama = new Beranda(stage);
                stage.setScene(new Scene(halamanUtama, 650, 700));
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

    private static void showErrorDialog(String message) {
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

    private static class Beranda extends BorderPane {
        public Beranda(Stage stage) {

            HBox headerBox = new HBox();
            headerBox.setStyle("-fx-background-color: #4CAF50;");
            headerBox.setPadding(new Insets(10));
            headerBox.setSpacing(10);

            Image profileImage = new Image(Utama.class.getResourceAsStream("/cus.png"));
            ImageView profileImageView = new ImageView(profileImage);
            profileImageView.setFitWidth(30);
            profileImageView.setFitHeight(30);
            profileImageView.setOnMouseClicked(e -> handleprofileButton());

            Label gtradeLabel = new Label("\t\t\tGTrade");
            gtradeLabel.setStyle("-fx-font-size: 25; -fx-text-fill: white;");
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            Image gtradeLogo = new Image(Utama.class.getResourceAsStream("/Designer.png"));
            ImageView gtradeLogoView = new ImageView(gtradeLogo);
            gtradeLogoView.setFitWidth(100);
            gtradeLogoView.setFitHeight(100);
            VBox logoBox = new VBox();
            logoBox.setAlignment(Pos.TOP_CENTER);
            logoBox.getChildren().add(gtradeLogoView);
            setCenter(logoBox);

            Button searchButton = new Button();
            searchButton.setStyle("-fx-background-color: #4CAF50;");
            Image searchImage = new Image(Utama.class.getResourceAsStream("/se.png"));
            ImageView searchImageView = new ImageView(searchImage);
            searchImageView.setFitWidth(30);
            searchImageView.setFitHeight(30);
            searchButton.setGraphic(searchImageView);

            Button notificationButton = new Button();
            notificationButton.setStyle("-fx-background-color: #4CAF50;");
            Image notificationImage = new Image(Utama.class.getResourceAsStream("/not.png"));
            ImageView notificationImageView = new ImageView(notificationImage);
            notificationImageView.setFitWidth(30);
            notificationImageView.setFitHeight(30);
            notificationButton.setGraphic(notificationImageView);

            headerBox.getChildren().addAll(profileImageView, gtradeLabel, region, searchButton, notificationButton);
            setTop(headerBox);

            HBox bottomBarBox = new HBox();
            bottomBarBox.setStyle("-fx-background-color: #4CAF50;");
            bottomBarBox.setPadding(new Insets(10));
            bottomBarBox.setSpacing(25);

            Image bottomHomeImage = new Image(Utama.class.getResourceAsStream("/home.png"));
            ImageView bottomHomeImageView = new ImageView(bottomHomeImage);
            bottomHomeImageView.setFitWidth(30);
            bottomHomeImageView.setFitHeight(30);

            Button homeButton = new Button();
            homeButton.setStyle("-fx-background-color: transparent;");
            homeButton.setGraphic(bottomHomeImageView);
            homeButton.setOnAction(e -> handleHomeButton());

            Region spacer1 = new Region();
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Image bottomProductImage = new Image(Utama.class.getResourceAsStream("/gadget.png"));
            ImageView bottomProductImageView = new ImageView(bottomProductImage);
            bottomProductImageView.setFitWidth(30);
            bottomProductImageView.setFitHeight(30);
            Button gadgetButton = new Button();
            gadgetButton.setStyle("-fx-background-color: transparent;");
            gadgetButton.setGraphic(bottomProductImageView);
            gadgetButton.setOnAction(e -> handleGadgetButton());

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);


            Image bottomInvoiceImage = new Image(Utama.class.getResourceAsStream("/invoice.png"));
            ImageView bottomInvoiceImageView = new ImageView(bottomInvoiceImage);
            Button InvoiceButton = new Button();
            bottomInvoiceImageView.setFitWidth(30);
            bottomInvoiceImageView.setFitHeight(30);
            InvoiceButton.setStyle("-fx-background-color: transparent;");
            InvoiceButton.setGraphic(bottomInvoiceImageView);
            InvoiceButton.setOnAction(e -> handleInvoiceButton());


            Region spacer3 = new Region();
            HBox.setHgrow(spacer3, Priority.ALWAYS);

            Image bottomWarrantyImage = new Image(Utama.class.getResourceAsStream("/warranty.png"));
            ImageView bottomWarrantyImageView = new ImageView(bottomWarrantyImage);
            bottomWarrantyImageView.setFitWidth(30);
            bottomWarrantyImageView.setFitHeight(30);

            Button warrantyButton = new Button();
            warrantyButton.setStyle("-fx-background-color: transparent;");
            warrantyButton.setGraphic(bottomWarrantyImageView);
            warrantyButton.setOnAction(e -> handleWarrantyButton());

            bottomBarBox.getChildren().addAll(bottomHomeImageView, spacer1, gadgetButton, spacer2, InvoiceButton, spacer3, warrantyButton);

            setBottom(bottomBarBox);


            VBox mainContent = new VBox(10);
            mainContent.setPadding(new Insets(10));


            VBox box1 = createInfoBox("Pesanan Baru", "box1");

            VBox box2 = createInfoBox("Siap Dikirim", "box2");

            VBox box3 = createInfoBox("Profit", "box3");

            VBox box4 = createInfoBox("Terjual!!!", "box4");

            LineChart<String, Number> lineChart = createLineChart();

            lineChart.setPrefWidth(500);
            lineChart.setPrefHeight(300);

            mainContent.getChildren().addAll(box1, box2, box3, box4, lineChart);
            setLeft(mainContent);
            createImeiContent();
        }

        private void handleInvoiceButton() {
            BorderPane gadLayout = new BorderPane();

            HBox headerBox = new HBox();
            headerBox.setStyle("-fx-background-color: #4CAF50;");
            headerBox.setPadding(new Insets(10));
            headerBox.setSpacing(10);

            Image profileImage = new Image(Utama.class.getResourceAsStream("/cus.png"));
            ImageView profileImageView = new ImageView(profileImage);
            profileImageView.setFitWidth(30);
            profileImageView.setFitHeight(30);
            profileImageView.setOnMouseClicked(e -> handleprofileButton());

            Label gtradeLabel = new Label("\t\t\tGTrade");
            gtradeLabel.setStyle("-fx-font-size: 25; -fx-text-fill: white;");
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            Image gtradeLogo = new Image(Utama.class.getResourceAsStream("/Designer.png"));
            ImageView gtradeLogoView = new ImageView(gtradeLogo);
            gtradeLogoView.setFitWidth(100);
            gtradeLogoView.setFitHeight(100);
            VBox logoBox = new VBox();
            logoBox.setAlignment(Pos.TOP_CENTER);
            logoBox.getChildren().add(gtradeLogoView);

            Button searchButton = new Button();
            searchButton.setStyle("-fx-background-color: #4CAF50;");
            Image searchImage = new Image(Utama.class.getResourceAsStream("/se.png"));
            ImageView searchImageView = new ImageView(searchImage);
            searchImageView.setFitWidth(30);
            searchImageView.setFitHeight(30);
            searchButton.setGraphic(searchImageView);

            Button notificationButton = new Button();
            notificationButton.setStyle("-fx-background-color: #4CAF50;");
            Image notificationImage = new Image(Utama.class.getResourceAsStream("/not.png"));
            ImageView notificationImageView = new ImageView(notificationImage);
            notificationImageView.setFitWidth(30);
            notificationImageView.setFitHeight(30);
            notificationButton.setGraphic(notificationImageView);

            headerBox.getChildren().addAll(profileImageView, gtradeLabel, region, searchButton, notificationButton);
            gadLayout.setTop(headerBox);


            VBox InvoiceContent = new VBox(10);
            Label Invoice = new Label("Invoice Content");
            Text gadgetDescription = new Text("This is the content of the Invoice page.");
            InvoiceContent.getChildren().addAll(Invoice, gadgetDescription);

            gadLayout.setCenter(InvoiceContent);

            HBox bottomBarBoxGad = new HBox();
            bottomBarBoxGad.setStyle("-fx-background-color: #4CAF50;");
            bottomBarBoxGad.setPadding(new Insets(10));
            bottomBarBoxGad.setSpacing(25);

            HBox bottomBarBox = new HBox();
            bottomBarBox.setStyle("-fx-background-color: #4CAF50;");
            bottomBarBox.setPadding(new Insets(10));
            bottomBarBox.setSpacing(25);

            Image bottomHomeImage = new Image(Utama.class.getResourceAsStream("/home.png"));
            ImageView bottomHomeImageView = new ImageView(bottomHomeImage);
            bottomHomeImageView.setFitWidth(30);
            bottomHomeImageView.setFitHeight(30);

            Button homeButton = new Button();
            homeButton.setStyle("-fx-background-color: transparent;");
            homeButton.setGraphic(bottomHomeImageView);
            homeButton.setOnAction(e -> handleHomeButton());

            Region spacer1 = new Region();
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Image bottomProductImage = new Image(Utama.class.getResourceAsStream("/gadget.png"));
            ImageView bottomProductImageView = new ImageView(bottomProductImage);
            bottomProductImageView.setFitWidth(30);
            bottomProductImageView.setFitHeight(30);
            Button gadgetButton = new Button();
            gadgetButton.setStyle("-fx-background-color: transparent;");
            gadgetButton.setGraphic(bottomProductImageView);
            gadgetButton.setOnAction(e -> handleGadgetButton());

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);


            Image bottomInvoiceImage = new Image(Utama.class.getResourceAsStream("/invoice.png"));
            ImageView bottomInvoiceImageView = new ImageView(bottomInvoiceImage);
            Button InvoiceButton = new Button();
            bottomInvoiceImageView.setFitWidth(30);
            bottomInvoiceImageView.setFitHeight(30);
            InvoiceButton.setStyle("-fx-background-color: transparent;");
            InvoiceButton.setGraphic(bottomInvoiceImageView);
            InvoiceButton.setOnAction(e -> handleInvoiceButton());


            Region spacer3 = new Region();
            HBox.setHgrow(spacer3, Priority.ALWAYS);

            Image bottomWarrantyImage = new Image(Utama.class.getResourceAsStream("/warranty.png"));
            ImageView bottomWarrantyImageView = new ImageView(bottomWarrantyImage);
            bottomWarrantyImageView.setFitWidth(30);
            bottomWarrantyImageView.setFitHeight(30);

            Button warrantyButton = new Button();
            warrantyButton.setStyle("-fx-background-color: transparent;");
            warrantyButton.setGraphic(bottomWarrantyImageView);
            warrantyButton.setOnAction(e -> handleWarrantyButton());

            bottomBarBoxGad.getChildren().addAll(bottomHomeImageView, spacer1, gadgetButton, spacer2, InvoiceButton, spacer3, warrantyButton);

            gadLayout.setBottom(bottomBarBoxGad);

            Scene scene = new Scene(gadLayout, 650, 700);
            Stage stage = new Stage();
            stage.setTitle("Invoice");
            stage.setScene(scene);
            stage.show();
        }

        private void handleGadgetButton() {
            BorderPane gadLayout = new BorderPane();

            HBox headerBox = new HBox(10);
            headerBox.setStyle("-fx-background-color: #4CAF50;");
            headerBox.setPadding(new Insets(10));
            headerBox.setSpacing(10);

            Image profileImage = new Image(Utama.class.getResourceAsStream("/cus.png"));
            ImageView profileImageView = new ImageView(profileImage);
            profileImageView.setFitWidth(30);
            profileImageView.setFitHeight(30);
            profileImageView.setOnMouseClicked(e -> handleprofileButton());

            Label gtradeLabel = new Label("\t\t\tGTrade");
            gtradeLabel.setStyle("-fx-font-size: 25; -fx-text-fill: white;");
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            Image gtradeLogo = new Image(Utama.class.getResourceAsStream("/Designer.png"));
            ImageView gtradeLogoView = new ImageView(gtradeLogo);
            gtradeLogoView.setFitWidth(100);
            gtradeLogoView.setFitHeight(100);
            VBox logoBox = new VBox();
            logoBox.setAlignment(Pos.TOP_CENTER);
            logoBox.getChildren().add(gtradeLogoView);

            Button searchButton = new Button();
            searchButton.setStyle("-fx-background-color: #4CAF50;");
            Image searchImage = new Image(Utama.class.getResourceAsStream("/se.png"));
            ImageView searchImageView = new ImageView(searchImage);
            searchImageView.setFitWidth(30);
            searchImageView.setFitHeight(30);
            searchButton.setGraphic(searchImageView);

            Button notificationButton = new Button();
            notificationButton.setStyle("-fx-background-color: #4CAF50;");
            Image notificationImage = new Image(Utama.class.getResourceAsStream("/not.png"));
            ImageView notificationImageView = new ImageView(notificationImage);
            notificationImageView.setFitWidth(30);
            notificationImageView.setFitHeight(30);
            notificationButton.setGraphic(notificationImageView);

            headerBox.getChildren().addAll(profileImageView, gtradeLabel, region, searchButton, notificationButton);
            gadLayout.setTop(headerBox);

            VBox gadgetContent = new VBox(10);
            Label gadgetLabel = new Label("Daftar Produk");
            gadgetLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-font-weight: bold;");
            Text gadgetDescription = new Text("This is the content of the Gadget page.");
            Line garis = new Line(50, 50, 3000, 50);

            gadgetContent.getChildren().addAll(gadgetLabel,garis, gadgetDescription);

            gadLayout.setCenter(gadgetContent);

            HBox bottomBarBoxGad = new HBox();
            bottomBarBoxGad.setStyle("-fx-background-color: #4CAF50;");
            bottomBarBoxGad.setPadding(new Insets(10));
            bottomBarBoxGad.setSpacing(25);

            Image bottomHomeImage = new Image(Utama.class.getResourceAsStream("/home.png"));
            ImageView bottomHomeImageView = new ImageView(bottomHomeImage);
            bottomHomeImageView.setFitWidth(30);
            bottomHomeImageView.setFitHeight(30);

            Button homeButton = new Button();
            homeButton.setStyle("-fx-background-color: transparent;");
            homeButton.setGraphic(bottomHomeImageView);
            homeButton.setOnAction(e -> handleHomeButton());

            Region spacer1 = new Region();
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            Image bottomProductImage = new Image(Utama.class.getResourceAsStream("/gadget.png"));
            ImageView bottomProductImageView = new ImageView(bottomProductImage);
            bottomProductImageView.setFitWidth(30);
            bottomProductImageView.setFitHeight(30);

            Button gadgetButton = new Button();
            gadgetButton.setStyle("-fx-background-color: transparent;");
            gadgetButton.setGraphic(bottomProductImageView);
            gadgetButton.setOnAction(e -> handleGadgetButton());

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            Image bottomInvoiceImage = new Image(Utama.class.getResourceAsStream("/invoice.png"));
            ImageView bottomInvoiceImageView = new ImageView(bottomInvoiceImage);
            bottomInvoiceImageView.setFitWidth(30);
            bottomInvoiceImageView.setFitHeight(30);

            Button InvoiceButton = new Button();
            InvoiceButton.setStyle("-fx-background-color: transparent;");
            InvoiceButton.setGraphic(bottomInvoiceImageView);
            InvoiceButton.setOnAction(e -> handleInvoiceButton());

            Region spacer3 = new Region();
            HBox.setHgrow(spacer3, Priority.ALWAYS);

            Image bottomWarrantyImage = new Image(Utama.class.getResourceAsStream("/warranty.png"));
            ImageView bottomWarrantyImageView = new ImageView(bottomWarrantyImage);
            bottomWarrantyImageView.setFitWidth(30);
            bottomWarrantyImageView.setFitHeight(30);

            Button warrantyButton = new Button();
            warrantyButton.setStyle("-fx-background-color: transparent;");
            warrantyButton.setGraphic(bottomWarrantyImageView);
            warrantyButton.setOnAction(e -> handleWarrantyButton());

            bottomBarBoxGad.getChildren().addAll(bottomHomeImageView, spacer1, gadgetButton, spacer2, InvoiceButton, spacer3, warrantyButton);
            gadLayout.setBottom(bottomBarBoxGad);

            Scene scene = new Scene(gadLayout, 650, 700);
            Stage stage = new Stage();
            stage.setTitle("Gadget");
            stage.setScene(scene);
            stage.show();
        }



        private static void handleprofileButton() {
            String currentUser = "admin";

            try {
                List<String> userData = Files.readAllLines(Paths.get("DataUser.txt"));

                for (String line : userData) {
                    String[] parts = line.split(",");
                    String storedUsername = parts[1];

                    if (storedUsername.equals(currentUser)) {
                        String storedEmail = parts[2];
                        String storedName = parts[0];

                        showUserProfile(storedName, storedEmail);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void showUserProfile(String name, String email) {
            VBox userProfileBox = new VBox(10);
            userProfileBox.setAlignment(Pos.CENTER);
            userProfileBox.setPadding(new Insets(10));

            Label nameLabel = new Label("Name: " + name);
            Label emailLabel = new Label("Email: " + email);

            userProfileBox.getChildren().addAll(nameLabel, emailLabel);

            Stage userProfileStage = new Stage();
            userProfileStage.setTitle("User Profile");
            userProfileStage.setScene(new Scene(userProfileBox, 300, 200));
            userProfileStage.show();
        }

        private void handleHomeButton() {
            createHomeContent();
        }

        private static void createHomeContent() {
            VBox homeContentBox = new VBox(10);
            homeContentBox.setAlignment(Pos.CENTER);

            Label homeTitleLabel = new Label("Home");
            homeTitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");


            homeContentBox.getChildren().addAll(homeTitleLabel);

            Stage newStage = new Stage();
            newStage.setTitle("Home");
            newStage.setScene(new Scene(homeContentBox, 400, 400));
            newStage.show();
        }


        private VBox createInfoBox(String title, String content) {
            VBox infoBox = new VBox();
            infoBox.setAlignment(Pos.TOP_LEFT);
            infoBox.setPadding(new Insets(10));
            infoBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: #E1EFFF;");

            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

            Label contentLabel = new Label(content);
            contentLabel.setStyle("-fx-font-size: 12;");

            infoBox.getChildren().addAll(titleLabel, contentLabel);
            return infoBox;
        }

        private LineChart<String, Number> createLineChart() {
            ObservableList<XYChart.Series<String, Number>> lineChartData = FXCollections.observableArrayList(
                    new XYChart.Series<>("Rupiah", FXCollections.observableArrayList(
                            new XYChart.Data<>("Jan", 500),
                            new XYChart.Data<>("Feb", 800),
                            new XYChart.Data<>("Mar", 1200),
                            new XYChart.Data<>("Dec", 1500)
                    )),
                    new XYChart.Series<>("Tanggal", FXCollections.observableArrayList(
                            new XYChart.Data<>("Jan", 10),
                            new XYChart.Data<>("Feb", 20),
                            new XYChart.Data<>("Mar", 30),
                            new XYChart.Data<>("Dec", 50)
                    ))
            );

            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis, lineChartData);
            lineChart.setTitle("Grafik Penjualan");
            return lineChart;
        }
        private static VBox createImeiContent() {
            VBox imeiContentBox = new VBox(10);
            imeiContentBox.setAlignment(Pos.CENTER);

            Label imeiTitleLabel = new Label("Cek IMEI");
            imeiTitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

            imeiField = new TextField();
            imeiField.setPromptText("Masukkan IMEI");

            searchImeiButton = new Button("Search");
            searchImeiButton.setOnAction(e -> handleImeiSearch());

            statusLabel = new Label("");
            productLabel = new Label("");
            imeiResultLabel = new Label("");
            activationDateLabel = new Label("");

            GridPane resultGrid = new GridPane();
            resultGrid.addRow(0, new Label("    Status Garansi:  "), statusLabel);
            resultGrid.addRow(1, new Label("    Produk:  "), productLabel);
            resultGrid.addRow(2, new Label("    IMEI:  "), imeiResultLabel);
            resultGrid.addRow(3, new Label("    Tanggal Aktivasi:  "), activationDateLabel);

            imeiContentBox.getChildren().addAll(imeiTitleLabel, imeiField, searchImeiButton, resultGrid);

            return imeiContentBox;
        }

        private static void handleWarrantyButton() {
            VBox imeiContentBox = createImeiContent();

            Stage newStage = new Stage();
            newStage.setTitle("IMEI Search");
            newStage.setScene(new Scene(imeiContentBox, 400, 400));
            newStage.show();
        }


        private static void updateImeiContent() {
            imeiContentBox.getChildren().clear();

            Label imeiTitleLabel = new Label("Cek IMEI");
            imeiTitleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

            imeiField = new TextField();
            imeiField.setPromptText("Masukkan IMEI");

            searchImeiButton = new Button("Search");
            searchImeiButton.setOnAction(e -> handleImeiSearch());

            statusLabel = new Label("");
            productLabel = new Label("");
            imeiResultLabel = new Label("");
            activationDateLabel = new Label("");

            GridPane resultGrid = new GridPane();
            resultGrid.addRow(0, new Label("Status Garansi:"), statusLabel);
            resultGrid.addRow(1, new Label("Produk:"), productLabel);
            resultGrid.addRow(2, new Label("IMEI:"), imeiResultLabel);
            resultGrid.addRow(3, new Label("Tanggal Aktivasi:"), activationDateLabel);

            imeiContentBox.getChildren().addAll(imeiTitleLabel, imeiField, searchImeiButton, resultGrid);
        }

        private static void handleImeiSearch() {
            String imeiToSearch = imeiField.getText().trim();

            List<String> productData;
            try {
                productData = Files.readAllLines(Paths.get("PRODUKGARANSI.txt"));
            } catch (IOException e) {
                showErrorDialog("Error reading product data.");
                return;
            }

            boolean imeiFound = false;
            for (String line : productData) {
                String[] parts = line.split(",");
                String storedImei = parts[0];
                String statusGaransi = parts[1];
                String product = parts[2];
                String activationDate = parts[3];

                if (storedImei.equals(imeiToSearch)) {
                    imeiFound = true;

                    statusLabel.setText(statusGaransi);
                    productLabel.setText(product);
                    imeiResultLabel.setText(storedImei);
                    activationDateLabel.setText(activationDate);

                    break;
                }
            }

            if (!imeiFound) {
                statusLabel.setText("Status Garansi: Not Found");
                productLabel.setText("Produk: Not Found");
                imeiResultLabel.setText("IMEI: Not Found");
                activationDateLabel.setText("Tanggal Aktivasi: Not Found");
            }
        }

        private static void showErrorDialog(String s) {
        }
    }
}
