package com.example.mylibrary;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class SettingsFragment extends Fragment {


    String configFileName = "config.txt";
    View view;
    Button firstButton;
    private EditText editText_config = null;
    private Button button_reload = null;
    Bundle bundle = null;
    java.util.function.BiFunction<Integer, Object, Void> homeopathic_listener = null;
    private String config_context = "local_config";
    private String config_context_br = "baserelay_config";
    public String defaultFileName = "config.txt";
    private Button importFile_button = null;
    private Button exportFile_button=null;
    public OnImportExportFile onImportExportFile;



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


        Button btn = view.findViewById(R.id.btn_settings_save);
        btn.setOnClickListener(this::button_save_onclick);;
        button_reload = view.findViewById(R.id.button_reload);
        Button btn_importfile = view.findViewById(R.id.btn_importfile);
        btn_importfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeopathic_listener.apply(0, null);
            }
        });
        Button btn_exportfile = view.findViewById(R.id.btn_exportfile);
        EditText finalEt_config = et_config;
        btn_exportfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeopathic_listener.apply(1, finalEt_config.getText().toString());
            }
        });
        if (homeopathic_listener!=null)
        homeopathic_listener.apply(10, null);

        Button load_button = view.findViewById(R.id.button_reload);
        load_button.setOnClickListener(this::onClick);

        importFile_button = view.findViewById(R.id.btn_importfile);
        exportFile_button = view.findViewById(R.id.btn_exportfile);

        importFile_button.setOnClickListener(this::importFile_button_onClick);
        exportFile_button.setOnClickListener(this::exportFile_button_onClick);



        EditText et = view.findViewById(R.id.et_config);
        EditText settings_fileName_et=view.findViewById(R.id.settings_fileName_et);
        String fileName = null;
        et_config = view.findViewById(R.id.et_config);

        //defaultFileName = new File(getContext().getFilesDir(), defaultFileName).getPath();
        if (settings_fileName_et.getText().toString().isBlank()) {
            fileName=defaultFileName;
        } else {
            fileName = settings_fileName_et.getText().toString();
        }

        File file = new File(fileName);
        if (file.exists()) {
            settings_fileName_et.setText(fileName);

            et.setText(loadFile(fileName));
        } else{
            settings_fileName_et.setText(fileName);
        }


        return view;
    }


    public interface OnImportExportFile {
        public void onImportFile(OnImportFileReady onImportFileReady);
        public void onExportFile(String filename, String data, OnExportFileReady onExportFileReady);
    }
    public interface OnImportFileReady {
        public void onImportFileReady(Object data);
    }
    public interface OnExportFileReady {
        public void onExportFileReady(Object data);
    }

    public void importFile_button_onClick(View v) {
        onImportExportFile.onImportFile(new OnImportFileReady() {
            @Override
            public void onImportFileReady(Object data) {
                importCallback(data);
            }
        });
    }
    public void exportFile_button_onClick(View v) {
        String fileName = null;
        EditText et_config = view.findViewById(R.id.et_config);
        EditText settings_fileName_et=view.findViewById(R.id.settings_fileName_et);

        if (settings_fileName_et.getText().toString().isBlank()) {
            fileName=defaultFileName;
        } else {
            fileName = settings_fileName_et.getText().toString();
        }

        onImportExportFile.onExportFile(fileName, et_config.getText().toString(), new OnExportFileReady() {
            @Override
            public void onExportFileReady(Object data) {
                exportCallback(data);
            }
        });
    }
    private Void importCallback(Object data) {
        EditText et_config = view.findViewById(R.id.et_config);
        et_config.setText((String)data);
        return null;
    }
    private Void exportCallback(Object data) {
        return null;
    }



    public void onClick(View v) {
        EditText et = view.findViewById(R.id.et_config);
        EditText settings_fileName_et=view.findViewById(R.id.settings_fileName_et);
        String fileName = null;
        EditText et_config = view.findViewById(R.id.et_config);
        if (settings_fileName_et.getText().toString().isBlank()) {
            fileName=defaultFileName;
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
                fileName = defaultFileName;
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
            Log.i("abc", e.toString());
        }

    }









    public void set_homeopathic_listener(java.util.function.BiFunction<Integer, Object, Void> f) {
        this.homeopathic_listener = f;
    }
    public void callonready() {
        if (view != null && homeopathic_listener != null) {
            homeopathic_listener.apply(10, null);
        }
    }
    public Void do_something(Integer i, Object obj) {

        switch (i) {
            case 10:
                String context = (String) obj;
                config_context = context.toLowerCase();
                if (context.equals("baserelay_config")) {
                    baserelay_downloadConfig();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditText et = view.findViewById(R.id.et_config);
                            et.setText(loadFile(null));
                        }
                    });
                }
                break;
            case 0:
                String c = (String) obj;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        EditText et = view.findViewById(R.id.et_config);
                        et.setText(c);
                    }
                });
                saveFile(c, null);
                break;
            case 1:
                break;
            case 5:
                String c__ =(String)obj;
                EditText et = view.findViewById(R.id.et_config);
                et.setText(c__);
                break;
            default:
                break;
        }

        return null;
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
