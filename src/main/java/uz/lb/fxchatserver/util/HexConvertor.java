package uz.lb.fxchatserver.util;

public class HexConvertor {
    public static String hexToText(String hex) {
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String hexChar = hex.substring(i, i + 2);
            textBuilder.append((char) Integer.parseInt(hexChar, 16));
        }
        return textBuilder.toString();
    }

    public static String textToHex(String text) {
        StringBuilder hexBuilder = new StringBuilder();
        for (char c : text.toCharArray()) {
            hexBuilder.append(String.format("%02x", (int) c));
        }
        return hexBuilder.toString();
    }
}
