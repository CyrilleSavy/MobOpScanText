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
 * Created by Fadi on 5/11/2014.
 * modified by cyrille savy on 03.12.2015
 */
public class FocusBoxView extends View
    {
    private static final int MIN_FOCUS_BOX_WIDTH = 200;
    private static final int MIN_FOCUS_BOX_HEIGHT = 150;
    private static final int MIN_FOCUS_BOX_MARGIN = 50;

    private final Paint paint;
    private final int maskColor;
    private final int frameColor;
    private final int cornerColor;

    public FocusBoxView(Context context, AttributeSet attrs)
        {
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

    private Rect getBoxRect()
        {
        if (box == null)
            {
//            ScrRes = FocusBoxUtils.getScreenResolution(getContext());
            ScrRes = new Point(this.getWidth(), this.getHeight());

//            int width = ScrRes.x * 6 / 7;
//            int height = ScrRes.y / 9;


            int width = this.getWidth() * 6 / 7;//- 2 * MIN_FOCUS_BOX_MARGIN;
            int height = this.getHeight() / 9;//- 2 * MIN_FOCUS_BOX_MARGIN;

//            width = width == 0
//                    ? MIN_FOCUS_BOX_WIDTH
//                    : width < MIN_FOCUS_BOX_WIDTH ? MIN_FOCUS_BOX_WIDTH : width;
//
//            height = height == 0
//                    ? MIN_FOCUS_BOX_HEIGHT
//                    : height < MIN_FOCUS_BOX_HEIGHT ? MIN_FOCUS_BOX_HEIGHT : height;

            int left = (ScrRes.x - width) / 2;
            int top = (ScrRes.y - height) / 2;

//            int left = MIN_FOCUS_BOX_MARGIN;
//            int top = MIN_FOCUS_BOX_MARGIN;

            box = new Rect(left, top, left + width, top + height);
            }
        return box;
        }

    public Rect getBox()
        {
        return box;
        }

    public static enum FocusBoxSide
        {
            TOP, BOTTOM, RIGHT, LEFT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
        }


    private void updateBoxRect(int dW, int dH, FocusBoxSide side)
        {
        int newWidth = ((side == FocusBoxSide.LEFT) || (side == FocusBoxSide.BOTTOM_LEFT) || (side == FocusBoxSide.TOP_LEFT)) ? (box.width() - dW) : (box.width() + dW);
        newWidth = (newWidth >= ScrRes.x - 2 * MIN_FOCUS_BOX_MARGIN)
                ? (ScrRes.x - 2 * MIN_FOCUS_BOX_MARGIN)
                : (newWidth <= MIN_FOCUS_BOX_WIDTH) ? MIN_FOCUS_BOX_WIDTH : (newWidth);

        int newHeight = ((side == FocusBoxSide.TOP) || (side == FocusBoxSide.TOP_LEFT) || (side == FocusBoxSide.TOP_RIGHT)) ? (box.height() - dH) : (box.height() + dH);
        newHeight = (newHeight >= ScrRes.y - 2 * MIN_FOCUS_BOX_MARGIN)
                ? (ScrRes.y - 2 * MIN_FOCUS_BOX_MARGIN)
                : (newHeight <= MIN_FOCUS_BOX_HEIGHT) ? MIN_FOCUS_BOX_HEIGHT : (newHeight);

        int leftOffset = ((side == FocusBoxSide.LEFT) || (side == FocusBoxSide.TOP_LEFT) || (side == FocusBoxSide.BOTTOM_LEFT)) ? (newWidth <= MIN_FOCUS_BOX_WIDTH) ? box.left : (box.left - dW) : box.left;

        int rightOffset = ((side == FocusBoxSide.RIGHT) || (side == FocusBoxSide.TOP_RIGHT) || (side == FocusBoxSide.BOTTOM_RIGHT)) ? (newWidth <= MIN_FOCUS_BOX_WIDTH) ? box.right : (box.right + dW) : box.right;

        int topOffset = ((side == FocusBoxSide.TOP) || (side == FocusBoxSide.TOP_LEFT) || (side == FocusBoxSide.TOP_RIGHT)) ? (newHeight <= MIN_FOCUS_BOX_HEIGHT) ? box.top : (box.top - dH) : box.top;

        int bottomOffset = ((side == FocusBoxSide.BOTTOM) || (side == FocusBoxSide.BOTTOM_LEFT) || (side == FocusBoxSide.BOTTOM_RIGHT)) ? (newHeight <= MIN_FOCUS_BOX_HEIGHT) ? box.bottom : (box.bottom + dH) : box.bottom;

//        if (newWidth < MIN_FOCUS_BOX_WIDTH || newHeight < MIN_FOCUS_BOX_HEIGHT)
//            return;

        //box = new Rect(leftOffset, topOffset, leftOffset + newWidth, topOffset + newHeight);

        box = new Rect(leftOffset, topOffset, rightOffset, bottomOffset);
        }

    private OnTouchListener touchListener;

    private OnTouchListener getTouchListener()
        {
        if (touchListener == null)
            touchListener = new OnTouchListener()
            {
            int lastX = -1;
            int lastY = -1;

            @Override
            public boolean onTouch(View v, MotionEvent event)
                {
                switch (event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                        lastX = -1;
                        lastY = -1;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int currentX = (int) event.getX();
                        int currentY = (int) event.getY();
                        try
                            {
                            Rect rect = getBoxRect();
                            final int BUFFER = 50;
                            final int BIG_BUFFER = 60;
                            if (lastX >= 0)
                                {
                                if (((currentX >= rect.left - BIG_BUFFER
                                        && currentX <= rect.left + BIG_BUFFER)
                                        || (lastX >= rect.left - BIG_BUFFER
                                        && lastX <= rect.left + BIG_BUFFER))
                                        && ((currentY <= rect.top + BIG_BUFFER
                                        && currentY >= rect.top - BIG_BUFFER)
                                        || (lastY <= rect.top + BIG_BUFFER
                                        && lastY >= rect.top - BIG_BUFFER)))
                                    {
                                    // Top left corner: adjust both top and left sides
                                    updateBoxRect((lastX - currentX),
                                            (lastY - currentY), FocusBoxSide.TOP_LEFT);
                                    }
                                else if (((currentX >= rect.right - BIG_BUFFER
                                        && currentX <= rect.right + BIG_BUFFER)
                                        || (lastX >= rect.right - BIG_BUFFER
                                        && lastX <= rect.right + BIG_BUFFER))
                                        && ((currentY <= rect.top + BIG_BUFFER
                                        && currentY >= rect.top - BIG_BUFFER)
                                        || (lastY <= rect.top + BIG_BUFFER
                                        && lastY >= rect.top - BIG_BUFFER)))
                                    {
                                    // Top right corner: adjust both top and right sides
                                    updateBoxRect((currentX - lastX),
                                            (lastY - currentY), FocusBoxSide.TOP_RIGHT);
                                    }
                                else if (((currentX >= rect.left - BIG_BUFFER
                                        && currentX <= rect.left + BIG_BUFFER)
                                        || (lastX >= rect.left - BIG_BUFFER
                                        && lastX <= rect.left + BIG_BUFFER))
                                        && ((currentY <= rect.bottom + BIG_BUFFER
                                        && currentY >= rect.bottom - BIG_BUFFER)
                                        || (lastY <= rect.bottom + BIG_BUFFER
                                        && lastY >= rect.bottom - BIG_BUFFER)))
                                    {
                                    // Bottom left corner: adjust both bottom and left sides
                                    updateBoxRect((lastX - currentX),
                                            (currentY - lastY), FocusBoxSide.BOTTOM_LEFT);
                                    }
                                else if (((currentX >= rect.right - BIG_BUFFER
                                        && currentX <= rect.right + BIG_BUFFER)
                                        || (lastX >= rect.right - BIG_BUFFER
                                        && lastX <= rect.right + BIG_BUFFER))
                                        && ((currentY <= rect.bottom + BIG_BUFFER
                                        && currentY >= rect.bottom - BIG_BUFFER)
                                        || (lastY <= rect.bottom + BIG_BUFFER
                                        && lastY >= rect.bottom - BIG_BUFFER)))
                                    {
                                    // Bottom right corner: adjust both bottom and right sides
                                    updateBoxRect((currentX - lastX),
                                            (currentY - lastY), FocusBoxSide.BOTTOM_RIGHT);
                                    }
                                else if (((currentX >= rect.left - BUFFER
                                        && currentX <= rect.left + BUFFER)
                                        || (lastX >= rect.left - BUFFER
                                        && lastX <= rect.left + BUFFER))
                                        && ((currentY <= rect.bottom
                                        && currentY >= rect.top)
                                        || (lastY <= rect.bottom
                                        && lastY >= rect.top)))
                                    {
                                    // Adjusting left side: event falls within BUFFER pixels of
                                    // left side, and between top and bottom side limits
                                    updateBoxRect((lastX - currentX), 0, FocusBoxSide.LEFT);
                                    }
                                else if (((currentX >= rect.right - BUFFER
                                        && currentX <= rect.right + BUFFER)
                                        || (lastX >= rect.right - BUFFER
                                        && lastX <= rect.right + BUFFER))
                                        && ((currentY <= rect.bottom
                                        && currentY >= rect.top)
                                        || (lastY <= rect.bottom
                                        && lastY >= rect.top)))
                                    {
                                    // Adjusting right side: event falls within BUFFER pixels of
                                    // right side, and between top and bottom side limits
                                    updateBoxRect((currentX - lastX), 0, FocusBoxSide.RIGHT);
                                    }
                                else if (((currentY <= rect.top + BUFFER
                                        && currentY >= rect.top - BUFFER)
                                        || (lastY <= rect.top + BUFFER
                                        && lastY >= rect.top - BUFFER))
                                        && ((currentX <= rect.right
                                        && currentX >= rect.left)
                                        || (lastX <= rect.right
                                        && lastX >= rect.left)))
                                    {
                                    // Adjusting top side: event falls within BUFFER pixels of
                                    // top side, and between left and right side limits
                                    updateBoxRect(0, (lastY - currentY), FocusBoxSide.TOP);
                                    }
                                else if (((currentY <= rect.bottom + BUFFER
                                        && currentY >= rect.bottom - BUFFER)
                                        || (lastY <= rect.bottom + BUFFER
                                        && lastY >= rect.bottom - BUFFER))
                                        && ((currentX <= rect.right
                                        && currentX >= rect.left)
                                        || (lastX <= rect.right
                                        && lastX >= rect.left)))
                                    {
                                    // Adjusting bottom side: event falls within BUFFER pixels of
                                    // bottom side, and between left and right side limits
                                    updateBoxRect(0, (currentY - lastY), FocusBoxSide.BOTTOM);
                                    }
                                }
                            } catch (NullPointerException e)
                            {

                            }
                        v.invalidate();
                        lastX = currentX;
                        lastY = currentY;
                        return true;
                    case MotionEvent.ACTION_UP:
                        lastX = -1;
                        lastY = -1;
                        return true;
                    }
                return false;
                }
            };
        return touchListener;
        }

    @Override
    public void onDraw(Canvas canvas)
        {
        Rect frame = getBoxRect();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        paint.setAlpha(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(frameColor);
        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

        paint.setColor(cornerColor);
        canvas.drawCircle(frame.left - 32, frame.top - 32, 32, paint);
        canvas.drawCircle(frame.right + 32, frame.top - 32, 32, paint);
        canvas.drawCircle(frame.left - 32, frame.bottom + 32, 32, paint);
        canvas.drawCircle(frame.right + 32, frame.bottom + 32, 32, paint);
        }
    }
