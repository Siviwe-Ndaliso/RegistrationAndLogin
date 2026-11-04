package com.mycompany.registrationandlogin;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.mycompany.registrationandlogin.Message;


public class MessageTest {

    @Test
    public void testCheckMessageID() {
        Message message = new Message(
                "1234567890", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");

        assertTrue(message.checkMessageID());
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message message = new Message(
                "1234567890", 0, "+27718693002", "Hello");

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message message = new Message(
                "1234567890", 0, "08575975889", "Hello");

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message(
                "0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");

        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageLengthValid() {
        Message message = new Message(
                "1234567890", 0, "+27718693002", "Hello");

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength());
    }

    @Test
    public void testMessageLengthTooLong() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                "1234567890", 0, "+27718693002", longMessage);

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength());
    }

    @Test
    public void testGenerateMessageID() {
        String messageID = Message.generateMessageID();

        assertTrue(messageID.matches("[0-9]{10}"));
    }

    @Test
    public void testSendMessageStatus() {
        Message message = new Message(
                "1234567890", 0, "+27718693002", "Hello");

        message.setMessageStatus("send");

        assertEquals(
                "Message successfully sent.",
                message.SentMessage());
    }

    @Test
    public void testDisregardMessageStatus() {
        Message message = new Message(
                "1234567890", 0, "+27718693002", "Hello");

        message.setMessageStatus("disregard");

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage());
    }
}
