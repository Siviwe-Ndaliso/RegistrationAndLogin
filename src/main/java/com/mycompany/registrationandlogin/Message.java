
package com.mycompany.registrationandlogin;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    private static int totalMessagesSent = 0;

    // Original constructor retained for existing tests.
    public Message(String messageID, int messageNumber,
                   String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = "Unknown";
        this.recipient = recipient;
        this.message = message;
    }

    // Constructor including sender information.
    public Message(String messageID, int messageNumber,
                   String sender, String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("[0-9]{10}");
    }

    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27[0-9]{9}$")) {

            return "Cell phone number successfully captured.";

        } else {

            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {

        if (message == null || message.trim().isEmpty()
                || messageID == null || messageID.length() < 2) {

            return "";
        }

        String[] words = message.trim().split("\\s+");

        String firstWord = words[0]
                .replaceAll("[^a-zA-Z0-9]", "");

        String lastWord = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        messageHash = (messageID.substring(0, 2) + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    public String checkMessageLength() {

        if (message == null) {
            return "Message cannot be empty.";
        }

        if (message.length() <= 250) {

            return "Message ready to send.";

        } else {

            int excessCharacters = message.length() - 250;

            return "Message exceeds 250 characters by "
                    + excessCharacters
                    + "; please reduce the size.";
        }
    }

    public static String generateMessageID() {

        long randomNumber = java.util.concurrent.ThreadLocalRandom
                .current()
                .nextLong(10000000000L);

        return String.format("%010d", randomNumber);
    }

    public String SentMessage() {

        if ("send".equalsIgnoreCase(messageStatus)) {

            return "Message successfully sent.";

        } else if ("disregard".equalsIgnoreCase(messageStatus)) {

            return "Press 0 to delete the message.";

        } else if ("store".equalsIgnoreCase(messageStatus)) {

            return storeMessage();

        } else {

            return "No valid message action selected.";
        }
    }

    public void setMessageStatus(String messageStatus) {

        if (this.messageStatus == null
                && "send".equalsIgnoreCase(messageStatus)) {

            totalMessagesSent++;

        } else if ("send".equalsIgnoreCase(this.messageStatus)
                && !"send".equalsIgnoreCase(messageStatus)) {

            totalMessagesSent--;

        } else if (!"send".equalsIgnoreCase(this.messageStatus)
                && "send".equalsIgnoreCase(messageStatus)) {

            totalMessagesSent++;
        }

        this.messageStatus = messageStatus;
    }

    public String printMessages() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + getMessageHash()
                + "\nSender: " + sender
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    // Converts a message into a JSON object.
    public JSONObject toJSON() {

        if (messageHash == null || messageHash.isEmpty()) {
            createMessageHash();
        }

        JSONObject json = new JSONObject();

        json.put("messageID", messageID);
        json.put("messageNumber", messageNumber);
        json.put("sender", sender);
        json.put("recipient", recipient);
        json.put("message", message);
        json.put("messageHash", messageHash);
        json.put("messageStatus", messageStatus);

        return json;
    }

    // Creates a Message object from a JSON object.
    public static Message fromJSON(JSONObject json) {

        String messageID = json.optString("messageID", "");
        int messageNumber = json.optInt("messageNumber", 0);
        String sender = json.optString("sender", "Unknown");
        String recipient = json.optString("recipient", "");
        String messageText = json.optString("message", "");

        Message storedMessage = new Message(
                messageID,
                messageNumber,
                sender,
                recipient,
                messageText
        );

        storedMessage.messageHash =
                json.optString("messageHash", "");

        storedMessage.messageStatus =
                json.optString("messageStatus", "store");

        return storedMessage;
    }

    // Stores the message in messages.json.
    public String storeMessage() {

        try (FileWriter writer = new FileWriter("messages.json", true)) {

            writer.write(toJSON().toString());
            writer.write(System.lineSeparator());

            return "Message successfully stored.";

        } catch (IOException e) {

            return "Error storing message: " + e.getMessage();
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        if (messageHash == null || messageHash.isEmpty()) {
            return createMessageHash();
        }
        return messageHash;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }
}
