
package com.mycompany.prog5121_part1_maven;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageManagerTest {

    private Message createMessage(
            String id, int number, String recipient, String text) {

        Message message = new Message();
        message.setMessageID(id);
        message.setMessageNumber(number);
        message.setRecipient(recipient);
        message.setMessageText(text);
        message.createMessageHash();

        return message;
    }

    @Test
    public void testAddStoredMessage() {
        MessageManager manager = new MessageManager();

        Message message = createMessage(
                "1234567890", 1, "+27838884567", "Where are you?");

        assertTrue(manager.addMessage(message, "Stored"));
        assertEquals(1, manager.getStoredCount());
        assertEquals("Stored", message.getMessageStatus());
    }

    @Test
    public void testAddSentAndDisregardedMessages() {
        MessageManager manager = new MessageManager();

        Message sent = createMessage(
                "1111111111", 1, "+27834557896", "Hello");
        Message disregarded = createMessage(
                "2222222222", 2, "+27834484567", "Not sending");

        assertTrue(manager.addMessage(sent, "Sent"));
        assertTrue(manager.addMessage(disregarded, "Disregard"));

        assertEquals(1, manager.getSentCount());
        assertEquals(1, manager.getDisregardedCount());
    }

    @Test
    public void testFindLongestStoredMessage() {
        MessageManager manager = new MessageManager();

        manager.addMessage(createMessage(
                "1111111111", 1, "+27838884567", "Short"), "Stored");

        String longestText =
                "Where are you? You are late! I have asked you to be on time.";

        manager.addMessage(createMessage(
                "2222222222", 2, "+27838884567", longestText), "Stored");

        assertEquals(longestText, manager.findLongestStoredMessage());
    }

    @Test
    public void testSearchStoredMessageByID() {
        MessageManager manager = new MessageManager();

        manager.addMessage(createMessage(
                "1234567890", 1, "+27838884567", "Where are you?"),
                "Stored");

        String result = manager.searchStoredMessageByID("1234567890");

        assertTrue(result.contains("+27838884567"));
        assertTrue(result.contains("Where are you?"));
    }

    @Test
    public void testSearchStoredMessagesByRecipient() {
        MessageManager manager = new MessageManager();

        manager.addMessage(createMessage(
                "1111111111", 1, "+27838884567", "First message"),
                "Stored");
        manager.addMessage(createMessage(
                "2222222222", 2, "+27838884567", "Second message"),
                "Stored");

        String result =
                manager.searchStoredMessagesByRecipient("+27838884567");

        assertTrue(result.contains("First message"));
        assertTrue(result.contains("Second message"));
    }

    @Test
    public void testDeleteStoredMessageByHash() {
        MessageManager manager = new MessageManager();

        Message message = createMessage(
                "1234567890", 1, "+27838884567", "Delete this message");

        manager.addMessage(message, "Stored");

        assertTrue(manager.deleteStoredMessageByHash(
                message.getMessageHash()));

        assertEquals(0, manager.getStoredCount());
    }

    @Test
    public void testStoredMessageReport() {
        MessageManager manager = new MessageManager();

        Message message = createMessage(
                "1234567890", 1, "+27838884567", "Report this message");

        manager.addMessage(message, "Stored");

        String report = manager.displayStoredMessageReport();

        assertTrue(report.contains("1234567890"));
        assertTrue(report.contains(message.getMessageHash()));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains("Report this message"));
        assertTrue(report.contains("Stored"));
    }
    
    @Test
public void testLoadStoredMessagesFromJSON() throws java.io.IOException {
    java.nio.file.Path file = java.nio.file.Paths.get("messages.json");

    String originalJson = java.nio.file.Files.exists(file)
            ? java.nio.file.Files.readString(file) : null;

    try {
        Message first = new Message();
        first.setMessageID("1234567890");
        first.setMessageNumber(1);
        first.setRecipient("+27834557896");
        first.setMessageText("Did you get the cake?");
        first.setMessageStatus("Stored");

        Message second = new Message();
        second.setMessageID("9876543210");
        second.setMessageNumber(2);
        second.setRecipient("+27838884567");
        second.setMessageText("I am on my way");
        second.setMessageStatus("Stored");

        MessageStorage storage = new MessageStorage();
        storage.saveMessages(new Message[]{first, second});

        MessageManager manager = new MessageManager();
        manager.loadStoredMessagesFromJSON();

        assertEquals(2, manager.getStoredCount());
        assertTrue(manager.displayStoredMessages()
                .contains("Did you get the cake?"));
    } finally {
        if (originalJson != null) {
            java.nio.file.Files.writeString(file, originalJson);
        } else {
            java.nio.file.Files.deleteIfExists(file);
        }
    }
}

    
}
