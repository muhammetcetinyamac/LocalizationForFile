import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ConversionProcess conversionProcess = new ConversionProcess();
        String apiPath = System.getProperty("user.dir");
        apiPath = apiPath.substring(0, apiPath.length() - 5);
        apiPath += "/api/";
        String selectYaml = args[0];
        String language = args[1];
        selectYaml = selectYaml.trim();
        language = language.trim();
        String newYamlName = selectYaml.replaceAll(".yaml", "_" + language + ".yaml");

        if (selectYaml.equalsIgnoreCase(null) && language.equalsIgnoreCase(null)) {
            System.out.println("Please specify the correct language and yaml path to translate.");
        } else {
            conversionProcess.convertYaml(language, apiPath + selectYaml, apiPath + newYamlName);
        }
    }
}
