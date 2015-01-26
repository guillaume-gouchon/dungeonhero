package com.glevel.dungeonhero.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.dungeons.Directions;
import com.glevel.dungeonhero.models.dungeons.Dungeon;
import com.glevel.dungeonhero.models.dungeons.Room;

/**
 * Created by guillaume ON 10/3/14.
 */
public class Minimap extends View {

    private static final int MINIMAP_RECTANGLE_ALPHA = 150;

    private Dungeon mDungeon;
    private int mRectangleSize;

    private final Paint mCurrentRoomPaint = new Paint() {
        {
            setColor(getResources().getColor(R.color.green));
            setAlpha(MINIMAP_RECTANGLE_ALPHA);
        }
    };

    private final Paint mRoomPaint = new Paint() {
        {
            setColor(getResources().getColor(R.color.white));
            setAlpha(MINIMAP_RECTANGLE_ALPHA);
        }
    };

    public Minimap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Minimap(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // make it as a minimal square
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int minimumSize = Math.min(widthMeasure, heightMeasure);
        setMeasuredDimension(minimumSize, minimumSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDungeon != null) {

            // set size depending on the canvas size and the dungeon size
            mRectangleSize = canvas.getWidth() * 3 / 5 / Math.max(mDungeon.getRooms().length, mDungeon.getRooms()[0].length);

            Room[][] rooms = mDungeon.getRooms();
            Room room;

            // center map
            int xMin = 10000, xMax = -1, yMin = 10000, yMax = -1;
            for (int i = 0; i < rooms.length; i++) {
                for (int j = 0; j < rooms[0].length; j++) {
                    room = rooms[i][j];
                    if (room.isVisited() && room.getDoors() != null) {
                        if (j < xMin) {
                            xMin = j;
                        }

                        if (j > xMax) {
                            xMax = j;
                        }

                        if (i < yMin) {
                            yMin = i;
                        }

                        if (i > yMax) {
                            yMax = i;
                        }
                    }
                }
            }

            int iX = getPaddingLeft() + (canvas.getWidth() - mRectangleSize * 5 / 3 * (xMax - xMin + 1)) / 2;
            int iY = getPaddingTop() + (canvas.getHeight() - mRectangleSize * 5 / 3 * (yMax - yMin + 1)) / 2;
            for (int i = yMin; i < yMax + 1; i++) {
                for (int j = xMin; j < xMax + 1; j++) {
                    room = rooms[i][j];
                    if (room.isVisited() && room.getDoors() != null) {
                        canvas.drawRect(iX + (mRectangleSize * 5 / 3) * (j - xMin),
                                iY + (mRectangleSize * 5 / 3) * (i - yMin),
                                iX + (mRectangleSize * 5 / 3) * (j - xMin) + mRectangleSize,
                                iY + (mRectangleSize * 5 / 3) * (i - yMin) + mRectangleSize,
                                mDungeon.getCurrentRoom() == room ? mCurrentRoomPaint : mRoomPaint);
                        for (Directions direction : room.getDoors().keySet()) {
                            canvas.drawRect(iX + (mRectangleSize * 5 / 3) * (j - xMin) + (direction.getDx() * 2 + 1) * mRectangleSize / 3,
                                    iY + (mRectangleSize * 5 / 3) * (i - yMin) + (-direction.getDy() * 2 + 1) * mRectangleSize / 3,
                                    iX + (mRectangleSize * 5 / 3) * (j - xMin) + (direction.getDx() * 2 + 1) * mRectangleSize / 3 + mRectangleSize / 3,
                                    iY + (mRectangleSize * 5 / 3) * (i - yMin) + (-direction.getDy() * 2 + 1) * mRectangleSize / 3 + mRectangleSize / 3,
                                    mDungeon.getCurrentRoom() == room ? mCurrentRoomPaint : mRoomPaint);
                        }
                    }
                }
            }
        }
    }

    public void setDungeon(Dungeon dungeon) {
        mDungeon = dungeon;
        invalidate();
    }

}
