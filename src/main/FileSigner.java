package main;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FileSigner {

    private static final String[] validActions = {"sign", "verify", "help"};
    private final String[] args;
    private String action = "";
    private String inputFile = "";
    private String sigfile = "";
    private String keystore = "keystorage.jks";
    private String alias = "";
    String toDo = "";
    Logic logica = new Logic();
    HashMap<String, String> mapToDo = new HashMap<>();


    public FileSigner(String[] args) {
        this.args = args;
        if (!processArgs()) {
            printHelp();
        } else {
            logica.startMethod(mapToDo);
        }
    }

    private boolean processArgs() {

        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            System.out.println("Help:");
            return false;
        }

        if (args.length < 3 || args.length > 5) {
            String text = args.length < 3 ? "Missing args" : "Too much args";
            System.out.println(text);
            return false;
        }
        action = args[0];
        inputFile = args[1];

        if (!checkActions(action)) {
            return false;
        }
        if (!checkFile(inputFile)) {
            System.out.println("Invalid file: " + inputFile);
            return false;
        }

        if (!checkAlias(args)) {
            System.out.println("Missing ALIAS");
            return false;
        }

        for (int i = 2; i < args.length; i++) {
            if (!checkOptions(args[i])) {
                return false;
            }
        }

        if (!checkNotSameOptions()) {
            System.out.println("No pueden haber dos opciones iguales");
            return false;
        }

        mapToDo.put("sigfile", sigfile);
        mapToDo.put("alias", alias);
        mapToDo.put("keystore", keystore);
        mapToDo.put("inputfile", inputFile);
        mapToDo.put("action", toDo);

        return true;
    }

    private boolean checkActions(String action) {
        // Check if the action is "help" and return false if it is
        if (action.equalsIgnoreCase("help")) {
            System.out.println("Invalid syntax usage");
            return false;
        }
        // Continue to check if the action is contained in the validActions array
        boolean result = Arrays.asList(validActions).contains(action);

        if (!result) {
            System.out.println("El action esta mal");
        } else {
            toDo = action.equals("sign") ? "1" : (action.equals("verify") ? "2" : "0");
        }

        return result;
    }

    private boolean checkFile(String fileName) {
        return new File(fileName).isFile();
    }

    private boolean checkAlias(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--alias=") && arg.length() > 8) {
                alias = arg;
                return true;
            }
        }
        return false;
    }

    private boolean checkOptions(String arg) {
        if (arg.startsWith("--sigfile=")) {
            if (!arg.endsWith(".md5")) {
                System.out.println("Invalid option: " + arg);
                return false;
            }
            sigfile = arg.substring(10);
            return true;
        }
        if (arg.startsWith("--keystore=")) {
             if (!arg.endsWith(".jks")) {
                System.out.println("Invalid option: " + arg);
                return false;
            }
            keystore = arg.substring(11);
            return true;
        }
        if (arg.startsWith("--alias=")) {
            alias = arg.substring(8);
            return true;
        }
        System.out.println("Invalid option: " + arg);
        return false;
    }

    private boolean checkNotSameOptions() {
        Set<String> uniqueKeys = new HashSet<>();
        boolean result = true;

        for (int i = 2; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--sigfile=") || arg.startsWith("--keystore=") || arg.startsWith("--alias=")) {
                String key = arg.split("=")[0];
                if (!uniqueKeys.add(key)) {
                    result = false;  // Se encontró un tipo de argumento duplicado
                }
            }
        }

        return result;  // No se encontraron tipos de argumentos duplicados
    }

    private void printHelp() {
        System.out.println("\n----------------------------------------------------------------------------------------------------------");
        System.out.println("Usage: java main.FileSigner <action> <inputFile> [--sigfile=sigFile] [--keystore=keystoreFile] [--alias=alias]");
        System.out.println("Actions:");
        for (String action : validActions) {
            System.out.println(" - " + action);
        }
        System.out.println("Options:");
        System.out.println(" --sigfile=  Specify the output file for the signature");
        System.out.println(" --keystore= Specify the keystore file");
        System.out.println(" --alias=    Specify the alias");
        System.out.println("----------------------------------------------------------------------------------------------------------");

    }

    public static void main(String[] args) {
        new FileSigner(args);
    }
}
