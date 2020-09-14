
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConversionProcess {

    public static boolean isDefaultLanguage = false;
    public static boolean pass = false;
    public static boolean longTextDetected = false;
    public static boolean longLanguageDetected = false;
    public static boolean longTextStart = false;
    public static boolean longHardPass = false;
    public static boolean longTextEnd = false;
    public static boolean filePathError = false;
    public static String body;
    public static String defaultLanguage = "en";
    public static String text;
    public static String finishLongText;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static File file;
    public static FileReader fileReader;

    public List<String> getData(String yamlPath) throws IOException {
        List<String> list = new ArrayList<>();
        file = new File(yamlPath);

        if (list.size() == 0 && file.exists()) {
            fileReader = new FileReader(yamlPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                list.add(str);
            }
            fileReader.close();
            filePathError = false;
        } else {
            System.out.println(ANSI_RED + "The file path is incorrect" + ANSI_RESET);
            filePathError = true;
        }

        return list;
    }

    public void convertYaml(String selectLanguage, String yamlPath, String saveYamlPath) throws IOException {
        List<String> getList;
        getList = getData(yamlPath);
        try {
            FileWriter myWriter = new FileWriter(saveYamlPath);
            for (String item : getList) {
                if (item != null && item != "" && filePathError == false) {
                    //if it finds text to translate it will enter this block
                    if (((item.contains("x-") || item.contains("«x»-")) && item.contains("-i18n")) && longLanguageDetected == false) {
                        //if the length of the text is short
                        if (item.contains("x-")) {
                            //if selected language is default
                            if (selectLanguage.equalsIgnoreCase(defaultLanguage)) {
                                isDefaultLanguage = true;
                                item = item.replaceAll("x-", "");
                                item = item.replaceAll("-i18n", "");
                                pass = false;
                            } else {
                                isDefaultLanguage = false;
                                body = item.substring(0, item.indexOf("-i18n")) + ": ";
                                body = body.replaceAll("x-", "");
                                pass = true;
                            }
                        } else {
                            finishLongText = null;
                            item = item.replaceAll("«x»-", "");
                            longTextDetected = true;
                            finishLongText = item.replaceAll("-i18n", "");
                            pass = true;
                        }
                    } else {
                        pass = false;
                    }
                    //If the text is short, it receives the message of the selected language under the lines to be translated
                    if (item.contains("x_")) {
                        if (item.contains("x_" + selectLanguage)) {
                            text = item.substring(item.indexOf("x_" + selectLanguage + ": ") + 6, item.length());
                            item = body + text;
                            pass = false;
                        } else if (item.contains("x_")) {
                            pass = true;
                        } else {
                            pass = false;
                        }
                    }
                    //It enters when it detects a long translation
                    if (longTextDetected) {
                        //If default language is selected
                        if (isDefaultLanguage) {
                            //Is the long text starting?
                            if (item.contains("»x»") && longHardPass == false) {
                                longTextStart = true;
                                longTextEnd = false;
                                pass = true;
                            }//Is the long text ending?
                            else if (item.contains("«x«") && longHardPass == false) {
                                longTextStart = false;
                                longTextEnd = true;
                                pass = false;
                            }
                            //Is the long text starting?
                            if (longTextStart && item.contains("»x»") == false) {
                                finishLongText = finishLongText + "\n" + item;
                                pass = true;
                            }//Is the long text ending?
                            else if (longTextEnd || item.contains("«x«") && longHardPass == false) {
                                pass = false;
                                longTextStart = false;
                                longTextEnd = false;
                                item = finishLongText;

                            }// if the default language is selected, pass all the translations below
                            else if (item.contains("«x»_") || longHardPass) {
                                if (item.contains("«x«")) {
                                    pass = false;
                                    longHardPass = false;
                                } else {
                                    pass = true;
                                    longHardPass = true;
                                }
                                item = null;
                            }
                        }
                        // If a different language is selected
                        else {
                            //If it finds the translation in the selected language, the variable longLanguageDetected will be true.
                            if (item.contains("«x»_" + selectLanguage)) {
                                longLanguageDetected = true;
                            } else {
                                pass = true;
                            }
                            //If the desired language has been detected
                            if (longLanguageDetected) {
                                //Is the long text starting?
                                if (item.contains("»x»")) {
                                    longTextStart = true;
                                    longTextEnd = false;
                                    pass = true;
                                }//Is the long text ending?
                                else if (item.contains("«x«")) {
                                    longTextStart = false;
                                    longTextEnd = true;
                                    pass = false;
                                }
                                //If long text is starting enter
                                if (longTextStart) {
                                    if (item.contains("»x»")) {
                                        pass = true;
                                    } else {
                                        finishLongText = finishLongText + "\n" + item;
                                        finishLongText.replaceAll("»x»", "");
                                        pass = true;
                                    }
                                }//Is the long text ending?
                                else if (longTextEnd) {
                                    longTextStart = false;
                                    longTextEnd = false;
                                    longLanguageDetected = false;
                                    item = finishLongText;
                                    longHardPass = true;
                                    pass = false;

                                }// skip other translations
                                else {
                                    if (item.contains("«x«")) {
                                        pass = false;
                                        longHardPass = false;
                                    } else {
                                        pass = true;
                                    }
                                }
                            } else {
                                if (item.contains(">x<")) {
                                    longTextDetected = false;
                                    pass = false;
                                    item = null;
                                }
                            }
                        }
                    }
                    if (pass == false && item != null && item.contains(">x<") == false) {
                        myWriter.write(item + "\n");
                        text = null;
                    }
                }
            }
            if (filePathError == false) {
                myWriter.close();
                System.out.println(ANSI_GREEN + "Successful:" + ANSI_RESET + " Yaml file translated successfully\n" + ANSI_BLUE + "Yaml file path: " + ANSI_RESET + saveYamlPath);
            }
            else{
                file = new File(saveYamlPath);
                file.deleteOnExit();
            }
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error:" + ANSI_RESET + "An error occurred.");
        }
    }
}
