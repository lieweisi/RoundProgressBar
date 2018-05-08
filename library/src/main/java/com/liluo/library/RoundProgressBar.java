package com.liluo.library;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.liluo.library.util.PixelUtils;
/**
 * 带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author liwei
 */
public class RoundProgressBar extends View {
    /**
     * 弧度
     */
    private int   mAngle;
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;
    /**
     * 中间副标题的字符串的颜色
     */
    private int textSubColor;

    /**
     * 中间副标题的字符串的字体
     */
    private float textSubSize;

    /**
     * 中间副标题内容
     */
    private String subText = "";

    /**
     * 中间标题
     */
    private String centerText = "";

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    private Context context;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColors, Color.RED);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSizes, 15);
        textSubColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textSubColor, Color.GRAY);
        textSubSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSubSize, 14);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画中心文字
         */
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(0);
        paint1.setColor(textColor);
        paint1.setTextSize(textSize);
        paint1.setTypeface(Typeface.DEFAULT_BOLD); //设置字体

        paint1.setTextAlign(Paint.Align.CENTER);//这句话表明画的时候，以文本中间为基准坐标（仅限于X坐标，Y坐标不能靠这句话搞定

        int xPos = (canvas.getWidth() / 2);//表明文本的绘制X坐标以该坐标为中心
        int yPos;
        if (TextUtils.isEmpty(subText)) {
            yPos = (int) ((canvas.getHeight() / 2) - ((paint1.descent() + paint1.ascent()) / 2));//得到文本绘制的Y坐标
        } else {
            yPos = (int) ((canvas.getHeight() / 2) - ((paint1.descent() + paint1.ascent()) / 2)) - (int) PixelUtils.dp2px(10, context);//得到文本绘制的Y坐标
        }
        if (textIsDisplayable && style == STROKE) {
            canvas.drawText(centerText, xPos, yPos, paint1);
        }

        /**
         * 画副标题
         */
        paint1.setStrokeWidth(0);
        paint1.setColor(textSubColor);
        paint1.setTextSize(textSubSize);
        paint1.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        paint1.setTextAlign(Paint.Align.CENTER);//这句话表明画的时候，以文本中间为基准坐标（仅限于X坐标，Y坐标不能靠这句话搞定

        int xPos1 = (canvas.getWidth() / 2);//表明文本的绘制X坐标以该坐标为中心
        int yPos1 = (int) ((canvas.getHeight() / 2) - ((paint1.descent() + paint1.ascent()) / 2)) + (int) PixelUtils.dp2px(10, context);//得到文本绘制的Y坐标
        canvas.drawText(subText, xPos1, yPos1, paint1);

        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, mAngle, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, mAngle, true, paint);  //根据进度画圆弧
                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized String getSubText() {
        return subText;
    }

    /**
     * 设置副标题
     *
     * @param subText
     */
    public synchronized void setSubText(String subText) {
        this.subText = subText;
    }

    public synchronized String getCenterText() {
        return centerText;
    }

    public synchronized void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public int getTextSubColor() {
        return textSubColor;
    }

    public void setTextSubColor(int textSubColor) {
        this.textSubColor = textSubColor;
    }

    public float getTextSubSize() {
        return textSubSize;
    }

    public void setTextSubSize(float textSubSize) {
        this.textSubSize = textSubSize;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    /**
     * 设置带动画的进度
     *
     * @param p
     */
    public void setAnimProgress(int p) {
        if (p > max) {
            progress = max;
        } else {
            progress = p;
        }
        //设置属性动画
        ValueAnimator valueAnimator = new ValueAnimator().ofInt(0, p);
        //动画从快到慢
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(3000);
        //监听值的变化
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentV = (Integer) animation.getAnimatedValue();
                mAngle = 360 * currentV / max;
                invalidate();
            }
        });
        valueAnimator.start();
    }


    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public boolean isTextIsDisplayable() {
        return textIsDisplayable;
    }

    public void setTextIsDisplayable(boolean textIsDisplayable) {
        this.textIsDisplayable = textIsDisplayable;
    }
}