/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



/**
 *
 * @author Leseg
 *//*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_part1_maven;

/**
 *
 * @author Leseg
 */
public class Login {
    // Login class handles user registration, validation, and authentication.
    private String UserName;
    private String Password;
    private String cellphone;
    private String firstName;
    private String lastname;
    private String loginUsername;
    private String loginPassword;
    private String loginStatus;
    
    
    public boolean checkUserName(){
        return UserName.contains("_") && UserName.length() <= 5;
        
    }
    
    public void setusername(String username) {
        this.UserName = username;
     }
    
 public boolean checkPasswordComplexity() {
    return Password.length() >= 8
            && Password.matches(".*[A-Z].*")
            && Password.matches(".*[0-9].*")
            && Password.matches(".*[^a-zA-Z0-9].*");
}
 public void setPassword(String password) {
    this.Password = password;
}
     public void setcellphone (String cellphone) {
    this.cellphone = cellphone;
}
     public boolean checkCellPhoneNumber() {
         // Regex pattern adapted from Oracle Java documentation on regular expressions.
// https://docs.oracle.com/javase/tutorial/essential/regex/
    return cellphone.matches("^\\+27[0-9]{9}$");
}
   public void setfirstName(String firstName) {
    this.firstName = firstName;
} 
   public void setlastname(String lastname) {
    this.lastname = lastname;
   }
   
  public String registerUser() {
    if (!checkUserName()) {
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    if (!checkPasswordComplexity()) {
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    if (!checkCellPhoneNumber()) {
        return "Cell phone number incorrectly formatted or does not contain international code.";
    }

    return "Registration successful.";
}

public String registerUser(String field) {
    if (field.equals("username")) {
        if (checkUserName()) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    if (field.equals("password")) {
        if (checkPasswordComplexity()) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    if (field.equals("cellphone")) {
        if (checkCellPhoneNumber()) {
            return "Cell phone number successfully added.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
    }

    return "Invalid registration field.";
}
  
  
  
  
  public void setLoginUserName(String loginUsername) {
    this.loginUsername = loginUsername;
}
  
  public void setLoginPassword(String loginPassword) {
    this.loginPassword = loginPassword;
}
  
   public String returnLoginStatus() {
       return loginStatus;

     }
   
     public boolean loginUser() {
    if (UserName.equals(loginUsername) && Password.equals(loginPassword)) {
        loginStatus = "Welcome " + firstName + ", " + lastname + " it is great to see you again.";
        return true;
    } else {
        loginStatus = "Username or password incorrect, please try again.";
        return false;
    }
}
  
  
public static void main(String[] args) {
    java.util.Scanner scanner = new java.util.Scanner(System.in);
    Login login = new Login();

    System.out.println("================================");
    System.out.println("          QUICKCHAT");
    System.out.println("================================");
    System.out.print("Enter username: ");
    login.setusername(scanner.nextLine());

    System.out.print("Enter password: ");
    login.setPassword(scanner.nextLine());

    System.out.print("Enter cellphone (+27XXXXXXXXX): ");
    login.setcellphone(scanner.nextLine());

    System.out.print("Enter first name: ");
    login.setfirstName(scanner.nextLine());

    System.out.print("Enter last name: ");
    login.setlastname(scanner.nextLine());

    System.out.println("\n" + login.registerUser());

    if (!login.registerUser().equals("Registration successful.")) {
        System.out.println("Please check your details and run the program again.");
        scanner.close();
        return;
    }

    System.out.println("\nPlease log in.");
    System.out.print("Username: ");
    login.setLoginUserName(scanner.nextLine());

    System.out.print("Password: ");
    login.setLoginPassword(scanner.nextLine());

    if (!login.loginUser()) {
        System.out.println(login.returnLoginStatus());
        scanner.close();
        return;
    }

    System.out.println(login.returnLoginStatus());

    MessageManager manager = new MessageManager();
    MessageStorage storage = new MessageStorage();

    try {
        manager.loadStoredMessagesFromJSON();
    } catch (java.io.IOException e) {
        System.out.println("Could not load stored messages: " + e.getMessage());
    }

    
boolean running = true;

while (running) {
    System.out.println("\n========== QUICKCHAT MENU ==========");
    System.out.println("1. Send a message");
    System.out.println("2. View sent messages");
    System.out.println("3. Quit");
    System.out.println("4. Stored Messages");
    System.out.print("Choose an option: ");

    String choice = scanner.nextLine();

    switch (choice) {
        
case "1":
    Message message = new Message();

    System.out.print("Enter recipient cellphone (+27XXXXXXXXX): ");
    String recipient = scanner.nextLine();
    message.setRecipient(recipient);

    if (!message.checkRecipientCell()) {
        System.out.println("Invalid cellphone number. Use +27 followed by 9 digits.");
        break;
    }

    System.out.print("Enter your message (maximum 250 characters): ");
    String messageText = scanner.nextLine();
    message.setMessageText(messageText);

    if (!message.checkMessageText()) {
        System.out.println("Message is too long. Maximum length is 250 characters.");
        break;
    }

    int messageNumber = Message.returnTotalMessagess() + 1;
    message.setMessageNumber(messageNumber);
   String generatedID = message.generateMessageID();
    message.setMessageID(generatedID);
    message.createMessageHash();

    manager.addMessage(message, "Sent");
   message.incrementTotalMessages();

    System.out.println("Message successfully sent.");
    System.out.println("Message ID: " + message.getMessageID());
    System.out.println("Message Hash: " + message.getMessageHash());
    System.out.println("Recipient: " + message.getRecipient());
    System.out.println("Message: " + message.getMessageText());
    break;


        case "2":
            System.out.println("Sent messages: " + manager.getSentCount());
            break;

        case "3":
            running = false;
            System.out.println("Thank you for using QuickChat.");
            break;

        
case "4":
    boolean storedMenuRunning = true;

    while (storedMenuRunning) {
        System.out.println("\n====== STORED MESSAGES ======");
        System.out.println("1. Display sender and recipient");
        System.out.println("2. Display longest stored message");
        System.out.println("3. Search message by ID");
        System.out.println("4. Search messages by recipient");
        System.out.println("5. Delete message by hash");
        System.out.println("6. Display full message report");
        System.out.println("7. Return to main menu");
        System.out.print("Choose an option: ");

        String storedChoice = scanner.nextLine();

        switch (storedChoice) {
            case "1":
                System.out.println(manager.displayStoredMessages());
                break;

            case "2":
                System.out.println(manager.findLongestStoredMessage());
                break;

            case "3":
                System.out.print("Enter message ID: ");
                String searchID = scanner.nextLine();
                System.out.println(
                        manager.searchStoredMessageByID(searchID));
                break;

            case "4":
                System.out.print("Enter recipient cellphone: ");
                String searchRecipient = scanner.nextLine();
                System.out.println(
                        manager.searchStoredMessagesByRecipient(
                                searchRecipient));
                break;

            case "5":
                System.out.print("Enter message hash: ");
                String deleteHash = scanner.nextLine();
                System.out.println(
                        manager.deleteStoredMessageByHash(deleteHash));
                break;

            case "6":
                System.out.println(manager.displayStoredMessageReport());
                break;

            case "7":
                storedMenuRunning = false;
                break;

            default:
                System.out.println(
                        "Invalid option. Choose a number from 1 to 7.");
        }
    }
    break;

    }
    
}
}
}




    