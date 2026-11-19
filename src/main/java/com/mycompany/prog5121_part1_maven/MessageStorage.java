
package com.mycompany.prog5121_part1_maven;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MessageStorage {

    private static final Path FILE_PATH =
            Paths.get("messages.json");

    public void saveMessages(Message[] messages) throws IOException {
        List<String> jsonMessages = new ArrayList<>();

        for (Message message : messages) {
            if (message == null) {
                continue;
            }

            String json = "{"
                    + "\"messageID\":\"" + escape(message.getMessageID()) + "\","
                    + "\"messageNumber\":" + message.getMessageNumber() + ","
                    + "\"recipient\":\"" + escape(message.getRecipient()) + "\","
                    + "\"messageText\":\"" + escape(message.getMessageText()) + "\","
                    + "\"messageHash\":\"" + escape(message.getMessageHash()) + "\","
                    + "\"messageStatus\":\"" + escape(message.getMessageStatus()) + "\""
                    + "}";

            jsonMessages.add(json);
        }

        String content = "[\n" + String.join(",\n", jsonMessages) + "\n]";
        Files.writeString(FILE_PATH, content, StandardCharsets.UTF_8);
    }

    public String readStoredMessages() throws IOException {
        if (!Files.exists(FILE_PATH)) {
            return "[]";
        }

        return Files.readString(FILE_PATH, StandardCharsets.UTF_8);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
public String[] loadMessageRecords() throws IOException {
    String json = readStoredMessages().trim();

    if (json.length() < 2 || json.equals("[]")) {
        return new String[0];
    }

    if (!json.startsWith("[") || !json.endsWith("]")) {
        throw new IOException("Invalid messages JSON file.");
    }

    String content = json.substring(1, json.length() - 1).trim();

    if (content.isEmpty()) {
        return new String[0];
    }

    return content.split("\\s*,\\s*(?=\\{)");
}


public Message[] loadMessages() throws IOException {
    String[] records = loadMessageRecords();
    Message[] messages = new Message[records.length];

    for (int i = 0; i < records.length; i++) {
        String record = records[i];

        Message message = new Message();
        message.setMessageID(readJsonString(record, "messageID"));
        message.setMessageNumber(
                Integer.parseInt(readJsonNumber(record, "messageNumber")));
        message.setRecipient(readJsonString(record, "recipient"));
        message.setMessageText(readJsonString(record, "messageText"));
        message.createMessageHash();
        message.setMessageStatus(readJsonString(record, "messageStatus"));

        messages[i] = message;
    }

    return messages;
}

private String readJsonNumber(String json, String key) throws IOException {
    String marker = "\"" + key + "\"";
    int keyPosition = json.indexOf(marker);

    if (keyPosition < 0) {
        throw new IOException("Missing JSON field: " + key);
    }

    int colon = json.indexOf(':', keyPosition + marker.length());

    if (colon < 0) {
        throw new IOException("Invalid JSON field: " + key);
    }

    int start = colon + 1;

    while (start < json.length()
            && Character.isWhitespace(json.charAt(start))) {
        start++;
    }

    int end = start;

    while (end < json.length()
            && Character.isDigit(json.charAt(end))) {
        end++;
    }

    if (start == end) {
        throw new IOException("Invalid number for field: " + key);
    }

    return json.substring(start, end);
}

private String readJsonString(String json, String key) throws IOException {
    String marker = "\"" + key + "\"";
    int keyPosition = json.indexOf(marker);

    if (keyPosition < 0) {
        throw new IOException("Missing JSON field: " + key);
    }

    int colon = json.indexOf(':', keyPosition + marker.length());
    int quote = colon < 0 ? -1 : json.indexOf('"', colon + 1);

    if (quote < 0) {
        throw new IOException("Invalid JSON field: " + key);
    }

    StringBuilder value = new StringBuilder();

    for (int i = quote + 1; i < json.length(); i++) {
        char c = json.charAt(i);

        if (c == '"') {
            return value.toString();
        }

        if (c == '\\') {
            if (++i >= json.length()) {
                throw new IOException("Invalid escape in JSON field: " + key);
            }

            char escaped = json.charAt(i);

            switch (escaped) {
                case '"': value.append('"'); break;
                case '\\': value.append('\\'); break;
                case 'n': value.append('\n'); break;
                case 'r': value.append('\r'); break;
                case 't': value.append('\t'); break;
                case 'b': value.append('\b'); break;
                case 'f': value.append('\f'); break;
                default:
                    throw new IOException("Unsupported JSON escape in: " + key);
            }
        } else {
            value.append(c);
        }
    }

    throw new IOException("Unclosed JSON string for field: " + key);
}

    
}
