package com.example.mySpecialConversion;

public class ParagraphsIndented {
    public static String indentParagraphs(String text, int indentationSpaces) {
        StringBuilder indentedText = new StringBuilder();

        // 使用 split("\n\n") 方法将文本按照两个连续的换行符拆分为段落
        String[] paragraphs = text.split("\n\n");

        for (int i = 0; i < paragraphs.length; i++) {
            // 删除段落前导空格
            String paragraph = removeLeadingSpaces(paragraphs[i]);

            String indentedParagraph = addIndentation(paragraph, indentationSpaces);
            indentedText.append(indentedParagraph);

            // 如果不是最后一个非空段落，则添加换行符
            if (i < paragraphs.length - 1 && !paragraph.trim().isEmpty()) {
                indentedText.append("\n\n");
            }
        }

        return indentedText.toString();
    }

    private static String removeLeadingSpaces(String paragraph) {
        StringBuilder result = new StringBuilder();

        String[] lines = paragraph.split("\n");
        for (String line : lines) {
            // 寻找段落中第一个非空格字符的索引
            int leadingNonSpaceIndex = 0;
            while (leadingNonSpaceIndex < line.length() && line.charAt(leadingNonSpaceIndex) == ' ') {
                leadingNonSpaceIndex++;
            }

            if (leadingNonSpaceIndex < line.length()) {
                // 非空格字符之后的内容添加到结果中
                result.append(line.substring(leadingNonSpaceIndex));
            }
            result.append("\n");
        }

        return result.toString().trim();
    }

    private static String addIndentation(String paragraph, int indentationSpaces) {
        StringBuilder indentedParagraph = new StringBuilder();

        // 使用 split("\n") 方法将段落按照换行符拆分为行
        String[] lines = paragraph.split("\n");

        for (String line : lines) {
            String indentation = "";
            for (int i = 0; i < indentationSpaces; i++) {
                indentation += "    ";
            }
            indentedParagraph.append(indentation).append(line).append("\n");
        }
        return indentedParagraph.toString();
    }
}
