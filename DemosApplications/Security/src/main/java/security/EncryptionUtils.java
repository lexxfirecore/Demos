package security;

import org.springframework.cloud.config.server.encryption.EncryptionController;
import org.springframework.cloud.config.server.encryption.KeyStoreTextEncryptorLocator;
import org.springframework.cloud.config.server.encryption.TextEncryptorLocator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.rsa.crypto.RsaAlgorithm;
import org.springframework.security.rsa.crypto.RsaSecretEncryptor;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.LinkedHashMap;
import java.util.Map;


public final class EncryptionUtils {

    private static String keyLocation;
    private static String keyAlias;
    private static String password;
    private static String secret;

    private static final String ALGORITHM = "RSA";  // "SHA1PRNG", "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String SALT = "deadbeef";
    private static final String ESCAPE = "{plain}";
    private static KeyPair keys = null;

    private static KeyPair retrieveKeys() throws KeyStoreException, IOException, UnrecoverableKeyException, NullPointerException {
        InputStream is = null;
        try {
            // load keys from file
            is = EncryptionUtils.class.getClassLoader().getResourceAsStream(keyLocation);
            if (is == null) {
                throw new NullPointerException(ExceptionMessage.KEYS_BAD_LOCATION_OR_WRONG_PATH);
            }
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, password.toCharArray());

            if (keyStore.containsAlias(keyAlias)) {
                Certificate certificate = keyStore.getCertificate(keyAlias);
                PublicKey publicKey = certificate.getPublicKey();
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, secret.toCharArray());

                keys = new KeyPair(publicKey, privateKey);
            } else {
                throw new KeyStoreException();
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return keys;
    }

