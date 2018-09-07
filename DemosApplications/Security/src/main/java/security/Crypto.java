package security;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public final class Crypto {

    private String location;
    private String password;
    private String alias;
    private String secret;

    private static final String ALGORITHM = "RSA";
    private static final boolean needToGenerateKeys = false;
    private static KeyPair keys = null;

    private KeyPair retrieveKeys() {

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(location);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, password.toCharArray());

            if (keyStore.containsAlias(alias)) {
                Certificate certificate = keyStore.getCertificate(alias);
                PublicKey publicKey = certificate.getPublicKey();
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, secret.toCharArray());
                keys = new KeyPair(publicKey, privateKey);
            }

            if (keys == null) {
                if (needToGenerateKeys) {
                    final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
                    keyPairGenerator.initialize(1024);
                    keys = keyPairGenerator.generateKeyPair();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public byte[] encrypt(String textToEncrypt) throws Exception {

        if (location == null || password == null || alias == null || secret == null) {
            throw new Exception("One of parameters encryption parameters are not set properly.");
        }

        byte[] encryptedBytes = null;
        PublicKey publicKey = retrieveKeys().getPublic();


        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(textToEncrypt.getBytes());

        return encryptedBytes;
    }

    public String decrypt(byte[] textToDecryptBytes) throws Exception {

        if (location == null || password == null || alias == null || secret == null) {
            throw new Exception("One of parameters encryption parameters are not set properly.");
        }

        PrivateKey privateKey = retrieveKeys().getPrivate();
        byte[] decryptedText = null;

        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);

        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedText = cipher.doFinal(textToDecryptBytes);

        return new String(decryptedText);
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
