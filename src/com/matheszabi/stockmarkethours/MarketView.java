package com.matheszabi.stockmarkethours;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

public class MarketView extends View {

	public MarketView(Context context) {
		super(context);
		init();
	}

	public MarketView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MarketView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private Paint paint;
	private int mBackgroundColorFill;
	private int mGridColor;
	private int mHourColorMiddle;
	private int mHourColorMargin;
	private int[] colorInterval = new int[] {
			Color.rgb(220, 240, 200),//
			Color.rgb(240, 220, 200), };
	
	private int colorOverlap = Color.rgb(130,30,20);
	

	private int myTimezoneRawOffset;

	private void init() {
		paint = new Paint();
		mBackgroundColorFill = Color.rgb(0xFF, 0xFF, 0xDF);
		mGridColor = Color.rgb(0x0F, 0x0F, 0x0A);
		mHourColorMiddle = Color.rgb(0xFF, 0x7F, 0x7F);
		mHourColorMargin = Color.rgb(0xFF, 0x1F, 0x1F);

		myTimezoneRawOffset = TimeZone.getDefault().getRawOffset() / 3600000;

		setClickable(true);
		setOnClickListener(new MyOnClickListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */

	@Override
	protected void onDraw(Canvas canvas) {
		// Log.e("MarketView", "onDraw");
		mWidthCanvas = canvas.getWidth();
		mHeightCanvas = canvas.getHeight();
		// 24 hours + 25 separators
		mWidthCell = (mWidthCanvas / 24) - 1;
		paint.setTextSize(mWidthCell / 2);

		drawGrid(canvas);

		if (getContext() instanceof MainActivity) {
			// from settings / menu add markets, intervals, what needed!
			MainActivity mainActivity = (MainActivity) getContext();
			LabeledInterval[] intervals = mainActivity.getLabeledIntervals();
			if (intervals != null) {
				if (intervals.length > 1) { // draw overlaps
					for (int i = 0; i < intervals.length; i += 2) {
						if (i < intervals.length - 1) {
							drawOverlapInterval(canvas, i, intervals[i], intervals[i + 1]);
						}
					}
				}

				for (int i = 0; i < intervals.length; i++) {
					drawInterval(canvas, i, intervals[i]);
				}
			}
		}
	}

	private int mWidthCanvas, mHeightCanvas;// canvas size
	private int mWidthCell;
	private int headerEnd;
	private int x, y, width, height;// temp

	private void drawGrid(Canvas canvas) {
		paint.setColor(mBackgroundColorFill);
		canvas.drawPaint(paint);
		// background:
		paint.setColor(mGridColor);
		// borders:
		canvas.drawLine(0, 0, mWidthCanvas, 0, paint);
		canvas.drawLine(mWidthCanvas - 1, 0, mWidthCanvas - 1, mHeightCanvas, paint);
		canvas.drawLine(mWidthCanvas - 1, mHeightCanvas - 1, 0, mHeightCanvas - 1, paint);
		canvas.drawLine(0, mHeightCanvas - 1, 0, 0, paint);

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// vertical lines
		if (mWidthCell > 1) {
			FontMetrics fm = paint.getFontMetrics();
			height = (int) (4 + -fm.top + -fm.bottom);
			for (int i = 1; i <= 24; i++) {
				x = i + i * mWidthCell;
				canvas.drawLine(x, 0, x, mHeightCanvas, paint);

				if (i - 1 == hour) {// draw hour
					int minX = x - mWidthCell + mWidthCell * minute / 60;

					paint.setColor(mHourColorMiddle);// draw 1 pixel
					canvas.drawLine(minX - 1, 15, minX - 1, mHeightCanvas - 15, paint);
					canvas.drawLine(minX - 0, 15, minX, mHeightCanvas - 15, paint);
					canvas.drawLine(minX + 1, 15, minX + 1, mHeightCanvas - 15, paint);

					paint.setColor(mHourColorMargin);// draw 1+1 pixel
					canvas.drawLine(minX - 2, 15, minX - 2, mHeightCanvas - 15, paint);
					canvas.drawLine(minX + 2, 15, minX + 2, mHeightCanvas - 15, paint);

					// leave the color unchanged
					paint.setColor(mGridColor);
				}

				String nr = "" + (i - 1);
				width = (int) paint.measureText(nr);
				canvas.drawText(nr, x - mWidthCell + 2, height, paint);
			}
			y = 3 + height;
			canvas.drawLine(0, y, mWidthCanvas, y, paint);

			headerEnd = y;
		}

	}

	private int labelHeight = 70;

	private int[] outParams = new int[4];
	private int[] outParams2 = new int[4];

	private void drawInterval(Canvas canvas, int i, LabeledInterval labeledInterval) {
		// skip a bit from header and draw a rectangle with a color.

		getIntervalBounds(outParams, labeledInterval, i);
		x = outParams[0];
		y = outParams[1];
		width = outParams[2];

		paint.setStrokeWidth(0);
		paint.setColor(colorInterval[i]);
		// draw rectangle
		canvas.drawRect(x, y, width, y + labelHeight, paint);

		paint.setColor(mGridColor);
		paint.setStrokeWidth(1);
		FontMetrics fm = paint.getFontMetrics();
		height = (int) (4 + -fm.top + -fm.bottom);
		int widthText = (int) paint.measureText(labeledInterval.getLabel());

		int textX = (x + width) / 2 - widthText / 2;

		canvas.drawText(labeledInterval.getLabel(), textX, y + labelHeight / 2 + height / 2, paint);

	}

	private void getIntervalBounds(int[] params, LabeledInterval labeledInterval, int i) {
		
		// for me it is enough the London+New York, : 10:00 - 23:00 
		// but for Australia it need to calculate the daily overlaps : 23:00-00:00 + 00:00-7:00
		
		params[1] = 20 + headerEnd + i * (labelHeight + 10);// y
		// convert interval to utc, than converts to used timezone
		params[0] = (int) ((1 + mWidthCell) * (labeledInterval.getStartHourLocal() + labeledInterval.getStartMinuteLocal() / 60.0 //
				+ myTimezoneRawOffset - labeledInterval.getTimezoneLocal().getRawOffset() / 3600000));// x

		params[2] = (int) ((1 + mWidthCell) * (labeledInterval.getEndHourLocal() + labeledInterval.getEndMinuteLocal() / 60.0 //
				+ myTimezoneRawOffset - labeledInterval.getTimezoneLocal().getRawOffset() / 3600000));// width

		// params[3] = labelHeight;
	}

	private void drawOverlapInterval(Canvas canvas, int i, LabeledInterval labeledInterval, LabeledInterval labeledInterval2) {
		getIntervalBounds(outParams, labeledInterval, i);
		getIntervalBounds(outParams2, labeledInterval2, i + 1);

		x = outParams2[0];
		y = outParams[1] - 5;
		width = outParams[2];
		height = outParams2[1] + labelHeight+5;
				
		paint.setStrokeWidth(0);
		paint.setColor(colorOverlap);
		// draw rectangle
		canvas.drawRect(x, y, width, height, paint);
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.invalidate();
			v.refreshDrawableState();
		}
	}
}
