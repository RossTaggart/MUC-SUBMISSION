package com.taggart.ross.gcu_weather_app;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by http://hmkcode.com and repurposed by Ross
 */
public class Face
{
    Paint facePaint;
    Paint mePaint;

    float radius;
    float adjust;

    float mouthLeftX, mouthRightX, mouthTopY, mouthBottomY;
    RectF mouthRectF;
    Path mouthPath;

    RectF eyeLeftRectF, eyeRightRectF;
    float eyeLeftX, eyeRightx, eyeTopY, eyeBottomY;


    public Face(float radius) {
        this.radius = radius;

        //Sets up Paint object that deals with the style and colour info
        //on drawing geometric shapes
        facePaint = new Paint();
        facePaint.setColor(0xfff3300); // red
        facePaint.setDither(true);
        facePaint.setStrokeJoin(Paint.Join.ROUND);
        facePaint.setStrokeCap(Paint.Cap.ROUND);
        facePaint.setPathEffect(new CornerPathEffect(10));
        facePaint.setAntiAlias(true);
        //facePaint.setShadowLayer(4, 2, 2, 0x80000000);

        //Sets up Paint object that deals with the style and colour info
        //on drawing geometric shapes
        mePaint = new Paint();
        mePaint.setColor(0xf00ffcc);//light blue
        mePaint.setDither(true);
        mePaint.setStyle(Paint.Style.STROKE);
        mePaint.setStrokeJoin(Paint.Join.ROUND);
        mePaint.setStrokeCap(Paint.Cap.ROUND);
        mePaint.setPathEffect(new CornerPathEffect(10));
        mePaint.setAntiAlias(true);
        mePaint.setStrokeWidth(radius / 14.0f);

        //slight offset
        adjust = radius / 3.2f;

        // Left Eye
        eyeLeftX = radius - (radius * 0.30f);
        eyeRightx = eyeLeftX + (radius * 0.3f);
        eyeTopY = radius - (radius * 0.5f);
        eyeBottomY = eyeTopY + (radius * 0.4f);

        eyeLeftRectF = new RectF(eyeLeftX + adjust, eyeTopY + adjust, eyeRightx + adjust, eyeBottomY + adjust);

        // Right Eye
        eyeLeftX = eyeRightx + (radius * 0.3f);
        eyeRightx = eyeLeftX + (radius * 0.3f);

        eyeRightRectF = new RectF(eyeLeftX + adjust, eyeTopY + adjust, eyeRightx + adjust, eyeBottomY + adjust);


        // Smiley Mouth
        mouthLeftX = radius - (radius / 2.0f);
        mouthRightX = mouthLeftX + radius;
        mouthTopY = radius - (radius * 0.2f);
        mouthBottomY = mouthTopY + (radius * 0.5f);

        mouthRectF = new RectF(mouthLeftX + adjust, mouthTopY + adjust, mouthRightX + adjust, mouthBottomY + adjust);
        mouthPath = new Path();

        mouthPath.arcTo(mouthRectF, 30, 120, true);
    }

    public void draw(Canvas canvas) {

        // 1. draw face
        canvas.drawCircle(radius + adjust, radius + adjust, radius, facePaint);

        // 2. draw mouth
        mePaint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(mouthPath, mePaint);

        // 3. draw eyes
        mePaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(eyeLeftRectF, 0, 360, true, mePaint);
        canvas.drawArc(eyeRightRectF, 0, 360, true, mePaint);

    }
}

