package hr.dbab.planer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import hr.dbab.planer.model.Task;

public class CustomView extends View {
    private Paint textPaintTitle;
    private Paint textPaintTime;
    String displayTitle;
    String displayTime;

    //konstruktori
    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    //metoda za inicijaliziranje Paint objekata
    private void init(@Nullable AttributeSet set){
        if (textPaintTitle == null) {
            textPaintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaintTitle.setColor(Color.BLACK);
            textPaintTitle.setTextSize(50);
        }
        if (textPaintTime == null){
            textPaintTime = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaintTime.setColor(Color.BLUE);
            textPaintTime.setTextSize(50);
        }
    }
    //metoda za dohvaćanje naslova i vremena
    public void setTitleTime(Task task){
        //dohvaćanje naslova i vremena i spremanje u varijablu
        displayTitle = task.getTitle();
        displayTime = task.getTime();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //izračun od kuda se počinje crtati
        int xTitle = this.getMeasuredWidth()/8;
        int yTitleTime = this.getMeasuredHeight()/2;
        int xTime = xTitle + 540;

        //iscrtavanje naslova
        canvas.drawText(displayTitle, xTitle, yTitleTime, textPaintTitle);

        //iscrtavanje vremena
        canvas.drawText(displayTime, xTime, yTitleTime, textPaintTime);
        invalidate();

    }

}