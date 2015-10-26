package com.example.cyrille.mobop_ex2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cyrille on 26.10.15.
 */
public class FocusBoxView extends View {

    private static final int MIN_FOCUS_BOX_WIDTH = 50;
    private static final int MIN_FOCUS_BOX_HEIGHT = 20;

    private final Paint paint;
    private final int maskColor;
    private final int frameColor;
    private final int cornerColor;

    public FocusBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();

        maskColor = resources.getColor(R.color.focus_box_mask);
        frameColor = resources.getColor(R.color.focus_box_frame);
        cornerColor = resources.getColor(R.color.focus_box_corner);

        this.setOnTouchListener(getTouchListener());
    }

    private Rect box;

    private static Point ScrRes;

    private Rect getBoxRect() {

        if (box == null) {

            //FocusBoxUtils class contains some helper methods
            ScrRes = FocusBoxUtils.getScreenResolution(getContext());

            int width = ScrRes.x * 6 / 7;
            int height = ScrRes.y / 9;

            width = width == 0
                    ? MIN_FOCUS_BOX_WIDTH
                    : width < MIN_FOCUS_BOX_WIDTH ? MIN_FOCUS_BOX_WIDTH : width;

            height = height == 0
                    ? MIN_FOCUS_BOX_HEIGHT
                    : height < MIN_FOCUS_BOX_HEIGHT ? MIN_FOCUS_BOX_HEIGHT : height;

            int left = (ScrRes.x - width) / 2;
            int top = (ScrRes.y - height) / 2;

            box = new Rect(left, top, left + width, top + height);
        }

        return box;
    }

    public Rect getBox() {
        return box;
    }

    private void updateBoxRect(int dW, int dH) {

//        ... TODO
//        ....UPDATE THE FOCUS BOX DIMENSIONS
//        ...
    }

    private OnTouchListener touchListener;

    private OnTouchListener getTouchListener() {

        if (touchListener == null)
            touchListener = new OnTouchListener() {

                int lastX = -1;
                int lastY = -1;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = -1;
                            lastY = -1;
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int currentX = (int) event.getX();
                            int currentY = (int) event.getY();
                            try {
//                                ... TODO
//                                ...updateBoxRect(dx, dy);

//                                ...
                            } catch (NullPointerException e) {
                            }

                            return true;
                        case MotionEvent.ACTION_UP:
                            lastX = -1;
                            lastY = -1;
                            return true;
                    }

                    return false;
                }
            }

                    ;

        return touchListener;
    }

    @Override
    public void onDraw(Canvas canvas) {

        Rect frame = getBoxRect();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

//        ... TODO
//        ....DRAW FOCUS BOX
//        ...
        paint.setColor(cornerColor);
        canvas.drawCircle(frame.left - 32, frame.top - 32, 32, paint);
        canvas.drawCircle(frame.right + 32, frame.top - 32, 32, paint);
        canvas.drawCircle(frame.left - 32, frame.bottom + 32, 32, paint);
        canvas.drawCircle(frame.right + 32, frame.bottom + 32, 32, paint);

//        ...TODO
//        ...
    }
}
