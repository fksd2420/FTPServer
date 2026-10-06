package com.example.mylibrary;



import android.content.Context;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RVCustomAdapter extends RecyclerView.Adapter<RVCustomAdapter.ViewHolder> {


    Context context;
    public OnCallbacks callbacks;
    List<Integer> selectedItems = new ArrayList<>();

    private SelectionMode selectionMode = RVCustomAdapter.SelectionMode.Single;
    RecyclerView recyclerView;

    public boolean Ripple = false;


    public RVCustomAdapter(Context context) {
        this.context = context;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        view = callbacks.onCreateViewHolder(parent);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (callbacks != null) {
            callbacks.onBindViewHolder(holder, position);
        }

        boolean selected = selectedItems.contains(position);


            if (selected) {
                holder.view.setBackground(UIHelper.createDrawableSelector(0xffcccccc, 0xffb4b4b4));
            } else {
                holder.view.setBackground(UIHelper.createDrawableSelector(0x00000000, 0xffb4b4b4));
            }
            //Critical
//        holder.clicker.invalidate();
//            holder.clicker.requestLayout();
//        holder.view.invalidate();
//            holder.view.requestLayout();
    }

    @Override
    public int getItemCount() {
        return callbacks.getItemCount();
    }


    public int getSelectedItemIndex() {
        if (selectedItems.size() > 0) return selectedItems.get(0);
        return -1;
    }
    public List<Integer> getSelectedItemsIndices() {
        return selectedItems;
    }
    public void setSelectedItem(int index) {
        selectedItems.add((Integer) index);
        notifyItemChanged(index);
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;


        List<Integer> selectedItems_ = new ArrayList<>();
        for (int i : selectedItems) {
            selectedItems_.add((Integer) i);
        }
        for (int i : selectedItems_) {
            selectedItems.remove((Integer) i);
            notifyItemChanged(i);
        }


        //if (selectedItems.size() > 0) {
            callbacks.onSelectionChange(selectedItems);
        //}
    }
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }


    public void NotifyDatasetChanged() {

        selectedItems.clear();
        if (selectedItems.size() > 0) callbacks.onSelectionChange(selectedItems);
        notifyDataSetChanged();

        recyclerView.scrollToPosition(0);
        //recyclerView.smoothScrollToPosition(0);

    }


    public interface OnCallbacks {
        ViewGroup onCreateViewHolder(ViewGroup parent);

        void onBindViewHolder(ViewHolder viewHolder, int position);
        public int getItemCount();


        public void onClick(View view, int pos);
        public void onLongClick(View view, int pos);

        void onSelectionChange(List<Integer> selectedItems);
    }
    public enum SelectionMode {
        None, Single, Multiple
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view = null;
        boolean IsSelected = false;
        public RelativeLayout clicker;
        public TextView textview;




        public ViewHolder(ViewGroup view) {

            super(view);
            this.view = view;

            clicker = new RelativeLayout(view.getContext());

            if (Ripple)
                UIHelper.setSelectedD(clicker, 0x00000000, 0xff808080);

            //relativeLayout.setBackgroundColor(0xffff0000);
            clicker.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT));


//            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//             @Override
//             public void onLayoutChange(View v, int left, int top, int right, int bottom,
//                                        int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//                    int width = right - left;
//                    int height = bottom - top;
//                    int oldWidth = oldRight - oldLeft;
//                    int oldHeight = oldBottom - oldTop;
//
//
//                    //ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
//
//                    //if (params.width == view.getWidth() && params.height == view.getHeight()) return;
//
////                    params.width = view.getWidth();
////                    params.height =view.getHeight();
//
////                    view.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            //relativeLayout.setLayoutParams(params);
////                        }
////                    });
//             }
//         });


            clicker.setClickable(true);
            clicker.setHapticFeedbackEnabled(false);
            view.setClickable(false);

            // If root viwe is used as clicker, the ripple hides behind
            // the color of the 'select' background color.
            clicker.setOnClickListener(this::row_onClick);
            clicker.setOnLongClickListener(this::row_onLongClick);




            // AoA sir.
            // Sir the relative layout (clicker) doesn't expand int its parent.
            // When items are of different heights and views get recycled, using onLayoutChange
            // causes smaller items to have large container views.
            // Following works.
            // We are wrapping views in the relative layout (clicker).
            List<View> views = new ArrayList<>();
            for (int i = 0; i < view.getChildCount(); i++) {
                views.add(view.getChildAt(i));
            }
            view.removeAllViews();

            for (int i = 0; i < views.size(); i ++) {
                clicker.addView(views.get(i));
            }

            view.addView(clicker, 0);

        }



        private void row_onClick(View view_) {
            View selectBackground = (View)view_.getParent();

            int index = getAdapterPosition();

            if (selectionMode == SelectionMode.None) {
                callbacks.onClick(view_, index);
                return;
            }


            boolean selected = selectedItems.contains(index);
            selectedItems.remove((Integer) index);
            selected = !selected;


            if (selectionMode == SelectionMode.Single) {
                for (int i : selectedItems) {
                    selectedItems.remove((Integer) i);
                    notifyItemChanged(i);
                }
            }
            if (selected)
                selectedItems.add(index);


            if (selected) {
                selectBackground.setBackground(UIHelper.createDrawableSelector(0xcccccccc, 0xffb4b4b4));
            } else {
                selectBackground.setBackground(UIHelper.createDrawableSelector(0x00000000, 0xffcccccc));
            }


            if (callbacks != null) {
                callbacks.onClick(view, index);
                callbacks.onSelectionChange(selectedItems);
            }
        }
        private boolean row_onLongClick(View v) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            if (callbacks != null) {
                callbacks.onLongClick(v, getAdapterPosition());
            }
            return true;
        }

    }

}
