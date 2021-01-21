package com.electrosoft.electrosoftnew.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.electrosoft.electrosoftnew.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Math.round;

public class YourMarkerView extends MarkerView {


    private final TextView tvContent;
    private final MPPointF mOffset2 = new MPPointF();
    private MPPointF mOffset = new MPPointF();

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public YourMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        MPPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = getWidth();
        float height = getHeight();

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX;
        } else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
            mOffset2.x = chart.getWidth() - posX - width;
        }

        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY;
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height;
        }

        return mOffset2;
    }

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF((float) (-(getWidth() / 2)), (float) (-getWidth()));
        }

        return super.getOffset();
    }

    public void setOffset(MPPointF offset) {
        mOffset = offset;

        if (mOffset == null) {
            mOffset = new MPPointF();
        }
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        float x = e.getX();
        Date startDate = new Date((long) x);

        float roundOff = round(e.getY() * 100f) / 100f;

//        DateFormat formatter = DateFormat.getDateTimeInstance(
//                DateFormat.LONG,
//                DateFormat.LONG,
//                currentLocale);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss");
        // get the time zone of Paskistan country
        TimeZone pakistan = TimeZone.getTimeZone("Asia/Karachi");
        // set the time zone to the date format
        sdf.setTimeZone(pakistan);
        // print the date to the console
        sdf.format(startDate);

        DateFormat outputformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");

        outputformat.setTimeZone(pakistan);


        tvContent.setText(roundOff + ", " + outputformat.format(startDate).toUpperCase());


        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {


        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
        int saveId = canvas.save();

        if (getResources().getDisplayMetrics().widthPixels - posX < getWidth()) {
            posX -= getWidth();
        }
        canvas.translate(posX + offset.x, posY + offset.y);
        draw(canvas);
        canvas.restoreToCount(saveId);

        super.draw(canvas, posX, posY);
    }


}
