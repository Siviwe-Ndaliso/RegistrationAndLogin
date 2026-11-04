/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationandlogin;

/**
 *
 * @author Student
 */
import java.nio.file.Path;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

public class QuickChatTest {

    @TempDir
    Path tempDir;

    private QuickChat createQuickChat() {
        String testFile = tempDir.resolve("test-messages.json").toString();

        return new QuickChat(
                new java.util.Scanner(""),
                "Test Sender",
                testFile
        );
    }

    private Message createMessage(
            String id,
            int number,
            String recipient,
            String text,
            String status) {

        Message message = new Message(
                id,
                number,
                "Test Sender",
                recipient,
                text
        );

        message.setMessageStatus(status);

        return message;
    }

    @Test
    public void testSentMessagesArrayIsPopulatedCorrectly() {

        QuickChat chat = createQuickChat();

        Message message1 = createMessage(
                "0000000001",
                0,
                "+27834557896",
                "Did you get the cake?",
                "send"
        );

        Message message4 = createMessage(
                "0838884567",
                3,
                "+27834557896",
                "It is dinner time!",
                "send"
        );

        chat.addMessage(message1);
        chat.addMessage(message4);

        String[] sentMessages = chat.getSentMessages();

        assertEquals(2, sentMessages.length);
        assertEquals("Did you get the cake?", sentMessages[0]);
        assertEquals("It is dinner time!", sentMessages[1]);
    }

    @Test
    public void testLongestStoredMessage() {

        QuickChat chat = createQuickChat();

        Message message2 = createMessage(
                "0000000002",
                1,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "store"
        );

        Message message5 = createMessage(
                "0000000005",
                4,
                "+27838884567",
                "Ok, I am leaving without you.",
                "store"
        );

        chat.addMessage(message2);
        chat.addMessage(message5);

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                chat.getLongestStoredMessage()
        );
    }

    @Test
    public void testSearchMessageByID() {

        QuickChat chat = createQuickChat();

        Message message4 = createMessage(
                "0838884567",
                3,
                "+27834557896",
                "It is dinner time!",
                "send"
        );

        chat.addMessage(message4);

        JSONObject result = chat.findMessageByID("0838884567");

        assertNotNull(result);
        assertEquals("It is dinner time!", result.optString("message"));
        assertEquals(
                "+27834557896",
                result.optString("recipient")
        );
    }

    @Test
    public void testSearchMessagesByRecipient() {

        QuickChat chat = createQuickChat();

        Message message2 = createMessage(
                "0000000002",
                1,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "store"
        );

        Message message5 = createMessage(
                "0000000005",
                4,
                "+27838884567",
                "Ok, I am leaving without you.",
                "store"
        );

        chat.addMessage(message2);
        chat.addMessage(message5);

        List<JSONObject> results =
                chat.findMessagesByRecipient("+27838884567");

        assertEquals(2, results.size());

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                results.get(0).optString("message")
        );

        assertEquals(
                "Ok, I am leaving without you.",
                results.get(1).optString("message")
        );
    }

    @Test
    public void testDeleteStoredMessageByHash() {

        QuickChat chat = createQuickChat();

        Message message2 = createMessage(
                "0000000002",
                1,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "store"
        );

        chat.addMessage(message2);

        String hash = message2.getMessageHash();

        boolean deleted = chat.deleteStoredMessageByHash(hash);

        assertTrue(deleted);
        assertEquals(0, chat.getStoredMessages().length);
    }

    @Test
    public void testDisplayReportContainsSentMessageDetails() {

        QuickChat chat = createQuickChat();

        Message message1 = createMessage(
                "0000000001",
                0,
                "+27834557896",
                "Did you get the cake?",
                "send"
        );

        Message message4 = createMessage(
                "0838884567",
                3,
                "+27834557896",
                "It is dinner time!",
                "send"
        );

        chat.addMessage(message1);
        chat.addMessage(message4);

        String report = chat.generateMessageReport();

        assertTrue(report.contains("SENT MESSAGES"));
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }
}