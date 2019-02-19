package org.jabref.logic.layout.format;

import org.checkerframework.checker.units.qual.A;
import org.jabref.logic.layout.LayoutFormatter;
import org.jabref.logic.layout.StringInt;
import org.jabref.logic.util.strings.RtfCharMap;
import org.jabref.model.strings.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Transform a LaTeX-String to RTF.
 *
 * This method will:
 *
 *   1.) Remove LaTeX-Command sequences.
 *
 *   2.) Replace LaTeX-Special chars with RTF aquivalents.
 *
 *   3.) Replace emph and textit and textbf with their RTF replacements.
 *
 *   4.) Take special care to save all unicode characters correctly.
 *
 *   5.) Replace --- by \emdash and -- by \endash.
 */
public class RTFChars implements LayoutFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LayoutFormatter.class);

    private static final RtfCharMap RTF_CHARS = new RtfCharMap();

    @Override
    public String format(String field) {
        StringBuilder sb = new StringBuilder("");
        StringBuilder currentCommand = null;
        boolean escaped = false;
        boolean incommand = false;
        for (int i = 0; i < field.length(); i++) {

            char c = field.charAt(i);

            if (escaped && (c == '\\')) {
                sb.append('\\');
                escaped = false;
            }

            else if (c == '\\') {
                escaped = true;
                incommand = true;
                currentCommand = new StringBuilder();
            } else if (!incommand && ((c == '{') || (c == '}'))) {
                // Swallow the brace.
            } else if (Character.isLetter(c)
                    || StringUtil.SPECIAL_COMMAND_CHARS.contains(String.valueOf(c))) {
                escaped = false;
                if (incommand) {
                    // Else we are in a command, and should not keep the letter.
                    currentCommand.append(c);
                    testCharCom: if ((currentCommand.length() == 1)
                            && StringUtil.SPECIAL_COMMAND_CHARS.contains(currentCommand.toString())) {
                        // This indicates that we are in a command of the type
                        // \^o or \~{n}
                        if (i >= (field.length() - 1)) {
                            break testCharCom;
                        }

                        String command = currentCommand.toString();
                        i++;
                        c = field.charAt(i);
                        String combody;
                        if (c == '{') {
                            StringInt part = getPart(field, i, true);
                            i += part.i;
                            combody = part.s;
                        } else {
                            combody = field.substring(i, i + 1);
                        }

                        String result = RTF_CHARS.get(command + combody);

                        if (result != null) {
                            sb.append(result);
                        }

                        incommand = false;
                        escaped = false;

                    }
                } else {
                    sb.append(c);
                }

            } else {
                testContent: if (!incommand || (!Character.isWhitespace(c) && (c != '{') && (c != '}'))) {
                    sb.append(c);
                } else {
                    assert incommand;

                    // First test for braces that may be part of a LaTeX command:
                    if ((c == '{') && (currentCommand.length() == 0)) {
                        // We have seen something like \{, which is probably the start
                        // of a command like \{aa}. Swallow the brace.
                        continue;
                    } else if ((c == '}') && (currentCommand.length() > 0)) {
                        // Seems to be the end of a command like \{aa}. Look it up:
                        String command = currentCommand.toString();
                        String result = RTF_CHARS.get(command);
                        if (result != null) {
                            sb.append(result);
                        }
                        incommand = false;
                        escaped = false;
                        continue;
                    }

                    // Then look for italics etc.,
                    // but first check if we are already at the end of the string.
                    if (i >= (field.length() - 1)) {
                        break testContent;
                    }

                    if (((c == '{') || (c == ' ')) && (currentCommand.length() > 0)) {
                        String command = currentCommand.toString();
                        // Then test if we are dealing with a italics or bold
                        // command. If so, handle.
                        if ("em".equals(command) || "emph".equals(command) || "textit".equals(command)
                                || "it".equals(command)) {
                            StringInt part = getPart(field, i, c == '{');
                            i += part.i;
                            sb.append("{\\i ").append(part.s).append('}');
                        } else if ("textbf".equals(command) || "bf".equals(command)) {
                            StringInt part = getPart(field, i, c == '{');
                            i += part.i;
                            sb.append("{\\b ").append(part.s).append('}');
                        } else {
                            LOGGER.info("Unknown command " + command);
                        }
                        if (c == ' ') {
                            // command was separated with the content by ' '
                            // We have to add the space a
                        }
                    } else {
                        sb.append(c);
                    }

                }
                incommand = false;
                escaped = false;
            }
        }

        char[] chars = sb.toString().toCharArray();
        sb = new StringBuilder();

        for (char c : chars) {
            if (c < 128) {
                sb.append(c);
            } else {
                sb.append("\\u").append((long) c).append(transformSpecialCharacter(c));
            }
        }

        return sb.toString().replace("---", "{\\emdash}").replace("--", "{\\endash}").replace("``", "{\\ldblquote}")
                .replace("''", "{\\rdblquote}");
    }

    /**
     * @param text the text to extract the part from
     * @param i the position to start
     * @param commandNestedInBraces true if the command is nested in braces (\emph{xy}), false if spaces are sued (\emph xy)
     * @return a tuple of number of added characters and the extracted part
     */
    private StringInt getPart(String text, int i, boolean commandNestedInBraces) {
        char c;
        int count = 0;
        int icount = i;
        StringBuilder part = new StringBuilder();
        loop: while ((count >= 0) && (icount < text.length())) {
            icount++;
            c = text.charAt(icount);
            switch (c) {
            case '}':
                count--;
                break;
            case '{':
                count++;
                break;
            case ' ':
                if (!commandNestedInBraces) {
                    // in any case, a space terminates the loop
                    break loop;
                }
                break;
            default:
                break;
            }
            part.append(c);
        }
        String res = part.toString();
        // the wrong "}" at the end is removed by "format(res)"
        return new StringInt(format(res), part.length());
    }

    /**
     * This method transforms the unicode of a special character into its base character: 233 (é) - > e
     * @param c long
     * @return returns the basic character of the given unicode
     */
    private String transformSpecialCharacter(long c) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(256, "A");map.put(258, "A");map.put(260, "A");
        map.put(257, "a");map.put(259, "a");map.put(261, "a");
        map.put(199, "C");map.put(262, "C");map.put(264, "C");map.put(266, "C");map.put(268, "C");
        map.put(231, "c");map.put(263, "c");map.put(265, "c");map.put(267, "c");map.put(269, "c");
        map.put(208, "D");map.put(272, "D");
        map.put(240, "d");map.put(273, "d");
        map.put(274, "E");map.put(276, "E");map.put(278, "E");map.put(280, "E");map.put(282, "E");
        map.put(275, "e");map.put(277, "e");map.put(279, "e");map.put(281, "e");map.put(283, "e");
        map.put(284, "G");map.put(286, "G");map.put(288, "G");map.put(290, "G");map.put(330, "G");
        map.put(285, "g");map.put(287, "g");map.put(289, "g");map.put(291, "g");map.put(331, "g");
        map.put(292, "H");map.put(294, "H");
        map.put(293, "h");map.put(295, "h");
        map.put(296, "I");map.put(298, "I");map.put(300, "I");map.put(302, "I");map.put(304, "I");
        map.put(297, "i");map.put(299, "i");map.put(301, "i");map.put(303, "i");
        map.put(308, "J");
        map.put(309, "j");
        map.put(310, "K");
        map.put(311, "k");
        map.put(313, "L");map.put(315, "L");map.put(319, "L");
        map.put(314, "l");map.put(316, "l");map.put(320, "l");map.put(322, "l");
        map.put(209, "N");map.put(323, "N");map.put(325, "N");map.put(327, "N");
        map.put(241, "n");map.put(324, "n");map.put(326, "n");map.put(328, "n");
        map.put(216, "O");map.put(332, "O");map.put(334, "O");
        map.put(333, "o");map.put(335, "o");
        map.put(340, "R");map.put(342, "R");map.put(344, "R");
        map.put(341, "r");map.put(343, "r");map.put(345, "r");
        map.put(346, "S");map.put(348, "S");map.put(350, "S");map.put(352, "S");
        map.put(347, "s");map.put(349, "s");map.put(351, "s");map.put(353, "s");
        map.put(354, "T");map.put(356, "T");map.put(358, "T");
        map.put(355, "t");map.put(359, "t");
        map.put(360, "U");map.put(362, "U");map.put(364, "U");map.put(366, "U");map.put(370, "U");
        map.put(361, "u");map.put(363, "u");map.put(365, "u");map.put(367, "u");map.put(371, "u");
        map.put(372, "W");
        map.put(373, "w");
        map.put(374, "Y");map.put(376, "Y");map.put(221, "Y");
        map.put(375, "y");map.put(255, "y");
        map.put(377, "Z");map.put(379, "Z");map.put(381, "Z");
        map.put(378, "z");map.put(380, "z");map.put(382, "z");
        map.put(198, "AE");
        map.put(230, "ae");
        map.put(338, "OE");
        map.put(339, "oe");
        map.put(222, "TH");
        map.put(223, "ss");
        map.put(161, "!");

        //set range values in map
        for (int i = 192; i <=197; i++) {
            map.put(i, "A");
        }
        for (int i = 200; i <=203; i++) {
            map.put(i, "E");
        }
        for (int i = 204; i <=207; i++) {
            map.put(i, "I");
        }
        for (int i = 210; i <=214; i++) {
            map.put(i, "O");
        }
        for (int i = 217; i <=220; i++) {
            map.put(i, "U");
        }
        for (int i = 224; i <=229; i++) {
            map.put(i, "a");
        }
        for (int i = 232; i <=235; i++) {
            map.put(i, "e");
        }
        for (int i = 236; i <=239; i++) {
            map.put(i, "i");
        }
        for (int i = 242; i <=248; i++) {
            map.put(i, "o");
        }
        for (int i = 249; i <=251; i++) {
            map.put(i, "u");
        }

        // check to see if c exists in the hash map
        String character = map.get(c);
        if (character != null) {
            return character;
        }

        return "?";
    }
}
