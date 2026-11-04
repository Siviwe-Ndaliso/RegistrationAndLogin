package com.mycompany.registrationandlogin;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

public class QuickChat {

    private final Scanner scanner;
    private final String sender;
    private final String storageFile;

    // Required arrays
    private String[] sentMessages = new String[100];
    private String[] disregardedMessages = new String[100];
    private String[] storedMessages = new String[100];
    private String[] messageHashes = new String[100];
    private String[] messageIDs = new String[100];

    // Supporting message information
    private String[] senders = new String[100];
    private String[] recipients = new String[100];
    private String[] messageStatuses = new String[100];

    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    private int messageCount = 0;

    private final List<JSONObject> allRecords = new ArrayList<>();
    private final List<JSONObject> storedRecords = new ArrayList<>();

    public QuickChat(Scanner scanner) {
        this(scanner, "Unknown", "messages.json");
    }

    public QuickChat(Scanner scanner, String sender) {
        this(scanner, sender, "messages.json");
    }

    // Package-private constructor used by unit tests.
    QuickChat(Scanner scanner, String sender, String storageFile) {
        this.scanner = scanner;
        this.sender = sender;
        this.storageFile = storageFile;
        loadStoredMessages();
    }

    public void start() {

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Welcome to QuickChat.");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sendMessages();
                    break;
                case "2":
                    displaySentMessages();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                case "4":
                    storedMessagesMenu();
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
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
                System.out.println("Please enter a number greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please enter a whole number.");
            return;
        }

        for (int i = 0; i < numberOfMessages; i++) {

            System.out.println();
            System.out.println("Message " + (i + 1) + " of " + numberOfMessages);

            String id = Message.generateMessageID();
            System.out.println("DEBUG - Current sender: [" + sender + "]");
            System.out.println("Message ID generated: " + id);

            String recipient;

            while (true) {
                System.out.print("Enter recipient cellphone number: ");
                recipient = scanner.nextLine();

                Message check = new Message(
                        id, i, sender, recipient, ""
                );

                String result = check.checkRecipientCell();

                if (result.equals("Cell phone number successfully captured.")) {
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
                        id, i, sender, recipient, messageText
                );

                String result = currentMessage.checkMessageLength();
                System.out.println(result);

                if (messageText.length() <= 250
                        && !messageText.trim().isEmpty()) {
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
                    addMessage(currentMessage);
                    break;

                case "2":
                    currentMessage.setMessageStatus("disregard");
                    System.out.println(currentMessage.SentMessage());
                    addMessage(currentMessage);
                    break;

                case "3":
                    currentMessage.setMessageStatus("store");
                    addMessage(currentMessage);
                    saveStoredMessages();
                    System.out.println("Message successfully stored.");
                    break;

                default:
                    System.out.println("Invalid choice. Message not processed.");
                    break;
            }
        }

        Message counter = new Message("", 0, "", "");

        System.out.println("Total messages sent: "
                + counter.returnTotalMessagess());
    }

    /*
     * Adds a Message object to the appropriate arrays.
     * This method is also used by the unit tests.
     */
    void addMessage(Message message) {

        JSONObject record = createRecord(
                message.getMessageID(),
                message.getMessageNumber(),
                message.getSender(),
                message.getRecipient(),
                message.getMessage(),
                message.getMessageHash(),
                message.getMessageStatus()
        );

        addMessageRecord(record);
    }

    private JSONObject createRecord(
            String id,
            int number,
            String messageSender,
            String recipient,
            String message,
            String hash,
            String status) {

        JSONObject json = new JSONObject();

        json.put("messageID", id);
        json.put("messageNumber", number);
        json.put("sender", messageSender);
        json.put("recipient", recipient);
        json.put("message", message);
        json.put("messageHash", hash);
        json.put("messageStatus", status);

        return json;
    }

    private void addMessageRecord(JSONObject record) {

        if (messageCount == messageIDs.length) {
            expandAllArrays();
        }

        String status = record.optString("messageStatus");

        messageIDs[messageCount] = record.optString("messageID");
        messageHashes[messageCount] = record.optString("messageHash");
        senders[messageCount] = record.optString("sender");
        recipients[messageCount] = record.optString("recipient");
        messageStatuses[messageCount] = status;

        allRecords.add(record);

        if ("send".equalsIgnoreCase(status)) {

            if (sentCount == sentMessages.length) {
                sentMessages = expandArray(sentMessages);
            }

            sentMessages[sentCount] = record.optString("message");
            sentCount++;

        } else if ("disregard".equalsIgnoreCase(status)) {

            if (disregardedCount == disregardedMessages.length) {
                disregardedMessages = expandArray(disregardedMessages);
            }

            disregardedMessages[disregardedCount] =
                    record.optString("message");

            disregardedCount++;

        } else if ("store".equalsIgnoreCase(status)) {

            if (storedCount == storedMessages.length) {
                storedMessages = expandArray(storedMessages);
            }

            storedMessages[storedCount] = record.optString("message");
            storedRecords.add(record);
            storedCount++;
        }

        messageCount++;
    }

