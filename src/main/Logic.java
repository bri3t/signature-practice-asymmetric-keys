package main;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

/**
 *
 * @author arnau
 */
public class Logic {


    private static final String RED = "\u001B[31m";   
    private static final String GREEN = "\u001B[32m";   
    private final String RESET = "\033[0m";

    String inputFile;
    String keystore;
    private final char[] KSPASSWORD = "123456".toCharArray();
    String alias;
    String sigfile;
    String action;
    PrivateKey privateKey;
    PublicKey publicKey;

    public void startMethod(Map<String, String> options) {
        cargarKeyStore(options);
        switch (options.get("action")) {
            case "1" -> { // sign
                sign();
            }
            default -> { // verify
                checkSigFile();
                verify(fileToByteArray(new File(inputFile)), fileToByteArray(new File(sigfile)));
            }

        }
    }

    public void cargarKeyStore(Map<String, String> options) {
        try {
            inputFile = options.get("inputfile");
            keystore = options.get("keystore");
            alias = options.get("alias");
            sigfile = options.get("sigfile");
            action = options.get("action");

            // Cargar el almacén de claves (keystore)
            KeyStore ks = KeyStore.getInstance("JKS");
            File ksFile = new File(keystore);
            if (!ksFile.isFile()) {
                System.out.println(RED + "La keyStore no existe" + RESET); 
                System.exit(0);
            }

            try ( FileInputStream keyStoreStream = new FileInputStream(ksFile)) {
                ks.load(keyStoreStream, KSPASSWORD);
            }

            if (action.equals("1")) {

                Key key = ks.getKey(alias, KSPASSWORD);
                if (key != null) {
                    if (key instanceof PrivateKey) {
                        privateKey = (PrivateKey) key;
                    }
                } else {
                    System.out.println(RED + "No tienes la clave privada para este alias"  + RESET); 
                    System.exit(0);
                }
            } else {

                if (ks.getCertificate(alias) == null) {
                    System.out.println(RED + "El alias no es correcto, por lo que no se puede obtener la clave publica" + RESET); 
                    System.exit(0);
                }
                publicKey = ks.getCertificate(alias).getPublicKey();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndSignFIle(byte[] data) {

        File file = new File(sigfile);
        // Escribir la firma en el archivo de salida
        try ( FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println(GREEN + "Archivo firmado y guardado en: " + sigfile + RESET);
    }

    private void checkSigFile() {
        // Determinar el nombre del archivo de salida si no se proporciona
        if (sigfile.equals("") || sigfile.isEmpty()) {
            sigfile = inputFile.substring(0, inputFile.lastIndexOf('.')) + ".md5";
        }
    }

    public void sign() {

        try {
            // Crear una instancia de Signature con MD5 y RSA
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(fileToByteArray(new File(inputFile)));

            // Generar la firma
            byte[] digitalSignature = signature.sign();

            // Determinar el nombre del archivo de salida si no se proporciona
            checkSigFile();

            createAndSignFIle(digitalSignature);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verify(byte[] data, byte[] firma) {
        boolean isValid = false;

        try {
            Signature signer = Signature.getInstance("MD5withRSA");
            signer.initVerify(publicKey);
            signer.update(data);
            isValid = signer.verify(firma);
        } catch (Exception ex) {
        }

        if (isValid) {
            System.out.println(GREEN + "LA FIRMA HA SIDO VALIDADA" + RESET);
        } else {
            System.out.println(RED + "LA FIRMA NO HA SIDO VALIDADA" + RESET);
        }

    }

    private byte[] fileToByteArray(File arxiu) {

        byte[] arxiuBytes = new byte[(int) arxiu.length()];

        try ( FileInputStream inputStream = new FileInputStream(arxiu)) {
            inputStream.read(arxiuBytes);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return arxiuBytes;
    }

}