    public static byte[] simpleEncrypt(String textToEncrypt) throws Exception {

        checkParameters();
        byte[] encryptedBytes = null;
        PublicKey publicKey = null;
        try {
            publicKey = retrieveKeys().getPublic();
        } catch (NullPointerException e) {
            throw new NullPointerException(ExceptionMessage.KEYS_BAD_LOCATION_OR_WRONG_PATH);
        } catch (KeyStoreException e) {
            throw new KeyStoreException(ExceptionMessage.KEYS_COULD_NOT_RETRIEVE);
        } catch (IOException e) {
            throw new KeyStoreException(ExceptionMessage.KEYS_BAD_PASSWORD);
        } catch (UnrecoverableKeyException e) {
            throw new KeyStoreException(ExceptionMessage.KEYS_BAD_SECRET);
        }

        final Cipher cipher = Cipher.getInstance(ALGORITHM);                // get an RSA cipher object and print the provider
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);                        // simpleEncrypt the plain text using the public key
        encryptedBytes = cipher.doFinal(textToEncrypt.getBytes());
        return encryptedBytes;
    }

    public static String simpleDecrypt(byte[] bytesToDecrypt) throws Exception {
        checkParameters();
        final Cipher cipher = Cipher.getInstance(ALGORITHM);                // get an RSA cipher object and print the provider
        cipher.init(Cipher.DECRYPT_MODE, retrieveKeys().getPrivate());      // simpleDecrypt the text using the private key
        byte[] decryptedText = cipher.doFinal(bytesToDecrypt);
        return new String(decryptedText);
    }

    public static String springEncrypt(String textToEncrypt) throws Exception {
        checkParameters();
        ClassPathResource classPathResource = new ClassPathResource(getKeyLocation());
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, password.toCharArray());
        TextEncryptorLocator textEncryptorLocator = new KeyStoreTextEncryptorLocator(keyStoreKeyFactory, secret, keyAlias);
        EncryptionController encryptionController = new EncryptionController(textEncryptorLocator);
        String decrypted = encryptionController.encrypt(textToEncrypt, MediaType.APPLICATION_FORM_URLENCODED);
        return new String(decrypted);
    }

    public static String springDecrypt(byte[] bytesToDecrypt) throws Exception {
        checkParameters();
        ClassPathResource classPathResource = new ClassPathResource(getKeyLocation());
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, password.toCharArray());
        TextEncryptorLocator textEncryptorLocator = new KeyStoreTextEncryptorLocator(keyStoreKeyFactory, secret, keyAlias);
        EncryptionController encryptionController = new EncryptionController(textEncryptorLocator);
        String textToDecryptString = new String(bytesToDecrypt);
        String decrypted = encryptionController.decrypt(textToDecryptString, MediaType.APPLICATION_FORM_URLENCODED);
        return new String(decrypted);
    }

    public static String springDecryptAdapted(byte[] bytesToDecrypt) throws Exception {

        String input = stripFormData(new String(bytesToDecrypt), MediaTypes.APPLICATION_FORM_URLENCODED, true);
        Map<String, String> encryptorKeys = getEncryptorKeys("application", "default", input);
        TextEncryptor encryptor = locate(encryptorKeys);
        String encryptedText = stripPrefix(input);
        String decrypted = encryptor.decrypt(encryptedText);

        return new String(decrypted);
    }

    private static String stripFormData(String data, String mediaType, boolean cipher) {
        if (data.endsWith("=") && !mediaType.equals(MediaTypes.TEXT_PLAIN)) {
            try {
                data = URLDecoder.decode(data, "UTF-8");
                if (cipher) {
                    data = data.replace(" ", "+");
                }
            } catch (UnsupportedEncodingException e) {
                // Really?
            }
            String candidate = data.substring(0, data.length() - 1);
            if (cipher) {
                if (data.endsWith("=")) {
                    if (data.length() / 2 != (data.length() + 1) / 2) {
                        try {
                            Hex.decode(candidate);
                            return candidate;
                        } catch (IllegalArgumentException e) {
                            if (Base64.isBase64(data.getBytes())) {
                                return data;
                            }
                        }
                    }
                }
                return data;
            }
            data = candidate;   // User posted data with content type form but meant it to be text/plain
        }
        return data;
    }


    private static Map<String, String> getEncryptorKeys(String name, String profiles, String text) {


        Map<String, String> keys = new LinkedHashMap<String, String>();

        text = removeEnvironmentPrefix(text);
        keys.put("name", name);
        keys.put("profiles", profiles);

        if (text.contains(ESCAPE)) {
            text = text.substring(0, text.indexOf(ESCAPE));
        }

        String[] tokens = StringUtils.split(text, "}");
        while (tokens != null) {
            String token = tokens[0].trim();
            if (token.startsWith("{")) {
                String key = "";
                String value = "";
                if (token.contains(":") && !token.endsWith(":")) {
                    key = token.substring(1, token.indexOf(":"));
                    value = token.substring(token.indexOf(":") + 1);
                } else {
                    key = token.substring(1);
                }
                keys.put(key, value);
            }
            text = tokens[1];
            tokens = StringUtils.split(text, "}");
        }

        return keys;

    }

    private static TextEncryptor locate(Map<String, String> keys) throws UnrecoverableKeyException, KeyStoreException, IOException {


        final String KEY = "key";
        final String SECRET = "secret";
        String alias = keys.containsKey(KEY) ? keys.get(KEY) : keyAlias;
        String secret = keys.containsKey(SECRET) ? keys.get(SECRET) : getSecret();


        return new RsaSecretEncryptor(retrieveKeys(), RsaAlgorithm.DEFAULT, SALT);
    }

    private static String stripPrefix(String value) {
        if (!value.contains("}")) {
            return value;
        }
        if (value.contains(ESCAPE)) {
            return value.substring(value.indexOf(ESCAPE) + ESCAPE.length());
        }
        return value.substring(value.lastIndexOf("}") + 1);
    }

    private static String removeEnvironmentPrefix(String input) {
        return input.replaceFirst("\\{name:.*\\}", "").replaceFirst("\\{profiles:.*\\}",
                "");
    }

    private static void checkParameters() throws Exception {
        if (keyLocation == null || password == null || keyAlias == null || secret == null) {
            throw new Exception(ExceptionMessage.PARAM_WRONG);
        }
    }

    private static class MediaTypes {
        public final static String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public final static String TEXT_PLAIN = "text/plain";


    }

    public static String getKeyLocation() {
        return keyLocation;
    }

    public static void setKeyLocation(String keyLocation) {
        EncryptionUtils.keyLocation = keyLocation;
    }

    public static String getKeyAlias() {
        return keyAlias;
    }

    public static void setKeyAlias(String keyAlias) {
        EncryptionUtils.keyAlias = keyAlias;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        EncryptionUtils.password = password;
    }

    public static String getSecret() {
        return secret;
    }

    public static void setSecret(String secret) {
        EncryptionUtils.secret = secret;
    }
}


