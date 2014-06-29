package util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import util.CircleAnimThread;

import java.util.jar.Attributes;

/**
 * Created by Jay on 6/26/14.
 */
public class CircleAnimView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private CircleAnimThread circleThread;

    public CircleAnimView(Context context){
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    public CircleAnimView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    public CircleAnimView(Context context, AttributeSet attrs){
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void setAnimStart(){
       if(circleThread != null){
           Log.d("THRead", circleThread.getState().toString());
           if(circleThread.getState() == Thread.State.NEW){
               circleThread.start();
           }else if(circleThread.getState() == Thread.State.TIMED_WAITING || circleThread.getState() == Thread.State.RUNNABLE){
               circleThread.onResume();
           }
           //circleThread.onResume();
//           if(circleThread.getState() == Thread.State.TERMINATED){
//               holder = getHolder();
//               if(holder == null){
//                   Log.e("HOLDER", "NULLLLLL");
//               }
//               circleThread = new CircleAnimThread(holder, getResources());
//           }
//           circleThread.start();
//           Log.d("FINISHED", String.valueOf(circleThread.isFinished()));
       }
    }

    public void stopAnim(){
        Log.d("STOP ANIM", "STOP");
        if(circleThread != null){
            circleThread.onPause();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

        if(circleThread == null){
            circleThread = new CircleAnimThread(holder, getResources());
            circleThread.setSurfaceSize(width, height);
            //circleThread.start();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = false;
        while(retry){
            try{
                circleThread.join();
                retry = false;
            }catch(InterruptedException e) {}
        }
    }
}
