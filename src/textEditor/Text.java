package textEditor;

public final class Text {
    private Text() {}

    public static String splitByLines(String text, int lineLength) {
        StringBuilder str = new StringBuilder(text);

        for (int i = lineLength; i < str.length(); i += lineLength) {
            int newLineIndex = str.lastIndexOf("\n", i);
            if (newLineIndex > i - lineLength)
                i = newLineIndex + 1;
            else {
                int spaceIndex = str.lastIndexOf(" ", i);
                if (spaceIndex > 0 && spaceIndex <= i) {
                    str.setCharAt(spaceIndex, '\n');
                    i = spaceIndex + 1;
                }
                else {
                    str.insert(i, "-\n");
                    i += 2;
                }
            }
        }

        return str.toString();
    }
}
