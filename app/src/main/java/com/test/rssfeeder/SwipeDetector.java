package com.test.rssfeeder;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nishant- on 26-11-2015.
 */
public class SwipeDetector implements View.OnTouchListener {

    public static enum Action
    {
        LR,
        RL,
        TB,
        BT,
        None
    }


    private final String TAG = getClass().getSimpleName();
    private final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    public boolean swipeDetected()
    {
        return mSwipeDetected != Action.None;
    }

    public Action getAction()
    {
        return  mSwipeDetected;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downX = event.getY();
                mSwipeDetected = Action.None;
                return false;

            case MotionEvent.ACTION_UP:

                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;


                //Horizontal swipe detection
                if(Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                        Log.i(TAG, "L TO R");
                        mSwipeDetected = Action.LR;
                        return false;
                    }

                    if (deltaX > 0) {
                        Log.i(TAG, "R TO L");
                        mSwipeDetected = Action.RL;
                        return false;
                    }
                }else if(Math.abs(deltaY) > MIN_DISTANCE)
                {
                    //Vertical swipe detection
                    if(deltaY < 0)
                    {
                        Log.i(TAG,"Top To Bottom");
                        mSwipeDetected = Action.TB;
                        return false;
                    }
                    if(deltaY > 0)
                    {
                        Log.i(TAG,"Bottom To Top");
                        mSwipeDetected = Action.BT;
                        return false;
                    }
                }
                return false;

        }

        return false;

    }//End of OnTouch Function

}
