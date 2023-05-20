package com.example.tspjavapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

import androidx.annotation.Nullable;

public class MapView extends View {
    private Paint paint;

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.design_default_color_primary));
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Add your drawing logic here.
    }
}
