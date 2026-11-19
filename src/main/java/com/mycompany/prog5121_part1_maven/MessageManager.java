
package com.mycompany.prog5121_part1_maven;

public class MessageManager {

    private Message[] sentMessages;
    private Message[] disregardedMessages;
    private Message[] storedMessages;

    private String[] messageHashes;
    private String[] messageIDs;

    public MessageManager() {
        sentMessages = new Message[100];
        disregardedMessages = new Message[100];
        storedMessages = new Message[100];

        messageHashes = new String[100];
        messageIDs = new String[100];
    }
    
private int sentCount = 0;
private int disregardedCount = 0;
private int storedCount = 0;


public boolean addMessage(Message message, String status) {
    if (message == null || status == null) {
        return false;
    }

    if (message.getMessageID() == null
            || message.getMessageID().isEmpty()) {
        return false;
    }

    String hash = message.getMessageHash();

    if (hash == null || hash.isEmpty()) {
        hash = message.createMessageHash();
    }

    if (hash == null || hash.isEmpty()) {
        return false;
    }

    if (status.equalsIgnoreCase("Sent")) {
        if (sentCount >= sentMessages.length) {
            return false;
        }
        sentMessages[sentCount++] = message;

    } else if (status.equalsIgnoreCase("Disregard")
            || status.equalsIgnoreCase("Disregarded")) {
        if (disregardedCount >= disregardedMessages.length) {
            return false;
        }
        disregardedMessages[disregardedCount++] = message;

    } else if (status.equalsIgnoreCase("Stored")) {
        if (storedCount >= storedMessages.length) {
            return false;
        }
        storedMessages[storedCount++] = message;

    } else {
        return false;
    }

    message.setMessageStatus(status);

    for (int i = 0; i < messageIDs.length; i++) {
        if (messageIDs[i] == null) {
            messageIDs[i] = message.getMessageID();
            messageHashes[i] = hash;
            break;
        }
    }

    return true;
}


public int getSentCount() {
    return sentCount;
}

public int getDisregardedCount() {
    return disregardedCount;
}

public int getStoredCount() {
    return storedCount;
}

public String displayStoredMessages() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < storedCount; i++) {
        Message message = storedMessages[i];

        result.append("Sender: QuickChat\n");
        result.append("Recipient: ")
              .append(message.getRecipient()).append("\n");
        result.append("Message: ")
              .append(message.getMessageText()).append("\n\n");
    }

    return result.toString();
}

public String findLongestStoredMessage() {
    if (storedCount == 0) {
        return "No stored messages found.";
    }

    Message longest = storedMessages[0];

    for (int i = 1; i < storedCount; i++) {
        if (storedMessages[i].getMessageText().length()
                > longest.getMessageText().length()) {
            longest = storedMessages[i];
        }
    }

    return longest.getMessageText();
}

public String searchStoredMessageByID(String messageID) {
    if (messageID == null) {
        return "Message ID not found.";
    }

    for (int i = 0; i < storedCount; i++) {
        Message message = storedMessages[i];

        if (messageID.equals(message.getMessageID())) {
            return "Recipient: " + message.getRecipient()
                    + "\nMessage: " + message.getMessageText();
        }
    }

    return "Message ID not found.";
}

public String searchStoredMessagesByRecipient(String recipient) {
    if (recipient == null) {
        return "No messages found for this recipient.";
    }

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < storedCount; i++) {
        Message message = storedMessages[i];

        if (recipient.equals(message.getRecipient())) {
            result.append("Message ID: ")
                  .append(message.getMessageID()).append("\n");
            result.append("Message: ")
                  .append(message.getMessageText()).append("\n\n");
        }
    }

    if (result.length() == 0) {
        return "No messages found for this recipient.";
    }

    return result.toString();
}


public boolean deleteStoredMessageByHash(String messageHash) {
    if (messageHash == null) {
        return false;
    }

    for (int i = 0; i < storedCount; i++) {
        Message message = storedMessages[i];

        String existingHash = message.getMessageHash();

        if (existingHash == null || existingHash.isEmpty()) {
            existingHash = message.createMessageHash();
        }

        if (messageHash.equalsIgnoreCase(existingHash)) {
            String deletedID = message.getMessageID();

            for (int j = i; j < storedCount - 1; j++) {
                storedMessages[j] = storedMessages[j + 1];
            }

            storedMessages[--storedCount] = null;

            for (int j = 0; j < messageIDs.length; j++) {
                if (deletedID != null
                        && deletedID.equals(messageIDs[j])) {
                    for (int k = j; k < messageIDs.length - 1; k++) {
                        messageIDs[k] = messageIDs[k + 1];
                        messageHashes[k] = messageHashes[k + 1];
                    }

                    messageIDs[messageIDs.length - 1] = null;
                    messageHashes[messageHashes.length - 1] = null;
                    break;
                }
            }

            return true;
        }
    }

    return false;
}


public String displayStoredMessageReport() {
    if (storedCount == 0) {
        return "No stored messages found.";
    }

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < storedCount; i++) {
        Message message = storedMessages[i];

        String hash = message.getMessageHash();

        if (hash == null || hash.isEmpty()) {
            hash = message.createMessageHash();
        }

        result.append("Message ID: ")
              .append(message.getMessageID()).append("\n");
        result.append("Message Hash: ")
              .append(hash).append("\n");
        result.append("Recipient: ")
              .append(message.getRecipient()).append("\n");
        result.append("Message: ")
              .append(message.getMessageText()).append("\n");
        result.append("Status: ")
              .append(message.getMessageStatus()).append("\n");
        result.append("------------------------------\n");
    }

    return result.toString();
}

public void loadStoredMessagesFromJSON() throws java.io.IOException {
    MessageStorage storage = new MessageStorage();
    Message[] loadedMessages = storage.loadMessages();

    for (Message message : loadedMessages) {
        if (message.getMessageStatus() != null
                && message.getMessageStatus().equalsIgnoreCase("Stored")) {
            addMessage(message, "Stored");
        }
    }
}

}
