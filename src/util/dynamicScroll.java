package util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.ScrollView;

/**
 * Created by Jay on 11/22/2014.
 */
public class dynamicScroll extends ScrollView {
    public static final int maxHeight = 350; // 100dp
    // default constructors

    public dynamicScroll(Context context) {
        super(context);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(dpToPx(getResources(),maxHeight), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}