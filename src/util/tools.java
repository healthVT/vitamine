package util;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jay on 6/14/14.
 */
public class tools {

    public static boolean checkEmailFormat(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    public static int getVitaminColor(String vitaminString){

        if (vitaminString.equalsIgnoreCase("A")) {
            return Color.parseColor("#790000");
        }
        if (vitaminString.equalsIgnoreCase("C")) {
            return Color.parseColor("#fff799");
        }
        if (vitaminString.equalsIgnoreCase("D")) {
            return Color.parseColor("#b5e0b1");
        }
        if (vitaminString.equalsIgnoreCase("E")) {
            return Color.parseColor("#bd8cbf");
        }
        if (vitaminString.equalsIgnoreCase("H")) {
            return Color.parseColor("#a1d2ff");
        }
        if (vitaminString.equalsIgnoreCase("K")) {
            return Color.parseColor("#c8104c");
        }
        if (vitaminString.toUpperCase().contains("B")) {
            return Color.parseColor("#8ff8f7");
        }

        return Color.parseColor("#790000");
    }
}
