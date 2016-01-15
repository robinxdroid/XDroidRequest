package com.xdroid.request.example.log;

import java.util.HashMap;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class LogFormattedString extends SpannableString {
    public static final HashMap<Character, Integer> LABEL_COLOR_MAP;

    public LogFormattedString(String line) {
        super(line);

        try {
            if (line.length() < 4) {
                throw new RuntimeException();
            }

            if (line.charAt(1) != '/') {
                throw new RuntimeException();
            }

            Integer labelColor = LABEL_COLOR_MAP.get(line.charAt(0));

            if (labelColor == null) {
                labelColor = LABEL_COLOR_MAP.get('E');
            }

            setSpan(new ForegroundColorSpan(labelColor), 0, 1, 0);
            setSpan(new StyleSpan(Typeface.BOLD), 0, 1, 0);

            int leftIdx;

            if ((leftIdx = line.indexOf(':', 2)) >= 0) {
                setSpan(new ForegroundColorSpan(labelColor), 2, leftIdx, 0);
                setSpan(new StyleSpan(Typeface.ITALIC), 2, leftIdx, 0);
            }
        } catch (Exception e) {
            setSpan(new ForegroundColorSpan(0xffddaacc), 0, length(), 0);
        }
    }
	
    static {
        LABEL_COLOR_MAP = new HashMap<Character, Integer>();
        LABEL_COLOR_MAP.put('D', 0xff9999ff);
        LABEL_COLOR_MAP.put('V', 0xffcccccc);
        LABEL_COLOR_MAP.put('I', 0xffeeeeee);
        LABEL_COLOR_MAP.put('E', 0xffff9999);
        LABEL_COLOR_MAP.put('W', 0xffffff99);
    }
}