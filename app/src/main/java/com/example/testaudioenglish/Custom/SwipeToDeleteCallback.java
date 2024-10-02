package com.example.testaudioenglish.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testaudioenglish.Adapter.AddFlashCardAdapter;
import com.example.testaudioenglish.R;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private final AddFlashCardAdapter mAdapter;
    private final Drawable deleteIcon;
    private final Paint clearPaint;
    private final int iconSize; // Kích thước icon

    public SwipeToDeleteCallback(AddFlashCardAdapter adapter, Context context) {
        mAdapter = adapter;
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete); // Thay bằng icon của bạn
        clearPaint = new Paint();
        clearPaint.setColor(Color.TRANSPARENT);

        // Đặt kích thước icon (ví dụ: 48dp)
        iconSize = (int) (48 * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false; // Không sử dụng kéo (drag)
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.deleteItem(viewHolder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT); // Chỉ cho phép kéo sang trái
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Kéo sang trái
            if (dX < 0) {
                final int itemHeight = viewHolder.itemView.getHeight();
                final int backgroundColor = Color.RED; // Màu nền
                c.drawRect(viewHolder.itemView.getRight() + dX, viewHolder.itemView.getTop(),
                        viewHolder.itemView.getRight(), viewHolder.itemView.getBottom(), clearPaint);

                // Vẽ icon xóa
                if (deleteIcon != null) {
                    int iconMargin = (itemHeight - iconSize) / 2; // Căn giữa icon
                    int iconTop = viewHolder.itemView.getTop() + iconMargin;
                    int iconBottom = iconTop + iconSize;
                    int iconLeft = viewHolder.itemView.getRight() - iconMargin - iconSize; // Điều chỉnh kích thước
                    int iconRight = iconLeft + iconSize;
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    deleteIcon.draw(c);
                }
            } else {
                // Khi không kéo nữa, xóa vẽ nền
                c.drawRect(viewHolder.itemView.getLeft(), viewHolder.itemView.getTop(),
                        viewHolder.itemView.getRight(), viewHolder.itemView.getBottom(), clearPaint);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
