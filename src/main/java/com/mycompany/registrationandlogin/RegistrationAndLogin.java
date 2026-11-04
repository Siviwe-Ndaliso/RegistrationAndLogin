/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationandlogin;

/**
 *
 * @author Student
 */

  import java.util.Scanner;

public class RegistrationAndLogin {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("CHAT APP REGISTRATION");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cellphone number: ");
        String cellPhoneNumber = scanner.nextLine();

        Login user = new Login(firstName, lastName, username, password, cellPhoneNumber);

        System.out.println();

        if (user.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        if (user.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }

        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        System.out.println(user.registerUser());

        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("LOGIN");

            System.out.print("Enter your username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String enteredPassword = scanner.nextLine();
            
            
          if (user.loginUser(enteredUsername, enteredPassword)) {
    System.out.println(user.returnLoginStatus(enteredUsername, enteredPassword));

    QuickChat quickChat = new QuickChat(scanner, user.getFullName());
    quickChat.start();

} else {
    System.out.println(user.returnLoginStatus(enteredUsername, enteredPassword));
}
           

        scanner.close();
    }
}    
}
