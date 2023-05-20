package com.example.tspjavapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View {

    private Paint paint = new Paint();
    private Canvas canvas;

    private int canvasWidth;
    private int canvasHeight;

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        this.canvasWidth = canvas.getWidth();
        this.canvasHeight = canvas.getHeight();
    }

    /**
     * Draw a circle on the canvas
     *
     * @param x      The x-coordinate of the center of the circle
     * @param y      The y-coordinate of the center of the circle
     * @param radius The radius of the circle
     * @param color  The color of the circle
     */
    public void drawCircle(int x, int y, int radius, int color) {
        int centerX = calculateCenterX(x, radius);
        int centerY = calculateCenterY(y, radius);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    /**
     * Draw a line on the canvas
     *
     * @param startX The x-coordinate of the start point of the line
     * @param startY The y-coordinate of the start point of the line
     * @param endX   The x-coordinate of the end point of the line
     * @param endY   The y-coordinate of the end point of the line
     * @param color  The color of the line
     */
    public void drawLine(int startX, int startY, int endX, int endY, int color) {
        int adjustedStartX = calculateCenterX(startX, 0);
        int adjustedStartY = calculateCenterY(startY, 0);
        int adjustedEndX = calculateCenterX(endX, 0);
        int adjustedEndY = calculateCenterY(endY, 0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(adjustedStartX, adjustedStartY, adjustedEndX, adjustedEndY, paint);
    }
    public void drawLineTH(int startX, int startY, int endX, int endY, int color, float strokeWidth) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    /**
     * Clear the canvas
     */
    public void clearCanvas() {
        canvas.drawColor(Color.WHITE);
    }

    /**
     * Calculate the adjusted X-coordinate based on the canvas width and radius
     *
     * @param x      The original X-coordinate
     * @param radius The radius
     * @return The adjusted X-coordinate
     */
    private int calculateCenterX(int x, int radius) {
        return (canvasWidth /15)+x ;
    }

    /**
     * Calculate the adjusted Y-coordinate based on the canvas height and radius
     *
     * @param y      The original Y-coordinate
     * @param radius The radius
     * @return The adjusted Y-coordinate
     */
    private int calculateCenterY(int y, int radius) {
        return (canvasHeight /15)+y ;
    }
}
