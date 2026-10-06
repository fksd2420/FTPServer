package com.example.mylibrary;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.sshd.client.SshClient;
//import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
//import org.apache.sshd.common.session.SessionContext;
//import org.apache.sshd.server.SshServer;
//import org.apache.sshd.server.auth.password.PasswordAuthenticator;
//import org.apache.sshd.server.channel.ChannelSession;
//import org.apache.sshd.server.command.Command;
//import org.apache.sshd.server.command.CommandFactory;
//import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
//
//import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
//import org.apache.sshd.server.shell.ProcessShellCommandFactory;
//import org.apache.sshd.server.shell.ProcessShellFactory;

public class ConfigFile {
    public static class Section {
        public String Name;
        public String Content;
        public String OriginalSectionHeader;

        public boolean HasChanged = false;
    }



    private StringIO stringIO;
    private int line_no = 0;

    public Section DefaultSection = new Section();
    public LinkedHashMap<String, Section> Sections = new LinkedHashMap<String, Section>();

    public LinkedHashMap<String, Object> configD = new LinkedHashMap<String, Object>();




    public ConfigFile() {
    }



    public void Load(String path) throws Exception {

        stringIO = new StringIO(ReadAllText(path));
        DefaultSection = ReadSection(true);

        Section section = ReadSection(false);
        while (section != null)
        {
            Sections.put(section.Name, section);
            section = ReadSection(false);
        }
    }
    public void Save(String path) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();

        if (DefaultSection.HasChanged) {
            stringBuilder.append("\n\n");
            stringBuilder.append(DefaultSection.Content);
            stringBuilder.append("\n\n");
        } else {
            stringBuilder.append(DefaultSection.Content);
        }

