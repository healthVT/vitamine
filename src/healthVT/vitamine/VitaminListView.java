package healthVT.vitamine;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Jay on 11/23/2014.
 */
public class VitaminListView extends ListView implements AbsListView.OnScrollListener {

    int focusItem;
    int mLastFirstVisibleItem;
    boolean mIsScrollingUp;
    boolean adjustScroll = false;
    List<String> viewList;

    public VitaminListView(Context context, List<String> column){
        super(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.number_list_row, R.id.numberTextView, column);

        this.viewList = column;

        setAdapter(adapter);
        setBackgroundColor(getResources().getColor(R.color.white));

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.numberPickerEachWidth), LinearLayout.LayoutParams.MATCH_PARENT);
        //llp.weight=0.3f;
        setLayoutParams(llp);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        final AbsListView thisView = view;
        switch(scrollState){
            case SCROLL_STATE_TOUCH_SCROLL:
                break;
            case SCROLL_STATE_IDLE:
                thisView.post(new Runnable() {
                    @Override
                    public void run() {
                        final AbsListView lw = thisView;

                        if (thisView.getId() == lw.getId()) {
                            final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

                            if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                                mIsScrollingUp = false;
                            } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                                mIsScrollingUp = true;
                            }

                            mLastFirstVisibleItem = currentFirstVisibleItem;
                        }


                        if(!adjustScroll){
                            if(!mIsScrollingUp){
                                focusItem++;
                            }
                            thisView.smoothScrollToPosition(focusItem);
                            adjustScroll = true;

                            thisView.postDelayed(new Runnable() {
                                public void run() {
                                    adjustScroll = false;
                                }
                            }, 800);
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        focusItem = firstVisibleItem;
    }

    public String getSelected(){
        String number;
        if(focusItem > viewList.size()-1){
            number = (String) getItemAtPosition(viewList.size()-1);
        }else{
            number = (String) getItemAtPosition(focusItem);
        }
        return number;
    }
}
