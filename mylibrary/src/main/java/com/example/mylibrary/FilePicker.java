package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//public class FilePicker {
//
//    Context context;
//    androidx.appcompat.app.AlertDialog alertDialog;
//    public CallbacksListener OnCallbacks;
//    List<FilePickerEntry> items;
//    public static int SELECTIONMODE_ANY = 0;
//    public static int SELECTIONMODE_DIR = 1;
//    public static int SELECTIONMODE_FILE = 2;
//    public String path = "/";
//    FilePickerEntry currentFolderEntry;
//    FilePickerEntry upperFolderEntry;
//
//    public FilePicker(Context context) {
//        this.context = context;
//
//        currentFolderEntry=  new FilePickerEntry();
//        currentFolderEntry.IsDirectory = true;
//        currentFolderEntry.ItemName = ".";
//
//        upperFolderEntry = new FilePickerEntry();
//        upperFolderEntry.IsDirectory = true;
//        upperFolderEntry.ItemName = "..";
//    }
//
//
//    public void Show(String title, int selectionMode) {
//
//        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//
//        items = new ArrayList<>();
//
//        View dialogView = ((AppCompatActivity)context).getLayoutInflater().inflate(com.example.mylibrary.R.layout.filepicker, null);
//        ListView recyclerView = dialogView.findViewById(com.example.mylibrary.R.id.alertdialog_list_listview);
//
//
//        //recyclerView.setLayoutManager(new LinearLayoutManager(context));
////        while (recyclerView.getItemDecorationCount() > 0) {
////            recyclerView.removeItemDecorationAt(0);
////        }
//        LVCustomAdapter rvCustomAdapter = new LVCustomAdapter(context);
//        rvCustomAdapter.callbacks = new LVCustomAdapter.OnCallbacks() {
//
//            @Override
//            public int getCount() {
//                return items.size();
//            }
//
//
//            @Override
//            public View getView(ViewGroup parent, View convertView, int position) {
//                if (convertView == null) {
//                    LayoutInflater inflater = (LayoutInflater.from(context));
//                    convertView = inflater.inflate(R.layout.recyclerview_simpletextitem, parent, false);
//                }
//                View view = convertView;
//                TextView textView = view.findViewById(R.id.alertDialog_listItem_textview);
//                textView.setText(items.get(position).ItemName);
//                return convertView;
//            }
//
//            @Override
//            public void onClick(View view, int pos) {
//                int i = rvCustomAdapter.getSelectedItemIndex();
//                if (i == -1) return;
//
//                if (items.get(i).IsDirectory) {
//                    FilePickerEntry entry = items.get(i);
//
//                    if (entry.ItemName.equals(".")) {
//                        return;
//                    }
//
//                    if (entry.ItemName.equals("..") && path.equals("/") == false) {
//                        String p = Paths.get(path).getParent().toString();
//                        path = p;
//                        items = OnCallbacks.onList(p);
//                        items.add(0, upperFolderEntry);
//                        items.add(0, currentFolderEntry);
//
//                        rvCustomAdapter.NotifyDatasetChanged();
//
//                    } else if (entry.ItemName.equals("..") == false) {
//                        path = Paths.get(path, items.get(i).ItemName).toString();
//                        items = OnCallbacks.onList(path);
//                        items.add(0, upperFolderEntry);
//                        items.add(0, currentFolderEntry);
//
//                        rvCustomAdapter.NotifyDatasetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onLongClick(View view, int pos) {
//
//            }
//
//            @Override
//            public void onSelectionChange(List<Integer> selectedItems) {
//
//            }
//        };
//        recyclerView.setAdapter(rvCustomAdapter);
//
//
//        CustomAlertDialog customAlertDialog = new CustomAlertDialog((AppCompatActivity) context);
//        customAlertDialog.Show(title, "Select file or folder.", dialogView,
//                new String[]{"SELECT", "Create", "Delete", "Cancel"},
//                new CustomAlertDialog.OnClickListener() {
//                    @Override
//                    public void onClick(View view, int pos) {
//
//                        if (pos == 0) {
//                            if (rvCustomAdapter.getSelectedItemIndex() == -1) {
//                                OnCallbacks.onSelect(path);
//                            }else {
//                                OnCallbacks.onSelect(Paths.get(path, items.get(rvCustomAdapter.getSelectedItemsIndices().get(0)).ItemName).toString());
//
//                            }
//                            customAlertDialog.dismiss();
//                        } else if (pos == 1) {
//                            EditText create = view.findViewById(R.id.filePicker_create);
//                            String s = create.getText().toString();
//                            if (s.isBlank() == false) {
//                                OnCallbacks.onCreate(s);
//                                OnCallbacks.onList(path);
//                            }
//                        } else if (pos == 2) {
//                            if (rvCustomAdapter.getSelectedItemIndex() != -1) {
//                                OnCallbacks.onDelete(path);
//                                OnCallbacks.onList(path);
//                            }
//                        } else if (pos == 3) {
//                            OnCallbacks.onCancel();
//                            customAlertDialog.dismiss();
//                        }
//                    }
//
//                    @Override
//                    public void onShow() {
//                        items = OnCallbacks.onList(path);
//                        items.add(0, upperFolderEntry);
//                        items.add(0, currentFolderEntry);
//
//                        rvCustomAdapter.notifyDataSetChanged();
//                    }
//                });
//
//
//
//
//    }
//
//
//
//    public static class FilePickerEntry  {
//        public String ItemName;
//        public boolean IsFile = false;
//        public boolean IsDirectory = true;
//        public boolean IsSelected = false;
//    }
//    public interface CallbacksListener {
//        public void onCancel();
//        public void onSelect(String path);
//        public List<FilePickerEntry> onList(String path);
//        void onCreate(String s);
//        void onDelete(String s);
//    }
//}
//





























