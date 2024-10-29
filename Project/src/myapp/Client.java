package myapp;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int SERVER_PORT = 8080;
	private Stage primary;
	
	public void start(Stage primary) {
		this.primary = primary;
		showLoginPage(); // Start with Login Page for now.
	}
	
	private void showLoginPage() {
		// Textbox for Username.
		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		
		// Textbox for Password.
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		
		// Login Button.
		Button loginButton = new Button("Login");
		Label statusLabel = new Label();
		
		// Register Button.'
		Button registerButton = new Button("Register");
		
		// Login Button Event Listener.
		loginButton.setOnAction(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			boolean isAuthenticated = sendLoginRequest(username, password);
			
			if (isAuthenticated) {
				statusLabel.setText("Login Successful.");
				// Go to Home Page...
			} else {
				statusLabel.setText("Invalid Username or Password");
			}
		});
		
		// Register Button Event Listener.
		registerButton.setOnAction(e -> {
			showRegisterPage();
		});
		
		// Screen for Login Page.
		VBox layout = new VBox(10, usernameField, passwordField, loginButton, statusLabel, registerButton);
		Scene loginScene = new Scene(layout, 300, 200);
		
		// Show Login Page.
		primary.setScene(loginScene);
		primary.setTitle("Login Page");
		primary.show();
	}
	
	private void showRegisterPage() {
		// Textbox for First Name.
		TextField firstNameText = new TextField();
		firstNameText.setPromptText("First Name");
		
		// Textbox for Last Name.
		TextField lastNameText = new TextField();
		lastNameText.setPromptText("Last Name");
		
		// Textbox for Email.
		TextField emailText = new TextField();
		emailText.setPromptText("Email");
		
		// Textbox for Password.
		TextField passwordText = new TextField();
		passwordText.setPromptText("Password");
		
		// Textbox for Confirm Password.
		TextField confirmPasswordText = new TextField();
		confirmPasswordText.setPromptText("Confirm Password");
		
		// Button for Create Account.
		Button createAccountButton = new Button("Create Account");
		Label statusLabel = new Label();
		
		// Create Account Event Listener.
		createAccountButton.setOnAction(e -> {
			String firstname = firstNameText.getText();
			String lastname = lastNameText.getText();
			String email = emailText.getText();
			String password = passwordText.getText();
			String confirmPassword = confirmPasswordText.getText();
			
			if (!password.equals(confirmPassword)) {
				statusLabel.setText("Passwords Don't Match");
			} else {
				String response = sendRegisterRequest(firstname, lastname, email, password);
				statusLabel.setText(response);
			}
		});
		
		// Screen for Creating a new Account.
		VBox layout = new VBox(10, firstNameText, lastNameText, emailText, passwordText, confirmPasswordText, createAccountButton, statusLabel);
		Scene registerScene = new Scene(layout, 300, 200);
		
		// Show Register Page.
		primary.setScene(registerScene);
		primary.setTitle("Create New Account");
		primary.show();
	}
	
	private boolean sendLoginRequest(String username, String password) {
		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			
			// Send Authentication Request to the Server.
			out.println("AUTHENTICATE " + username + " " + password);
			// Receive Response from the Client.
			String response = in.readLine();
			return "Authentication Successful".equals(response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String sendRegisterRequest(String firstname, String lastname, String email, String password) {
		try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			
			// Send Registration Request to the Server.
			out.println("REGISTER " + firstname + " " + lastname + " " + email + " " + password);
			// Receive Response from the Client.s
			String response = in.readLine();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return "Invalid Credentials";
		}
	}
	
	public static void main(String[] args) {
		launch(args); // runs app.
	}
}