package ir.smartdevelopers.smartspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class SmartSpinner extends AppCompatTextView {

    private SmartSpinnerBackgroundDrawable spinnerBackground;
    private int mDirection =0;
    private int mTint =0;
    private int mLineColor =0;
    private int mArrowColor =0;
    private int mArrowSize =-1;
    private boolean mRoundArrow;
    public SmartSpinner(Context context) {
        super(context);
        init(context,null);
    }

    public SmartSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context,AttributeSet attributeSet){
        spinnerBackground =new SmartSpinnerBackgroundDrawable(context);

        boolean roundArrow=false;
        if (attributeSet!=null){
            int[] set={android.R.attr.layoutDirection};
            TypedArray typedArray=context.obtainStyledAttributes(attributeSet,set);
            mDirection =typedArray.getInt(0, View.LAYOUT_DIRECTION_LTR);
            typedArray=context.obtainStyledAttributes( attributeSet,R.styleable.SmartSpinner);
            mLineColor =typedArray.getColor(R.styleable.SmartSpinner_lineColor,0);
            mArrowColor =typedArray.getColor(R.styleable.SmartSpinner_arrowColor,0);
            mTint =typedArray.getColor(R.styleable.SmartSpinner_tint,0);
            mArrowSize =typedArray.getDimensionPixelSize(R.styleable.SmartSpinner_arrowSize,-1);
            roundArrow=typedArray.getBoolean(R.styleable.SmartSpinner_roundArrow,false);
            typedArray.recycle();
        }
        spinnerBackground.setDirection(mDirection);
        spinnerBackground.setLineColor(mLineColor);
        spinnerBackground.setArrowColor(mArrowColor);
        if (mTint !=0) {
            spinnerBackground.setTint(mTint);
        }
        if (mArrowSize !=-1){
            spinnerBackground.setArrowSize(mArrowSize);
        }
        spinnerBackground.setRoundArrow(roundArrow);
        setBackground(spinnerBackground);
        setClickable(true);
    }



    public int getTint() {
        return mTint;
    }

    public void setTint(int tint) {
        this.mTint = tint;
        spinnerBackground.mutate();
        spinnerBackground.setTint(mTint);
        postInvalidate();
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        spinnerBackground.setLineColor(mLineColor);
        postInvalidate();
    }

    public int getArrowColor() {
        return mArrowColor;
    }

    public void setArrowColor(int arrowColor) {
        this.mArrowColor = arrowColor;
        spinnerBackground.setArrowColor(mArrowColor);
        postInvalidate();
    }


    public boolean isRoundArrow() {
        return mRoundArrow;
    }

    public void setRoundArrow(boolean roundArrow) {
        mRoundArrow = roundArrow;
        spinnerBackground.setRoundArrow(mRoundArrow);
        postInvalidate();
    }
}
