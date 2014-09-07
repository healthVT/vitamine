package util.circleAnim;

/**
 * Created by Jay on 6/18/14.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import beans.VitaminBean;

public class CircleSurface extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private CircleAnimThread circleThread;
    private VitaminBean vitamin;

    public CircleSurface(Context context){
        super(context);

        holder = getHolder();
        holder.addCallback(this);
    }
    public CircleSurface(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        holder = getHolder();
        getHolder().addCallback(this);

    }

    public CircleSurface(Context context, AttributeSet attrs){
        super(context, attrs);
        holder = getHolder();
        getHolder().addCallback(this);
    }

    public void start(VitaminBean vitamin){
        this.vitamin = vitamin;
        circleThread = new CircleAnimThread(holder, getContext(), getResources(), vitamin);

        setLayoutParams(new LinearLayout.LayoutParams(
                circleThread.getWidth(),
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if(circleThread != null){
            circleThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        if(circleThread == null){
            circleThread = new CircleAnimThread(holder, getContext(), getResources(), vitamin);
            //circleThread.setSurfaceSize(width, height);
            circleThread.start();
            Log.d("Animate", "Start");
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = false;
        circleThread.isFinished();
        circleThread.interrupt();
        circleThread = null;
        while(retry){
            try{
                circleThread.join();
                retry = false;
            }catch(InterruptedException e) {}
        }
    }

}