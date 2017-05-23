package com.fupengpeng.cameraapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fupengpeng on 2017/5/23 0023.
 *     继承LinearLayout，自定义Layout
 */

public class NewCalendar extends LinearLayout{
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    //日历控件
    private Calendar curDate = Calendar.getInstance();

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context){
        bindControl(context);
        bindControlEvent();
        renderCalendar();
    }

    /**
     * 按钮点击事件
     */
    private void bindControlEvent() {
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //表示日历向前翻一个月
                curDate.add(Calendar.MONTH,-1);
                //渲染控件
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //表示日历向前翻一个月
                curDate.add(Calendar.MONTH,+1);
                //渲染控件
                renderCalendar();
            }


        });
    }
    //渲染控件
    private void renderCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtDate.setText(sdf.format(curDate));

        //gridview 数据展示
        ArrayList<Date> cells = new ArrayList<Date>();
        Calendar calendar = (Calendar) curDate.clone();
        //计算当月之前有多少天
        calendar.set(Calendar.DAY_OF_MONTH,1);//当月的第一天
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1;
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);

        int maxCellCount = 6*7;
        while ((cells.size()<maxCellCount)){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

    }
    private class CalendarAdapter extends ArrayAdapter<Date>{

        public CalendarAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            return super.getView(position, convertView, parent);
        }
    }

    /**
     * 控件的获取
     * @param context
     */
    private void bindControl(Context context) {
        //view绑定类文件，使用LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(context);
        //layout绑定类文件
        inflater.inflate(R.layout.calendar_view,this,false);

        btnPrev = (ImageView) findViewById(R.id.btnPrev);
        btnNext = (ImageView) findViewById(R.id.btnNext);
        txtDate = (TextView) findViewById(R.id.txtDate);
        grid = (GridView) findViewById(R.id.calendar_grid);
        


    }
}
