package ir.smartdevelopers.smartspinner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class SmartSpinnerBackgroundDrawable extends Drawable {

    private static final int STYLE_UNDERLINED = 1;
    private static final int STYLE_NORMAL = 0;
    private Paint trianglePaint,linePaint,ripplePaint;
    private Path trianglePath;
    private float triangleWith;
    private float triangleHeight;
    private float triangleMarginHorizontal;
    private float lineStrokeWidth;
    private int paddingStart,paddingBottom,paddingTop;
    private float rippleRadius=0;
    private float rippleRadiusEdn;
    private int mPressedColor;
    private int mTint;
    private int mArrowColor,mLineColor;
    private int mDefaultColor=Color.BLACK;
    private int mDirection = View.LAYOUT_DIRECTION_LTR;
    private boolean mRoundArrow=false;
    private int mDeactivateColor=0;
    private boolean mLastPressed=false;
    private int mStyle=STYLE_NORMAL;


    private boolean enabled=true;

    public SmartSpinnerBackgroundDrawable(Context context) {
        Resources resources=context.getResources();
        mArrowColor=mLineColor=mDefaultColor;
//        mDeactivateDefaultColor = ColorUtils.setAlphaComponent(mDefaultColor,24);
        int triangleDpWidth = 10;
        float arrowSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, triangleDpWidth,resources.getDisplayMetrics());
        setArrowSize((int) arrowSize);

        float triangleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, resources.getDisplayMetrics());
       int triangleMarginDpHorizontal = 16;
        triangleMarginHorizontal= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, triangleMarginDpHorizontal,resources.getDisplayMetrics());
        lineStrokeWidth= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,resources.getDisplayMetrics());
        rippleRadiusEdn= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,resources.getDisplayMetrics());
        paddingStart= (int) (triangleWith+(triangleMarginHorizontal*2));
        paddingBottom=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,resources.getDisplayMetrics());
        paddingTop=paddingBottom;

        TypedValue typedValue=new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorControlActivated,typedValue,true);
        int defaultPressColor = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.colorControlHighlight,typedValue,true);
        int rippleColor = typedValue.data;

        //<editor-fold desc="triangle">
        trianglePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setColor(mArrowColor);
        trianglePaint.setStrokeWidth(triangleStrokeWidth);
        trianglePaint.setStrokeJoin(Paint.Join.MITER);
        trianglePaint.setStrokeCap(Paint.Cap.SQUARE);
        trianglePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        trianglePath=new Path();
        //</editor-fold>

        //<editor-fold desc="Line">
        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineStrokeWidth);
        linePaint.setColor(mLineColor);
        //</editor-fold>

        ripplePaint=new Paint(Paint.ANTI_ALIAS_FLAG);

