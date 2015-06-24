package com.xhanshawn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ImageDragShadowBuilder extends View.DragShadowBuilder {
    private Drawable shadow;

    public ImageDragShadowBuilder() {
        super();
    }

    public static View.DragShadowBuilder fromResource(Context context, int drawableId) {
        ImageDragShadowBuilder builder = new ImageDragShadowBuilder();

        builder.shadow = context.getResources().getDrawable(drawableId);
        if (builder.shadow == null) {
            throw new NullPointerException("Drawable from id is null");
        }

        builder.shadow.setBounds(0, 0, builder.shadow.getMinimumWidth(), builder.shadow.getMinimumHeight());

        return builder;
    }

    public static View.DragShadowBuilder fromBitmap(Context context, Bitmap bmp) {
        if (bmp == null) {
            throw new IllegalArgumentException("Bitmap cannot be null");
        }

        ImageDragShadowBuilder builder = new ImageDragShadowBuilder();

        builder.shadow = new BitmapDrawable(context.getResources(), bmp);
        builder.shadow.setBounds(0, 0, builder.shadow.getMinimumWidth(), builder.shadow.getMinimumHeight());

        return builder;
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        shadow.draw(canvas);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        shadowSize.x = shadow.getMinimumWidth();
        shadowSize.y = shadow.getMinimumHeight();

        shadowTouchPoint.x = (int)(shadowSize.x / 2);
        shadowTouchPoint.y = (int)(shadowSize.y / 2);
    }
}
