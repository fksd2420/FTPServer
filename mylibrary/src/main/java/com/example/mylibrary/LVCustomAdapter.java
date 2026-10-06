package com.example.mylibrary;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class LVCustomAdapter extends BaseAdapter {

    public static int DEFAULT_COLOR = 0x00000000;
    public static int PRESSED_COLOR = 0xffcccccc;
    private Context context;
    private List<Integer> selectedItems = new ArrayList<>();
    public OnCallbacks callbacks;
    boolean selectionEnabled = true;
    ListView listView = null;

    public LVCustomAdapter(Context context) {

    }


    @Override
    public int getCount() {
        return callbacks.getCount();
    }

    @Override
    public Object getItem(int i) {

        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listView = (ListView) parent;
        View view = callbacks.getView(parent, convertView, position);

        ItemTag itemTag = new ItemTag();
        itemTag.view = view;
        itemTag.position = position;

        view.setTag(itemTag);

        view.setOnClickListener(this::row_onClick);
        view.setOnLongClickListener(this::row_onLongClick);

        boolean isSelected = false;
        if (selectedItems.contains(position)) isSelected = true;

        if (selectionEnabled) {
            if (isSelected) {
                view.setBackground(UIHelper.createDrawableSelector(PRESSED_COLOR, PRESSED_COLOR));
            } else {
                view.setBackground(UIHelper.createDrawableSelector(DEFAULT_COLOR, PRESSED_COLOR));
            }
        } else {
            if (isSelected) {
                view.setBackground(UIHelper.createDrawableSelector(PRESSED_COLOR, PRESSED_COLOR));
            } else {
                view.setBackground(UIHelper.createDrawableSelector(DEFAULT_COLOR, DEFAULT_COLOR));
            }

        }

        return view;

    }

    private void row_onClick(View view) {
        if (selectionEnabled == false) return;
        ItemTag itemTag = (ItemTag) view.getTag();
        int index = itemTag.position;

        if (selectionMode == SelectionMode.None) {
            callbacks.onClick(view, index);
            return;
        }


        boolean selected = selectedItems.contains(index);
        selectedItems.remove((Integer) index);
        selected = !selected;


        if (selectionMode == SelectionMode.Single) {
            for (int i : selectedItems) {
                selectedItems.remove((Integer) i);

            }
        }
        if (selected)
            selectedItems.add(index);


        if (selected) {
            view.setBackground(UIHelper.createDrawableSelector(0xcccccccc, 0xffb4b4b4));
        } else {
            view.setBackground(UIHelper.createDrawableSelector(0x00000000, 0xffcccccc));
        }


        if (callbacks != null) {
            callbacks.onClick(view, index);
            callbacks.onSelectionChange(selectedItems);
        }
        notifyDataSetChanged();
    }
    private boolean row_onLongClick(View view) {
        if (selectionEnabled == false) return true;
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

        if (callbacks != null) {
            callbacks.onLongClick(view, ((ItemTag)view.getTag()).position);
        }
        return true;
    }



    public int getSelectedItemIndex() {
        return selectedItems.size() > 0 ? selectedItems.get(0) : -1;

    }
    public List<Integer> getSelectedItemsIndices() {
        return selectedItems;
    }


    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        NotifyDatasetChanged();
    }
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }


    public void SetSelectionEnabled(boolean b) {
        selectionEnabled =b;
        notifyDataSetChanged();
    }
    public void NotifyDatasetChanged() {

        selectedItems.clear();
        callbacks.onSelectionChange(selectedItems);
        notifyDataSetChanged();

        if (listView != null) {
            listView.setSelection(0);
            //listView.smoothScrollToPosition(0);
        }
    }





    SelectionMode selectionMode = SelectionMode.Single;
    public enum SelectionMode {
        None, Single, Multiple
    }
    public static class ItemTag {
        public View view;
        public int position;
        public Object object;
    }
    public interface OnCallbacks {
        public int getCount();


        public View getView(ViewGroup parent, View convertView, int position);
        public void onClick(View view, int pos);
        public void onLongClick(View view, int pos);

        void onSelectionChange(List<Integer> selectedItems);
    }




}