public class FilePicker {

    Context context;
    androidx.appcompat.app.AlertDialog alertDialog;
    public CallbacksListener OnCallbacks;
    List<FilePickerEntry> items;
    public static int SELECTIONMODE_ANY = 0;
    public static int SELECTIONMODE_DIR = 1;
    public static int SELECTIONMODE_FILE = 2;
    public String path = "/";
    public String workingDirectory = "/";
    FilePickerEntry currentFolderEntry;
    FilePickerEntry upperFolderEntry;

    public FilePicker(Context context) {
        this.context = context;

        currentFolderEntry=  new FilePickerEntry();
        currentFolderEntry.IsDirectory = true;
        currentFolderEntry.ItemName = ".";

        upperFolderEntry = new FilePickerEntry();
        upperFolderEntry.IsDirectory = true;
        upperFolderEntry.ItemName = "..";
    }


    public void Show(String title, int selectionMode) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        items = new ArrayList<>();

        View dialogView = ((AppCompatActivity)context).getLayoutInflater().inflate(com.example.mylibrary.R.layout.filepicker, null);
        RecyclerView recyclerView = dialogView.findViewById(com.example.mylibrary.R.id.alertdialog_list_listview);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(false);
        RVCustomAdapter rvCustomAdapter = new RVCustomAdapter(context);
        rvCustomAdapter.callbacks = new RVCustomAdapter.OnCallbacks() {


            public int getCount() {
                return items.size();
            }



            public View getView(ViewGroup parent, View convertView, int position) {
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater.from(context));
                    convertView = inflater.inflate(R.layout.recyclerview_simpletextitem, parent, false);
                }
                View view = convertView;
                TextView textView = view.findViewById(R.id.alertDialog_listItem_textview);
                textView.setText(items.get(position).ItemName);
                return convertView;
            }

            @Override
            public ViewGroup onCreateViewHolder(ViewGroup parent) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_simpletextitem, parent, false);
                return view;
            }

            @Override
            public void onBindViewHolder(RVCustomAdapter.ViewHolder viewHolder, int position) {
                View view = viewHolder.view;
                TextView name_tv = view.findViewById(R.id.alertDialog_listItem_textview);
                name_tv.setText(items.get(position).Text);
            }

            @Override
            public int getItemCount() {
                return items.size();
            }

            @Override
            public void onClick(View view, int pos) {
                int i = rvCustomAdapter.getSelectedItemIndex();
                if (i == -1) return;

                if (items.get(i).IsDirectory) {
                    FilePickerEntry entry = items.get(i);

                    if (entry.ItemName.equals(".")) {
                        return;
                    }

                    if (entry.ItemName.equals("..") && workingDirectory.equals("/") == false) {
                        String p = Paths.get(workingDirectory).getParent().toString();
                        path = p;
                        items = OnCallbacks.onList(p);
                        workingDirectory = p;
                        items.add(0, upperFolderEntry);
                        items.add(0, currentFolderEntry);
                        for (FilePickerEntry e: items) {
                            if (e.IsDirectory && e!=upperFolderEntry && e!=currentFolderEntry) e.Text = "<DIR>   " + e.ItemName;
                            else e.Text = "       " + e.ItemName;
                        }
                        rvCustomAdapter.NotifyDatasetChanged();

                    } else if (entry.ItemName.equals("..") == false) {
                        path = Paths.get(workingDirectory, items.get(i).ItemName).toString();
                        items = OnCallbacks.onList(path);
                        workingDirectory = path;
                        items.add(0, upperFolderEntry);
                        items.add(0, currentFolderEntry);
                        for (FilePickerEntry e: items) {
                            if (e.IsDirectory) e.Text = "<DIR>   " + e.ItemName;
                            else e.Text = "       " + e.ItemName;
                        }
                        rvCustomAdapter.NotifyDatasetChanged();
                    }
                } else {
                    path = Paths.get(workingDirectory, items.get(i).ItemName).toString();
                }
            }

            @Override
            public void onLongClick(View view, int pos) {

            }

            @Override
            public void onSelectionChange(List<Integer> selectedItems) {

            }
        };
        recyclerView.setAdapter(rvCustomAdapter);


        CustomAlertDialog customAlertDialog = new CustomAlertDialog((AppCompatActivity) context);
        customAlertDialog.Show(title, "Select file or folder.", dialogView,
                new String[]{"SELECT", "Create", "Delete", "Cancel"},
                new CustomAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View view, int pos) {

                        if (pos == 0) {
                            if (rvCustomAdapter.getSelectedItemIndex() == -1) {
                                OnCallbacks.onSelect(path);
                            }else {
                                OnCallbacks.onSelect(Paths.get(path, items.get(rvCustomAdapter.getSelectedItemsIndices().get(0)).ItemName).toString());

                            }
                            customAlertDialog.dismiss();
                        } else if (pos == 1) {
                            EditText create = dialogView.findViewById(R.id.filePicker_create);
                            String s = create.getText().toString();
                            if (s.isBlank() == false) {
                                OnCallbacks.onCreate(Paths.get(path, s).toString());
                                items = OnCallbacks.onList(path);
                                rvCustomAdapter.NotifyDatasetChanged();
                            }
                        } else if (pos == 2) {
                            if (rvCustomAdapter.getSelectedItemIndex() != -1) {
                                OnCallbacks.onDelete(path);
                                path = Paths.get(path).getParent().toString();
                                items = OnCallbacks.onList(path);
                                rvCustomAdapter.NotifyDatasetChanged();
                            }
                        } else if (pos == 3) {
                            OnCallbacks.onCancel();
                            customAlertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onShow() {
                        items = OnCallbacks.onList(path);
                        items.add(0, upperFolderEntry);
                        items.add(0, currentFolderEntry);
                        for (FilePickerEntry e: items) {
                            if (e.IsDirectory) e.Text = "<DIR>   " + e.ItemName;
                            else e.Text = "       " + e.ItemName;
                        }
                        rvCustomAdapter.NotifyDatasetChanged();
                    }
                });




    }



    public static class FilePickerEntry  {
        public String ItemName;
        public String Text;
        public boolean IsFile = false;
        public boolean IsDirectory = true;
        public boolean IsSelected = false;
    }
    public interface CallbacksListener {
        public void onCancel();
        public void onSelect(String path);
        public List<FilePickerEntry> onList(String path);
        void onCreate(String s);
        void onDelete(String s);
    }
}

