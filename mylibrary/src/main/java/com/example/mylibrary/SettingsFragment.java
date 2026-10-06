package com.example.mylibrary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class SettingsFragment extends Fragment {


    View view;
    Button firstButton;
    private EditText editText_config = null;
    private Button button_reload = null;
    Bundle bundle = null;
    private String config_context = "local_config";
    private String config_context_br = "baserelay_config";
    public String filePath = null;
    public String folderPath = null;
    private Button importFile_button = null;
    private Button exportFile_button=null;
    private List<String> files = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText settings_fileName_et;
    Button showFiles_button;
    final ActivityResultLauncher<Intent> importFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                    onImportFileResult(result);
            }
    );
    final ActivityResultLauncher<Intent> exportFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                    onExportFileResult(result);
            }
    );














    public SettingsFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment

        view = inflater.inflate(R.layout.settings_fragment, container, false);
        EditText et_config = view.findViewById(R.id.et_config);
        editText_config = et_config;
//et_config.setHorizontallyScrolling(true);
//        et_config.setText(loadFile());


        //requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Button btn = view.findViewById(R.id.btn_settings_save);
        btn.setOnClickListener(this::button_save_onclick);;
        button_reload = view.findViewById(R.id.button_reload);

        Button btn_exportfile = view.findViewById(R.id.btn_exportfile);
        EditText finalEt_config = et_config;


        Button load_button = view.findViewById(R.id.button_reload);
        load_button.setOnClickListener(this::onClick);

        importFile_button = view.findViewById(R.id.btn_importfile);
        exportFile_button = view.findViewById(R.id.btn_exportfile);

        importFile_button.setOnClickListener(this::importFileButton_onClick);
        exportFile_button.setOnClickListener(this::exportFileButton_onClick);



        EditText et = view.findViewById(R.id.et_config);
        settings_fileName_et=view.findViewById(R.id.settings_fileName_et);
        String fileName = null;
        et_config = view.findViewById(R.id.et_config);
        showFiles_button = view.findViewById(R.id.showFiles_button);
        showFiles_button.setOnClickListener(this::showFiles_button_onClick);


        File file = new File(filePath);
        if (file.exists()) {
            settings_fileName_et.setText(filePath);
            et.setText(loadFile(filePath));
        }

        return view;
    }




    private void importFileButton_onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        importFileLauncher.launch(intent);
    }
    private void exportFileButton_onClick(View view) {

        String filename = Paths.get(settings_fileName_et.getText().toString()).getFileName().toString();
        if (filename.isBlank()) {
            Toast.makeText(requireContext(), "File path is blank", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        exportFileLauncher.launch(intent);
    }
    private void onImportFileResult(ActivityResult result) {
        Uri uri = result.getData().getData();

        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) return;
        if (uri == null) return;

        try {
            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[is.available()];
            is.read(bytes, 0, bytes.length);
            String c = new String(bytes, StandardCharsets.UTF_8);
            is.close();

            String filename = getFileNameFromUri(requireContext(), uri);

            File file = Paths.get(folderPath, filename).toFile();
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(c.getBytes());
            } finally {
                stream.close();
            }
            EditText et = view.findViewById(R.id.et_config);
            et.setText(Charset.forName("UTF-8").decode(ByteBuffer.wrap(bytes)));
            EditText settings_fileName_et=view.findViewById(R.id.settings_fileName_et);
            settings_fileName_et.setText(Paths.get(folderPath, filename).toString());
            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
        }

    }
    private void onExportFileResult(ActivityResult result) {
        Uri uri = result.getData().getData();

        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) return;
        if (uri == null) return;

        try {
            FileInputStream fileInputStream = new FileInputStream(settings_fileName_et.getText().toString());
            int length = fileInputStream.available();
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);
            fileInputStream.close();

            OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri);
            outputStream.write(bytes);
            outputStream.close();

            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }
    public String getFileNameFromUri(Context context, Uri uri) {
        String result = null;

        // Check if the URI scheme is content://
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    // Find the index of the column holding the display name
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Fallback: If it's a file:// URI or the query failed, fallback to the last path segment
        if (result == null) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : -1;
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }
    private void showFiles_button_onClick(View view) {
        FilePicker filePicker = new FilePicker(requireActivity());
        filePicker.OnCallbacks = new FilePicker.CallbacksListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSelect(String path) {
                File file = new File(path);
                if (file.isDirectory()) {
                    Toast.makeText(SettingsFragment.this.requireActivity(), "Selected item is a directory", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText settings_fileName_et=SettingsFragment.this.view.findViewById(R.id.settings_fileName_et);
                    settings_fileName_et.setText(Paths.get(folderPath, path).toString());
                    onClick(null);
            }

            @Override
            public List<FilePicker.FilePickerEntry> onList(String path) {
                File file = new File(Paths.get(folderPath, path).toString());

                List<File> files = new LinkedList<>();
                for (File f : file.listFiles()) {
                    files.add(f);
                }
                files.sort((u1, u2) -> u1.getName().compareTo(u2.getName()));
                List<FilePicker.FilePickerEntry> list = new ArrayList<>();
                for (int i = 0; i < files.size(); i++) {
                    if (files.get(i).isDirectory() == false) continue;;
                    FilePicker.FilePickerEntry filePickerEntry= new FilePicker.FilePickerEntry();
                    filePickerEntry.ItemName = files.get(i).getName();
                    filePickerEntry.IsDirectory = files.get(i).isDirectory();
                    list.add(filePickerEntry);
                }
                for (int i = 0; i < files.size(); i++) {
                    if (files.get(i).isDirectory()) continue;;
                    FilePicker.FilePickerEntry filePickerEntry= new FilePicker.FilePickerEntry();
                    filePickerEntry.ItemName = files.get(i).getName();
                    filePickerEntry.IsDirectory = files.get(i).isDirectory();
                    list.add(filePickerEntry);
                }
                return list;
            }

            @Override
            public void onCreate(String s) {
                try {
                    Files.createDirectory(Paths.get(folderPath, s));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onDelete(String s) {
                try {
                    Path path = Paths.get(folderPath, s);
                    Log.d("tag", path.toString());
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        filePicker.Show("DIR", 0);
    }

    public void onClick(View v) {
        EditText et = view.findViewById(R.id.et_config);
        EditText settings_fileName_et=view.findViewById(R.id.settings_fileName_et);
        String fileName = null;
        EditText et_config = view.findViewById(R.id.et_config);
        if (settings_fileName_et.getText().toString().isBlank()) {
            fileName= filePath;
        } else {
            fileName = settings_fileName_et.getText().toString();
        }

        et.setText(loadFile(fileName));
    }
    private void button_save_onclick(View v) {
        if (config_context.equals(config_context_br)) {
            baserelay_uploadConfig(null);
        } else {
            EditText settings_fileName_et= view.findViewById(R.id.settings_fileName_et);
            String fileName;
            if (settings_fileName_et.getText().toString().strip().isBlank()){
                fileName = filePath;
            } else {
                fileName = settings_fileName_et.getText().toString();
            }
            saveFile(fileName, editText_config.getText().toString());
            Toast.makeText(getActivity(), "File saved", Toast.LENGTH_SHORT).show();
        }
    }


    private String loadFile(String fileName) {
        try {
            File file = new File(fileName);

            if (file.exists() == false) {
                Toast.makeText(getContext(), "File does not exist", Toast.LENGTH_SHORT).show();
                return "";
            }
            FileInputStream stream = new FileInputStream(file);
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            try {
                stream.read(bytes);
            } finally {
                stream.close();
            }

            String contents = new String(bytes);

            return contents;
        } catch (Exception e) {
            Log.i("abc", e.toString());
        }
        return null;
    }

    private void saveFile(String fileName, String c) {
        try {
            File file = new File(fileName);

            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(c.getBytes());
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("abc", e.toString());
        }



    }

    private void loadListOfFiles(String dir) {
        File file = new File(dir);

        files.clear();
        listFiles(file, files);

        for (int i = 0; i < files.size(); i++) {
            files.set(i, files.get(i).substring(file.getPath().length()));
        }

    }
    private void listFiles(File file, List<String> list) {
        if (file.isFile()) {
            files.add(file.getPath());
            return;
        }

        File[] files = file.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() == false) {
                list.add(files[i].getPath());
                listFiles(files[i], list);
            }
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                list.add(files[i].getPath());
            }
        }


    }










    public void set_homeopathic_listener(java.util.function.BiFunction<Integer, Object, Void> f) {

        //java.util.function.BiFunction<Integer, Object, Void> homeopathic_listener = null;
        //this.homeopathic_listener = f;
    }
    private void baserelay_downloadConfig() {
//        HTTPRequest httpRequest = new HTTPRequest();
//        httpRequest.setOnComplete((arg) -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                if (httpRequest.result == 1) {
//
//                    progressDialog.dismiss();
//
//                    JSONObject jsonObject = new JSONObject(httpRequest.response);
//                    String status = (String)jsonObject.get("Status");
//                    String content = (String)jsonObject.get("Config");
//                    if (status.equals("OK")) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                EditText et = view.findViewById(R.id.et_config);
//                                et.setText(content);
//                            }
//                        });
//
//                    } else {
//                        throw new Exception("Invlid response");
//                    }
//                } else {
//                    throw httpRequest.exception;
//                }
//            } catch (Exception e) {
//                progressDialog.dismiss();
//                alertDialog.setOnCompleteCallback((arg_) -> {
//                    homeopathic_listener.apply(99, null);
//                    return null;
//                });
//                alertDialog.show(getContext(), "Error", "An error occurred, details:\n" + httpRequest.exception.toString()
//                        , "OK", null);
//
//
//            }
//            return null;
//        });
//        progressDialog.show("BaseRelay Config", "Downloading config from BaseRelay.");
//        //httpRequest.make("http://192.168.10.90/config?action=retrieve");


    }
    private void baserelay_uploadConfig(String config) {

//
//        HTTPRequest httpRequest = new HTTPRequest();
//        httpRequest.setOnComplete((arg) -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//
//                if (httpRequest.result == 1) {
//
//                    progressDialog.dismiss();
//                    JSONObject jsonObject = new JSONObject(httpRequest.response);
//                    String status= (String)jsonObject.get("Status");
//                    if (status.equals("OK")) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    } else {
//                        throw new Exception("An error occurred");
//                    }
//                } else {
//                    throw httpRequest.exception;
//                }
//            } catch (Exception e) {
//                progressDialog.dismiss();
//                alertDialog.setOnCompleteCallback((arg_) -> {
//                    homeopathic_listener.apply(99, null);
//                    return null;
//                });
//                e.printStackTrace();
//                alertDialog.show(getContext(), "Error", "An error occurred, details:\n" + e.toString()
//                        , "OK", null);
//
//
//            }
          //  return null;
        //});
//        progressDialog.show("BaseRelay Config", "Downloading config from BaseRelay.");

//        try {
//            String ccc = ((EditText) view.findViewById(R.id.et_config)).getText().toString();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("Config", ccc);
//            httpRequest.requestBody = jsonObject.toString(4);
//            httpRequest.make("http://192.168.10.90/config?action=store");
//        } catch (Exception e) {
//            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//        }

    }


}
