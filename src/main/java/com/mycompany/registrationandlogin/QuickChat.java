/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationandlogin;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class QuickChat {
    
    private final Scanner scanner;

    public QuickChat(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("Welcome to QuickChat.");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    sendMessages();
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

           default:
                    System.out.println(
                            "Invalid option. Please choose 1, 2, or 3.");
                    break;
            }
        }
    }

    private void sendMessages() {

        System.out.print("How many messages would you like to send? ");

        int numberOfMessages;

        try {
            numberOfMessages = Integer.parseInt(scanner.nextLine());

            if (numberOfMessages <= 0) {
                System.out.println(
                        "Please enter a number greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid number. Please enter a whole number.");
            return;
        }

        for (int i = 0; i < numberOfMessages; i++) {

            System.out.println();
            System.out.println("Message " + (i + 1)
                    + " of " + numberOfMessages);

            String messageID = Message.generateMessageID();

            System.out.println("Message ID generated: " + messageID);

            String recipient;

            while (true) {

                System.out.print("Enter recipient cellphone number: ");
                recipient = scanner.nextLine();

                Message recipientCheck =
                        new Message(messageID, i, recipient, "");

                String result = recipientCheck.checkRecipientCell();

                if (result.equals(
                        "Cell phone number successfully captured.")) {
                    System.out.println(result);
                    break;
                } else {
                    System.out.println(result);
                }
            }

            String messageText;
            Message currentMessage;

            while (true) {

                System.out.print("Enter your message: ");
                messageText = scanner.nextLine();

                currentMessage = new Message(
                        messageID, i, recipient, messageText);

                String lengthResult =
                        currentMessage.checkMessageLength();

                System.out.println(lengthResult);

                if (messageText.length() <= 250) {
                    break;
                }
            }

            String hash = currentMessage.createMessageHash();

            System.out.println("Message Hash: " + hash);
            System.out.println();
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message");
            System.out.print("Choose an action: ");

            String action = scanner.nextLine();

            switch (action) {

                case "1":
                    currentMessage.setMessageStatus("send");
                    System.out.println(currentMessage.SentMessage());
                    System.out.println(currentMessage.printMessages());
                    break;

                case "2":
                    currentMessage.setMessageStatus("disregard");
                    System.out.println(currentMessage.SentMessage());
                    break;

                case "3":
                    currentMessage.setMessageStatus("store");
                    System.out.println(currentMessage.SentMessage());
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Message not sent.");
                    break;
            }
        }

        Message counter = new Message("", 0, "", "");

        System.out.println("Total messages sent: "
                + counter.returnTotalMessagess());
    }
}         
