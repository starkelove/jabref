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
     * Normal ascii conversion cannot be used because special characters will be returned.
     * @param c long
     * @return returns the basic character of the given unicode
     */
    private String transformSpecialCharacter(long c) {
        HashMap<Long, String> map = new HashMap<>();
        map.put((long)256, "A");map.put((long) 258, "A");map.put((long) 260, "A");
        map.put((long) 257, "a");map.put((long) 259, "a");map.put((long) 261, "a");
        map.put((long) 199, "C");map.put((long) 262, "C");map.put((long) 264, "C");map.put((long) 266, "C");map.put((long) 268, "C");
        map.put((long) 231, "c");map.put((long) 263, "c");map.put((long) 265, "c");map.put((long) 267, "c");map.put((long) 269, "c");
        map.put((long) 208, "D");map.put((long) 272, "D");
        map.put((long) 240, "d");map.put((long) 273, "d");
        map.put((long) 274, "E");map.put((long) 276, "E");map.put((long) 278, "E");map.put((long) 280, "E");map.put((long) 282, "E");
        map.put((long) 275, "e");map.put((long) 277, "e");map.put((long) 279, "e");map.put((long) 281, "e");map.put((long) 283, "e");
        map.put((long) 284, "G");map.put((long) 286, "G");map.put((long) 288, "G");map.put((long) 290, "G");map.put((long) 330, "G");
        map.put((long) 285, "g");map.put((long) 287, "g");map.put((long) 289, "g");map.put((long) 291, "g");map.put((long) 331, "g");
        map.put((long) 292, "H");map.put((long) 294, "H");
        map.put((long) 293, "h");map.put((long) 295, "h");
        map.put((long) 296, "I");map.put((long) 298, "I");map.put((long) 300, "I");map.put((long) 302, "I");map.put((long) 304, "I");
        map.put((long) 297, "i");map.put((long) 299, "i");map.put((long) 301, "i");map.put((long) 303, "i");
        map.put((long) 308, "J");
        map.put((long) 309, "j");
        map.put((long) 310, "K");
        map.put((long) 311, "k");
        map.put((long) 313, "L");map.put((long) 315, "L");map.put((long) 319, "L");
        map.put((long) 314, "l");map.put((long) 316, "l");map.put((long) 320, "l");map.put((long) 322, "l");
        map.put((long) 209, "N");map.put((long) 323, "N");map.put((long) 325, "N");map.put((long) 327, "N");
        map.put((long) 241, "n");map.put((long) 324, "n");map.put((long) 326, "n");map.put((long) 328, "n");
        map.put((long) 216, "O");map.put((long) 332, "O");map.put((long) 334, "O");
        map.put((long) 333, "o");map.put((long) 335, "o");
        map.put((long) 340, "R");map.put((long) 342, "R");map.put((long) 344, "R");
        map.put((long) 341, "r");map.put((long) 343, "r");map.put((long) 345, "r");
        map.put((long) 346, "S");map.put((long) 348, "S");map.put((long) 350, "S");map.put((long) 352, "S");
        map.put((long) 347, "s");map.put((long) 349, "s");map.put((long) 351, "s");map.put((long) 353, "s");
        map.put((long) 354, "T");map.put((long) 356, "T");map.put((long) 358, "T");
        map.put((long) 355, "t");map.put((long) 359, "t");
        map.put((long) 360, "U");map.put((long) 362, "U");map.put((long) 364, "U");map.put((long) 366, "U");map.put((long) 370, "U");
        map.put((long) 361, "u");map.put((long) 363, "u");map.put((long) 365, "u");map.put((long) 367, "u");map.put((long) 371, "u");
        map.put((long) 372, "W");
        map.put((long) 373, "w");
        map.put((long) 374, "Y");map.put((long) 376, "Y");map.put((long) 221, "Y");
        map.put((long) 375, "y");map.put((long) 255, "y");
        map.put((long) 377, "Z");map.put((long) 379, "Z");map.put((long) 381, "Z");
        map.put((long) 378, "z");map.put((long) 380, "z");map.put((long) 382, "z");
        map.put((long) 198, "AE");
        map.put((long) 230, "ae");
        map.put((long) 338, "OE");
        map.put((long) 339, "oe");
        map.put((long) 222, "TH");
        map.put((long) 223, "ss");
        map.put((long) 161, "!");

        //set range values in map
        for (int i = 192; i <=197; i++) {
            map.put((long) i, "A");
        }
        for (int i = 200; i <=203; i++) {
            map.put((long) i, "E");
        }
        for (int i = 204; i <=207; i++) {
            map.put((long) i, "I");
        }
        for (int i = 210; i <=214; i++) {
            map.put((long) i, "O");
        }
        for (int i = 217; i <=220; i++) {
            map.put((long) i, "U");
        }
        for (int i = 224; i <=229; i++) {
            map.put((long) i, "a");
        }
        for (int i = 232; i <=235; i++) {
            map.put((long) i, "e");
        }
        for (int i = 236; i <=239; i++) {
            map.put((long) i, "i");
        }
        for (int i = 242; i <=248; i++) {
            map.put((long) i, "o");
        }
        for (int i = 249; i <=251; i++) {
            map.put((long) i, "u");
        }

        // check to see if c exists in the hash map
        String character = map.get(c);
        if (character != null) {
            return character;
        }
        return "?";
    }
}