    private void expandAllArrays() {
        messageIDs = expandArray(messageIDs);
        messageHashes = expandArray(messageHashes);
        senders = expandArray(senders);
        recipients = expandArray(recipients);
        messageStatuses = expandArray(messageStatuses);
    }

    private String[] expandArray(String[] oldArray) {

        String[] newArray = new String[oldArray.length * 2];

        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    private void displaySentMessages() {

        System.out.println();
        System.out.println("RECENTLY SENT MESSAGES");

        if (sentCount == 0) {
            System.out.println("No messages have been sent yet.");
            return;
        }

        for (int i = 0; i < sentCount; i++) {
            System.out.println((i + 1) + ". " + sentMessages[i]);
        }
    }

    private void storedMessagesMenu() {

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("STORED MESSAGES");
            System.out.println("1) Display stored messages");
            System.out.println("2) Find the longest message");
            System.out.println("3) Search by message ID");
            System.out.println("4) Search by recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display complete message report");
            System.out.println("7) Return to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayStoredMessages();
                    break;
                case "2":
                    findLongestMessage();
                    break;
                case "3":
                    searchByMessageID();
                    break;
                case "4":
                    searchByRecipient();
                    break;
                case "5":
                    deleteMessageByHash();
                    break;
                case "6":
                    displayMessageReport();
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    private void loadStoredMessages() {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(storageFile))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                JSONObject json = new JSONObject(line);

                if ("store".equalsIgnoreCase(
                        json.optString("messageStatus"))) {

                    if (!json.has("sender")) {
                        json.put("sender", "Unknown");
                    }

                    addMessageRecord(json);
                }
            }

        } catch (IOException e) {
            // The file is created when the first message is stored.
        } catch (org.json.JSONException e) {
            System.out.println("Error reading stored messages: "
                    + e.getMessage());
        }
    }

    private void displayStoredMessages() {

        System.out.println();
        System.out.println("STORED MESSAGES");

        if (storedCount == 0) {
            System.out.println("There are no stored messages.");
            return;
        }

        for (int i = 0; i < storedCount; i++) {

            JSONObject record = storedRecords.get(i);

            System.out.println();
            System.out.println("Sender: " + record.optString("sender"));
            System.out.println("Recipient: " + record.optString("recipient"));
            System.out.println("Message ID: " + record.optString("messageID"));
            System.out.println("Message Hash: " + record.optString("messageHash"));
            System.out.println("Message: " + record.optString("message"));
        }
    }