        for (Map.Entry<String, Section> entry : Sections.entrySet()) {
            Section section = entry.getValue();

            if (section.HasChanged) {
                stringBuilder.append("|| " + section.Name + "\n");
                stringBuilder.append("=".repeat(section.Name.length() + 6) + "\n");
                stringBuilder.append("\n");
                stringBuilder.append(section.Content);
                stringBuilder.append("\n\n\n");
            } else {
                stringBuilder.append(section.OriginalSectionHeader);
                stringBuilder.append(section.Content);
            }
        }
        File file = new File(path);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(stringBuilder.toString().getBytes("UTF-8"));
        stream.close();
    }



    public Section ReadSection(boolean isDefaultSection) throws Exception {

        String sectionName = "";
        StringBuilder sectionContent = new StringBuilder();
        String originalSectionHeader = null;
        Section section = new Section();

        String line1, line2 = null;

        if (isDefaultSection == false)
        {

            line1 = stringIO.ReadLine();
            while (line1 != null)
            {
                line_no++;
                if (line1.isBlank() == false)
                {
                    break;
                }
                line1 = stringIO.ReadLine();
            }

            if (line1 == null)
            {
                return null;
            }

            line2 = stringIO.ReadLine();
            if (line2 == null)
            {
                throw new Exception("Expected section name, line no " + line_no);
            }
            line_no++;

            String line1_ = line1.stripTrailing();
            String line2_ = line2.stripTrailing();

            if (IsSectionHeaderTemplateMatch(line1, line2) == false)
            {
                throw new Exception("Invalid section header, line " + line_no);
            }

            section.OriginalSectionHeader = line1 + line2;
            section.Name = line1_.substring(3);
        }

        //int oldPos =(int)memoryStream.Position;
        int oldPos = stringIO.Position;
        line1 = stringIO.ReadLine();
        while (true)
        {
            if (line1 == null)
            {
                section.Content = sectionContent.toString();
                return section;
            }
            line_no++;

            if (line1.startsWith("|| "))
            {
                line2 = stringIO.ReadLine();

                if (line2 == null)
                {
                    //memoryStream.Seek(oldPos, SeekOrigin.Begin);
                    //stringReader.DiscardBufferedData();
                    //stringReader.Seek(oldPos);
                    section.Content = sectionContent.toString();
                    return section;
                }

                if (IsSectionHeaderTemplateMatch(line1, line2))
                {
                    //memoryStream.Seek(oldPos, SeekOrigin.Begin);
                    //stringReader.DiscardBufferedData();
                    stringIO.Seek(oldPos);
                    section.Content = sectionContent.toString();
                    return section;
                }

            }

            sectionContent.append(line1);
            oldPos = (int)stringIO.Position;
            //oldPos = (int)memoryStream.Position;
            line1 = stringIO.ReadLine();
        }



    }
    private boolean IsSectionHeaderTemplateMatch(String line1, String line2)
    {
        line1 = line1.stripTrailing();
        boolean isLine1_match = IsMatch(line1, "^\\|\\| [a-zA-Z0-9_\\- ]+( |\t)*$");
        line2 = line2.stripTrailing();
        boolean isLine2_match = IsMatch(line2, "^={"+String.valueOf(line1.length()) + ",}$");

        return isLine1_match && isLine2_match;
    }




    public boolean IsMatch(String text, String pattern_) {
        Pattern pattern = Pattern.compile(pattern_);
        Matcher matcher = pattern.matcher(text);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    public String ReadAllText(String path) throws Exception {
        File file = new File(path);
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
    }









    public class StringIO {

        private String str = null;
        private int Position = 0;

        public StringIO(String s) {
            str = s;
        }

        public int tell() {
            return Position;
        }

        public void Seek(int p) {
            Position = p;
        }

        public String read() {
            if (str == null) {
                return null;
            } else if (str.isEmpty()) {
                return null;
            } else if (Position < str.length()) {
                Position += 1;
                return String.valueOf(str.charAt(Position - 1));
            } else {
                return null;
            }
        }
        public String ReadLine() throws Exception {
            if (str.length() - Position <= 0)
            {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();

            while (true)
            {
                char c = str.charAt(Position);
                stringBuilder.append(c);
                Position++;
                if (c=='\n')
                {
                    return stringBuilder.toString();
                }

                if (Position == str.length())
                {
                    return stringBuilder.toString();
                }
            }
        }
        public String readUntil(String s) throws  Exception{
            if (s.length() > 1) {
                throw new Exception("string should be 1 char");
            }
            StringBuilder sb = new StringBuilder();
            while (true) {
                String s_ = read();
                if (s_ == null) {
                    return sb.toString();
                } else if (s_.equals(s)) {
                    return sb.toString();
                } else {
                    sb.append(s_);
                }
            }
        }

        public void skip(String[] ss) {
            while (true) {
                String s = read();
                if (s == null) {
                    return;
                }
                if (in(s, ss) == false) {
                    Seek(tell() - 1);
                    return;
                }
            }
        }


        public boolean in(String c, String[] sa) {
            for (int i = 0; i < sa.length; i++) {
                if (sa[i].equals(c)) {
                    return true;
                }
            }
            return false;
        }
    }













    public void read(String str) throws Exception {
        boolean verify_csvtable = true;
        boolean parseDT = true;

        StringIO stream = new StringIO(str);

        String sectionK = null;
        LinkedHashMap<String, Object> sectionD = new LinkedHashMap<String, Object>();

        while (true) {

            String s = stream.read();

            if (s == null) {
                if (sectionK != null) {
                    this.configD.put(sectionK, sectionD);
                }

                break;
            }

            if (stream.in(s, emptySpaceValues)) {
                continue;
            } else if (stream.in(s, commentValues)) {
                stream.Seek(stream.tell() - 1);
                ConfigFile.readComment(stream);
            } else if (s.equals("|")) {
                stream.Seek(stream.tell() - 1);
                String sectionK_ = readSectionHeader(stream);

                if (sectionK != null) {
                    this.configD.put(sectionK, sectionD);
                }
                sectionK = sectionK_;
                sectionD = new LinkedHashMap<String,Object>();
            } else {
                stream.Seek(stream.tell() - 1);
                int streamOff_ = stream.tell();
                String val = stream.readUntil("\n");
                stream.Seek(streamOff_);

                KeyValuePair kvp = parseKeyValue(stream);

                if (sectionK == null) {
                    configD.put(kvp.key, kvp.val);
                } else {
                    sectionD.put(kvp.key, kvp.val);
                }
            }
        }
    }
    public String write() throws Exception {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");

        for (String key : this.configD.keySet()) {
            Object value = configD.get(key);

            if (value instanceof LinkedHashMap) {

                LinkedHashMap<String, Object> sectionD = (LinkedHashMap<String, Object>) value;

                String sh0 = "|| " + key;
                String sh1 = "";
                for (int i = 0; i < sh0.length(); i++) {
                    sh1 += "=";
                }
                stringBuilder.append(sh0 + "\n");
                stringBuilder.append(sh1 + "\n");
                if (true)
                    throw new RuntimeException();
                for (String key_ : sectionD.keySet()) {
                    Object value_ = sectionD.get(key_);

                    if (value_ instanceof JSONObject) {

                    } else if (value_ instanceof List) {

                    } else {

                    }
                }
            } else if (value instanceof JSONObject) {
                stringBuilder.append(key + "=" + ((JSONObject)value).toString(4) + "\n");

            } else if (value instanceof List) {
                stringBuilder.append(key + "=" + "[ ");
                for (int i = 0; i < ((List<String>)value).size(); i++) {
                    if (i == ((List<String>)value).size() - 1) {
                        stringBuilder.append(((List<String>)value).get(i) + " ]\n");
                    } else {
                        stringBuilder.append(((List<String>)value).get(i) + ", ");
                    }
                }
            } else {
                stringBuilder.append(key + "=" + value + "\n");
            }
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public Object get(String key) throws Exception{

        key = key.toLowerCase();
        String[] keyF = key.split("->");

        if (keyF.length > 2) {
            throw new Exception("Key too long");
        }

        String keyF0 = keyF[0].toLowerCase();
        String keyF1 = keyF.length == 2 ? keyF[1].toLowerCase() : null;
        Log.i("abc", keyF0);
        Log.i("abc", keyF1);
        Log.i("abc", "def");
        Object[] keys = configD.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String k = (String)keys[i];
            String k_ = k.toLowerCase();

            if (k_.equals(keyF0)) {

                Object v = configD.get(k);

                if (v instanceof LinkedHashMap) {
                    if (keyF1 == null) {
                        throw new Exception("Invalid key.");
                    }

                    var sectionD = (LinkedHashMap<String, Object>) v;
                    var keys_ = sectionD.keySet().toArray();

                    for (int j = 0; j < keys_.length; j++) {
                        String k0 = (String) keys_[j];
                        String k0_ = k0.toLowerCase();

                        if (k0_.equals(keyF1)) {
                            return sectionD.get(k0);
                        }
                    }
                } else {

                    if (keyF1 != null) {
                        throw new Exception("Invalid key");
                    }

                    return configD.get(k);
                }
            }
        }

        throw new Exception("key not found.");
    }
    public boolean exists(String key) {
        try {
            get(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void readComment(StringIO stream) throws Exception {
        String s = stream.read();
        if (s == null) return;

        if (stream.in(s, commentValues)) {
            stream.readUntil("\n");
        }
    }

    private static String readSectionHeader(StringIO stream) throws Exception {
        String s = stream.readUntil("\n");
        String s_ = stream.readUntil("\n");

        if (s == null || s_ == null) {
            throw new Exception("porablem");
        }

        s = s.strip();
        s_ = s.strip();

        if (s.startsWith("||") == false) {
            throw new Exception();
        }
        if (s.length() < 3) {
            throw new Exception();
        }
        if (s_.length() < s.length()) {
            throw new Exception();
        }

        s = s.substring(2).strip();
        return s;
    }

    private static List<String> parseList(StringIO stream) throws Exception {

        List<String> list_ = new LinkedList<String>();
        int stage = 0;
        String lv = "";

        boolean inQuote = false;

        while (true) {
            stream.skip(new String[]{" ", "\t"});
            String c = stream.read();

            if (c == null) {
                throw new Exception();
            }

            if (stage == 0) {
                if (c.equals("[") == false) throw new Exception();

                stage = 1;
            } else if (stage == 1) {


                if (inQuote == false && c.equals("\"")) {
                    inQuote = true;
                    continue;
                } else if (inQuote == true && c.equals("\"")) {
                    list_.add(lv.strip());
                    lv = "";
                    inQuote = false;
                    stage = 2;
                    continue;
                } else if (inQuote == false && c.equals(",")) {
                    list_.add(lv.strip());
                    lv = "";
                    continue;
                } else if (inQuote == false && c.equals("]")){
                    list_.add(lv.strip());
                    break;
                }
                lv += c;
            } else if (stage == 2) {
                if (c.equals(",")) {
                    stage = 1;
                    continue;
                }
            } else if (c.equals("]")) {
                break;
            }


        }
        return list_;
    }
    private static JSONObject parseJSON(StringIO stream) throws Exception {
        String json_ = "";
        boolean inQuote = false;
        int curlyBrace = 0;

        stream.skip(new String[]{" ", "\t"});
        String c = stream.read();
        if (c == null) {
            throw new Exception();
        }
        if (c.equals("{") == false) {
            throw new Exception();
        }
        json_ += c;
        curlyBrace += 1;

        while (true) {

            c = stream.read();
            if (c == null) {
                throw new Exception();
            }
            if (c.equals("\"")) {
                if (inQuote == false) {
                    inQuote = true;
                } else {
                    inQuote = false;
                }
            }
            if (inQuote == false && c.equals("{")) {
                curlyBrace += 1;
            } else if (inQuote == false && c.equals("}")) {
                curlyBrace -= 1;
            }

            json_ += c;

            if (curlyBrace == 0)
                break;

        }
        Log.i("abc", json_);
        return new JSONObject(json_);
    }
    private static KeyValuePair parseKeyValue(StringIO stream) throws Exception {

        String k = "";
        Object v = null;

        while (true) {
            String c = stream.read();

            if (c == null) {
                break;
            }
            else if (c.equals("=")) {
                break;
            }

            k += c;
        }
        k = k.strip();

        if (k.isEmpty()) {
            throw new Exception("Empty key not allowed.");
        }

        stream.skip(new String[] { " ", "\t" });
        String s = stream.read();
        if (s==null) {
            return new KeyValuePair(k, null);
        }
        stream.Seek(stream.tell() - 1);
        Log.i("abc", s );
        if (s.equals("\"")) {
            throw new Exception();
        }
        else if (s.equals("[")) {
            v = parseList(stream);
        } else if (s.equals("{")) {
            Log.i("abc", "json curlt brace");
            v = parseJSON(stream);
        }
        else {
            v = stream.readUntil("\n").strip();
        }

        return new KeyValuePair(k, v);
    }

    public static class KeyValuePair {
        String key = null;
        Object val = null;

        public KeyValuePair(String key_, Object val_) {
            key = key_;
            val = val_;
        }
    }
    public static <K,V> K getKeyByIndex(LinkedHashMap<K, V> map, int index) {
        K keyValue = null;
        if(index < 0 || index >= map.size()) {
            return keyValue;
        }

        Iterator<K> it = map.keySet().iterator();
        for (int i = 0; i < index; i++) {
            it.next();
        }
        return it.next();
    }
    public void print_(LinkedHashMap<String, Object> dict) {

        for (int i = 0; i< dict.size(); i++) {
            String k = getKeyByIndex(dict, i);
            Object v = dict.get(k);

            if (v instanceof LinkedHashMap) {
                Log.i("abc", "Section \"" + k + "\"");
                print_((LinkedHashMap<String, Object>) v);
            } else if (v instanceof JSONObject) {
                try {
                    Log.i("abc", "\"" + k + "\"=");
                    Log.i("abc", ((JSONObject)v).toString(4));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("abc", v.getClass().toString());
                Log.i("abc", "\"" + k + "\"=\"" + v.toString() + "\"");

            }
        }
    }
    public void print() {
        Log.i("abc", "Printing config.");
        print_(this.configD);
    }


    private static String newLine = "\n";
    private static String[] emptySpaceValues = new String[]{" ", "\t", "\r", "\n"};
    private static String[] boolFalseValues = new String[]{"false", "no", "disable"};
    private static String[] boolTrueValues = new String[]{"true", "yes", "enable"};
    private static String[] commentValues = new String[]{"#"};

}