//        ripplePaint.setColor(resources.getColor(R.color.colorHilight));
        ripplePaint.setColor(rippleColor);

        mTint=mDefaultColor;
        mPressedColor= defaultPressColor;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @NonNull
    @Override
    public int[] getState() {
        return super.getState();
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        return super.setState(stateSet);
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (!enabled){
            int lineDeactivateColor=ColorUtils.setAlphaComponent(mLineColor,38);
            int arrowDeactivateColor=ColorUtils.setAlphaComponent(mArrowColor,38);
            if (mDeactivateColor!=0){
                lineDeactivateColor=mDeactivateColor;
                arrowDeactivateColor=mDeactivateColor;
            }
            linePaint.setColor(lineDeactivateColor);
            trianglePaint.setColor(arrowDeactivateColor);
            return true;
        }
        boolean pressed=false;


        for (int s:state) {
            if (s == android.R.attr.state_pressed) {
                pressed=true;
                break;
//            invalidateSelf();
            }

        }


            if (pressed) {
                linePaint.setColor(mPressedColor);
                linePaint.setStrokeWidth(lineStrokeWidth * 3);
                stopPressed = false;
                mLastPressed=true;
                animateRipplePress();
            } else if (mLastPressed){
                mLastPressed=false;
                linePaint.setColor(mLineColor);
                linePaint.setStrokeWidth(lineStrokeWidth);
                stopPressed = true;
                if (!isAnimating)
                    hideRipple();
            }


        return true;

    }

    private boolean isAnimating=false;
    private void hideRipple() {
        ValueAnimator valueAnimator=ValueAnimator.ofInt(67,0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int f=(int)animation.getAnimatedValue();
                ripplePaint.setAlpha(f);
                invalidateSelf();
            }
        });

        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private boolean stopPressed=false;

    private void animateRipplePress(){
        final ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,rippleRadiusEdn);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f=(float)animation.getAnimatedValue();
                int alpha=(int) ((f*127)/rippleRadiusEdn);
                alpha=127-alpha;
                if (alpha<67){
                    alpha=67;
                }
                ripplePaint.setAlpha(alpha);
                rippleRadius= f;
                invalidateSelf();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating=true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating=false;
                if (stopPressed){
                    hideRipple();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.start();

    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {

        if (mDirection == View.LAYOUT_DIRECTION_RTL) {
            padding.set(paddingStart, paddingTop, 0, paddingBottom);
        }else {
            padding.set(0, paddingTop, paddingStart, paddingBottom);
        }
        return true;
    }

    @Nullable
    @Override
    public ConstantState getConstantState() {
        return super.getConstantState();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect=getBounds();
        Log.v("TTT",rect.toString());
        float cTriangleY=((rect.bottom)/2f);
        float cTriangleX=(triangleWith/2)+triangleMarginHorizontal;
        float startTriangleY=cTriangleY-triangleHeight/2;
        float startTriangleX=triangleMarginHorizontal;
        /*if direction is ltr draw triangle from right side*/
        if (mDirection !=View.LAYOUT_DIRECTION_RTL){
            startTriangleX=rect.right - triangleMarginHorizontal - triangleWith;

        }
        float cx=startTriangleX+triangleWith/2;
        canvas.drawCircle(cx, cTriangleY,rippleRadius,ripplePaint);

        trianglePath.moveTo(startTriangleX,startTriangleY);
        trianglePath.rLineTo(triangleWith,0);
        trianglePath.rLineTo(-triangleWith/2,triangleHeight);
        trianglePath.lineTo(startTriangleX,startTriangleY);

        trianglePath.close();
        canvas.drawPath(trianglePath,trianglePaint);

        if (mStyle==STYLE_UNDERLINED) {
            canvas.drawLine(rect.left, rect.bottom - lineStrokeWidth, rect.right, rect.bottom - lineStrokeWidth, linePaint);
        }


    }

    @Override
    public void setAlpha(int alpha) {
        linePaint.setAlpha(alpha);
        trianglePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        linePaint.setColorFilter(colorFilter);
        trianglePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public int getPressedColor() {
        return mPressedColor;
    }

    public void setPressedColor(int pressedColor) {
        mPressedColor = pressedColor;
    }

    public int getTint() {
        return mTint;
    }

    @Override
    public void setTint(int tint) {
        mTint = tint;
        if (mTint!=0){
            mArrowColor=mTint;
            mLineColor=mTint;
        }else {
            mArrowColor=mDefaultColor;
            mLineColor=mDefaultColor;
        }
        trianglePaint.setColor(mArrowColor);
        linePaint.setColor(mLineColor);
        invalidateSelf();
    }

    public int getArrowColor() {
        return mArrowColor;
    }

    public void setArrowColor(int arrowColor) {
        if (arrowColor==0){return;}
        mArrowColor = arrowColor;
        trianglePaint.setColor(mArrowColor);
        invalidateSelf();
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        if (lineColor==0){return;}
        mLineColor = lineColor;
        linePaint.setColor(mLineColor);
        invalidateSelf();
    }
    public void setArrowSize(int size){
        triangleWith=size;
        triangleHeight=triangleWith/2;
        paddingStart= (int) (triangleWith+(triangleMarginHorizontal*2));
        invalidateSelf();
    }

    public int getDirection() {
        return mDirection;
    }

    /** @param direction {@link View#LAYOUT_DIRECTION_RTL } or {@link View#LAYOUT_DIRECTION_LTR}*/
    public void setDirection(int direction) {
        mDirection = direction;
        invalidateSelf();


    }


    public boolean isRoundArrow() {
        return mRoundArrow;
    }

    public void setRoundArrow(boolean roundArrow) {
        mRoundArrow = roundArrow;
        if (roundArrow) {
            trianglePaint.setStrokeCap(Paint.Cap.ROUND);
            trianglePaint.setStrokeJoin(Paint.Join.ROUND);
        }else {
            trianglePaint.setStrokeCap(Paint.Cap.SQUARE);
            trianglePaint.setStrokeJoin(Paint.Join.MITER);
        }

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDeactivateColor() {
        return mDeactivateColor;
    }

    public void setDeactivateColor(int deactivateColor) {
        mDeactivateColor = deactivateColor;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
    }
}