    /*
     * Returns the longest stored message.
     * Returns null when there are no stored messages.
     */
    String getLongestStoredMessage() {

        if (storedCount == 0) {
            return null;
        }

        int longestIndex = 0;

        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].length()
                    > storedMessages[longestIndex].length()) {
                longestIndex = i;
            }
        }

        return storedMessages[longestIndex];
    }

    private void findLongestMessage() {

        String longest = getLongestStoredMessage();

        if (longest == null) {
            System.out.println("There are no stored messages.");
            return;
        }

        System.out.println("Longest stored message:");
        System.out.println(longest);
        System.out.println("Length: " + longest.length() + " characters.");
    }

    /*
     * Searches all available messages, not only stored messages.
     */
    JSONObject findMessageByID(String searchID) {

        for (JSONObject record : allRecords) {
            if (record.optString("messageID").equals(searchID)) {
                return record;
            }
        }

        return null;
    }

    private void searchByMessageID() {

        System.out.print("Enter the message ID: ");
        String searchID = scanner.nextLine();

        JSONObject record = findMessageByID(searchID);

        if (record == null) {
            System.out.println("Message ID not found.");
            return;
        }

        System.out.println("Message found:");
        System.out.println("Sender: " + record.optString("sender"));
        System.out.println("Recipient: " + record.optString("recipient"));
        System.out.println("Message ID: " + record.optString("messageID"));
        System.out.println("Message Hash: " + record.optString("messageHash"));
        System.out.println("Message: " + record.optString("message"));
    }

    /*
     * Returns sent and stored messages for the specified recipient.
     * Disregarded messages are excluded.
     */
    List<JSONObject> findMessagesByRecipient(String searchRecipient) {

        List<JSONObject> results = new ArrayList<>();

        for (JSONObject record : allRecords) {

            String status = record.optString("messageStatus");

            if (record.optString("recipient").equals(searchRecipient)
                    && ("send".equalsIgnoreCase(status)
                    || "store".equalsIgnoreCase(status))) {

                results.add(record);
            }
        }

        return results;
    }

    private void searchByRecipient() {

        System.out.print("Enter recipient cellphone number: ");
        String searchRecipient = scanner.nextLine();

        List<JSONObject> results =
                findMessagesByRecipient(searchRecipient);

        if (results.isEmpty()) {
            System.out.println(
                    "No sent or stored messages found for this recipient."
            );
            return;
        }

        for (JSONObject record : results) {
            System.out.println();
            System.out.println("Sender: " + record.optString("sender"));
            System.out.println("Recipient: " + record.optString("recipient"));
            System.out.println("Message ID: " + record.optString("messageID"));
            System.out.println("Message Hash: " + record.optString("messageHash"));
            System.out.println("Message: " + record.optString("message"));
        }
    }

    /*
     * Deletes a stored message using its hash.
     * Returns true if a message was deleted.
     */
    boolean deleteStoredMessageByHash(String searchHash) {

        JSONObject recordToDelete = null;

        for (JSONObject record : storedRecords) {
            if (record.optString("messageHash")
                    .equalsIgnoreCase(searchHash)) {
                recordToDelete = record;
                break;
            }
        }

        if (recordToDelete == null) {
            return false;
        }

        storedRecords.remove(recordToDelete);
        allRecords.remove(recordToDelete);

        rebuildArrays();
        saveStoredMessages();

        return true;
    }

    private void deleteMessageByHash() {

        System.out.print("Enter the message hash: ");
        String searchHash = scanner.nextLine();

        if (deleteStoredMessageByHash(searchHash)) {
            System.out.println("Message successfully deleted.");
        } else {
            System.out.println("Message hash not found.");
        }
    }

    private void rebuildArrays() {

        List<JSONObject> remainingRecords =
                new ArrayList<>(allRecords);

        allRecords.clear();
        storedRecords.clear();

        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        messageCount = 0;

        sentMessages = new String[100];
        disregardedMessages = new String[100];
        storedMessages = new String[100];

        messageIDs = new String[100];
        messageHashes = new String[100];
        senders = new String[100];
        recipients = new String[100];
        messageStatuses = new String[100];

        for (JSONObject record : remainingRecords) {
            addMessageRecord(record);
        }
    }

    private void saveStoredMessages() {

        try (FileWriter writer = new FileWriter(storageFile, false)) {

            for (JSONObject record : storedRecords) {
                writer.write(record.toString());
                writer.write(System.lineSeparator());
            }

        } catch (IOException e) {
            System.out.println("Error updating messages: "
                    + e.getMessage());
        }
    }

    /*
     * Builds the complete report as a String.
     * This makes the report independently testable.
     */
    String generateMessageReport() {

        StringBuilder report = new StringBuilder();

        report.append("COMPLETE MESSAGE REPORT\n\n");

        report.append("SENT MESSAGES\n");

        boolean sentFound = false;

        for (JSONObject record : allRecords) {

            if ("send".equalsIgnoreCase(
                    record.optString("messageStatus"))) {

                appendMessageDetails(report, record);
                sentFound = true;
            }
        }

        if (!sentFound) {
            report.append("No sent messages.\n");
        }

        report.append("\nDISREGARDED MESSAGES\n");

        if (disregardedCount == 0) {
            report.append("No disregarded messages.\n");
        } else {
            for (int i = 0; i < disregardedCount; i++) {
                report.append(disregardedMessages[i]).append("\n");
            }
        }

        report.append("\nSTORED MESSAGES\n");

        if (storedCount == 0) {
            report.append("No stored messages.\n");
        } else {
            for (JSONObject record : storedRecords) {
                appendMessageDetails(report, record);
            }
        }

        Message counter = new Message("", 0, "", "");

        report.append("\nTotal messages sent: ")
                .append(counter.returnTotalMessagess())
                .append("\n");

        report.append("Total stored messages: ")
                .append(storedCount)
                .append("\n");

        report.append("Total disregarded messages: ")
                .append(disregardedCount)
                .append("\n");

        return report.toString();
    }

    private void appendMessageDetails(
            StringBuilder report, JSONObject record) {

        report.append("Sender: ")
                .append(record.optString("sender"))
                .append("\n");

        report.append("Recipient: ")
                .append(record.optString("recipient"))
                .append("\n");

        report.append("Message ID: ")
                .append(record.optString("messageID"))
                .append("\n");

        report.append("Message Hash: ")
                .append(record.optString("messageHash"))
                .append("\n");

        report.append("Message: ")
                .append(record.optString("message"))
                .append("\n\n");
    }

    private void displayMessageReport() {
        System.out.println(generateMessageReport());
    }

    // Copies returned to tests so they cannot change the internal arrays.
    String[] getSentMessages() {
        return java.util.Arrays.copyOf(sentMessages, sentCount);
    }

    String[] getStoredMessages() {
        return java.util.Arrays.copyOf(storedMessages, storedCount);
    }

    String[] getDisregardedMessages() {
        return java.util.Arrays.copyOf(
                disregardedMessages, disregardedCount
        );
    }

    String[] getMessageHashes() {
        return java.util.Arrays.copyOf(messageHashes, messageCount);
    }

    String[] getMessageIDs() {
        return java.util.Arrays.copyOf(messageIDs, messageCount);
    }
}
