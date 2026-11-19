
package com.mycompany.prog5121_part1_maven;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageStorageTest {

    @Test
    public void testSaveAndReadMessages() throws IOException {
        Message message = new Message();

        message.setMessageID("1234567890");
        message.setMessageNumber(1);
        message.setRecipient("+27834557896");
        message.setMessageText("Did you get the cake?");
        message.createMessageHash();
        message.setMessageStatus("Stored");

        Message[] messages = {message};

        MessageStorage storage = new MessageStorage();
      java.nio.file.Path originalFile = java.nio.file.Paths.get("messages.json");
String originalJson = java.nio.file.Files.exists(originalFile)
        ? java.nio.file.Files.readString(originalFile) : null;
storage.saveMessages(messages);

        String json = storage.readStoredMessages();

        assertTrue(json.contains("1234567890"));
        assertTrue(json.contains("+27834557896"));
        assertTrue(json.contains("Did you get the cake?"));
        assertTrue(json.contains("Stored"));
        if (originalJson != null) {
    java.nio.file.Files.writeString(originalFile, originalJson);
} else {
    java.nio.file.Files.deleteIfExists(originalFile);
}
    }
    
    @Test
    public void testLoadMessageRecords() throws IOException {
        MessageStorage storage = new MessageStorage();

        String[] records = storage.loadMessageRecords();

        assertTrue(records.length > 0);
        assertTrue(records[0].contains("1234567890"));
        assertTrue(records[0].contains("Did you get the cake?"));
    }

    @Test
    public void testLoadMessages() throws IOException {
        MessageStorage storage = new MessageStorage();

        Message[] messages = storage.loadMessages();

        assertTrue(messages.length > 0);
        assertEquals("1234567890", messages[0].getMessageID());
        assertEquals("+27834557896", messages[0].getRecipient());
        assertEquals("Did you get the cake?",
                messages[0].getMessageText());
        assertEquals("Stored", messages[0].getMessageStatus());
    }

}
