
package security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.security.KeyStoreException;

public class TestEncryptionUtils {
    String keyLocation = "security/config-server.jks";
    String keyAlias = "config-server-key";
    String password = "changeit";
    String secret = "mys3cr3t";
    String bad_value = "very_very_wrong_text";
    String smallText = "text to test encryption on";
    static byte[] encryptedBytes = null;
    // ecrypted secret
    String bigEncryptedText = "{cipher}AgBWF72OVUNofsXNTM2FPOFFIWcW8zi2hdS7COq041hnUGtxL160qqCFb1Vn9kWjTKCOtzKO5v0biOmT47m7xu9mcLaqCV+/8upNnLGrWiglnzOz1iY7izoJ2vThL5PVbdubo8QldR9v/6Hn/kwzpt3FfPCLJGaDnr4siPksO+T6LVmyL8azvGajExRZh7tyCOE/hfLEY8tZroBOphBRXCh04jLZjMaR8y+MbiNxd289CmidgBp8dEjVHownXmmpt35C1c9xM6IjqwTb1x1giMAP7aOcxVKKCQVqro96R5UbOZlFS6nrb1Zxwm3Btsx4LLTqVs7fGEFTUrL+mXW4qkkK6fskSlu+ov3Xb7H5883epPUiiIRS27Y+GkOBHWVT9CNKtDmqgi+yiY2VE2GzBmdziTlTWU3k3EA2KQg0NeN/Jjr565c1sVE/iBNGsObI/Eix9f4JA+2bExeaAuE5Ee374jGrbVdHhnP+mTCzZ7MYCGmzFt5pAN7gdciYyufyGO5kq4icR4TZzIF0R5D263O7+JdJaa33K5IjK1ppg5Td0CMQp5AjPYPb5422NtRhbO/UF/wyroPC7QKXqzPWHbvMr0ClT2+eDulEnBh/vWVL4RVWZxYy+rX4P4UMj9mMvHj6lRXiWntoPMJUmXfZNlv5pOvdXra5Wt4CsoQm+AqzsIKdlJIMt8Akepe2CEw9Rc5hVCrNrG9bx1zk8JJMoCzX";


    @Before
    public void init() {
        System.out.println("init all parameters.");
        EncryptionUtils.setKeyLocation(keyLocation);
        EncryptionUtils.setPassword(password);
        EncryptionUtils.setKeyAlias(keyAlias);
        EncryptionUtils.setSecret(secret);
    }

    @Test
    public void testSimpleEncryptDecryptPositive() throws Exception {
        System.out.println("run all good parameters test ...");
        encryptedBytes = EncryptionUtils.simpleEncrypt(smallText);
        Assert.assertNotEquals(0, encryptedBytes.length);
        String decrypted = EncryptionUtils.simpleDecrypt(encryptedBytes);
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(smallText, decrypted);
    }

    @Test
    public void testEncryptPositive() throws Exception {
        System.out.println("run simpleEncrypt with good parameters test ...");
        encryptedBytes = EncryptionUtils.simpleEncrypt(smallText);
        Assert.assertNotNull(encryptedBytes);
        Assert.assertEquals(512, encryptedBytes.length);
        System.out.println(encryptedBytes);
    }

    @Test
    public void testDecryptPositive() throws Exception {
        System.out.println(encryptedBytes);
        System.out.println("run simpleDecrypt with good parameters test ...");
        String decrypted = EncryptionUtils.simpleDecrypt(encryptedBytes);
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(smallText, decrypted);
    }

    @Test
    public void testEncryptNegative() throws Exception {

        try {
            System.out.println("run bad location test ...");
            EncryptionUtils.setKeyLocation(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (NullPointerException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_LOCATION_OR_WRONG_PATH, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad alias test ...");
            EncryptionUtils.setKeyAlias(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_COULD_NOT_RETRIEVE, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad password test ...");
            EncryptionUtils.setPassword(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_PASSWORD, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad secret test ...");
            EncryptionUtils.setSecret(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_SECRET, e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testDecryptNegative() throws Exception {


        try {
            System.out.println("run bad location test ...");
            EncryptionUtils.setKeyLocation(bad_value);
            EncryptionUtils.simpleDecrypt(encryptedBytes);
        } catch (NullPointerException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_LOCATION_OR_WRONG_PATH, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad alias test ...");
            EncryptionUtils.setKeyAlias(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_COULD_NOT_RETRIEVE, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad password test ...");
            EncryptionUtils.setPassword(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_PASSWORD, e.getMessage());
        }

        init();
        try {
            System.out.println("run bad secret test ...");
            EncryptionUtils.setSecret(bad_value);
            EncryptionUtils.simpleEncrypt(smallText);
        } catch (KeyStoreException e) {
            Assert.assertEquals(ExceptionMessage.KEYS_BAD_SECRET, e.getMessage());
        }
    }


    @Test
    public void testBestDecrypt() throws Exception {


        // adapted
        String encrypted = EncryptionUtils.springEncrypt(smallText);
        System.out.println(new String(encrypted));

        String decrypted = EncryptionUtils.springDecryptAdapted(encrypted.getBytes());
        System.out.println(decrypted);
        Assert.assertEquals(smallText, decrypted);

        String decryptedBigText = EncryptionUtils.springDecryptAdapted(bigEncryptedText.getBytes()); // ALGORITHM = "RSA"
        Assert.assertNotNull(decryptedBigText);
        Assert.assertEquals(secret, decryptedBigText);
    }


    @Test
    public void testSpringEncryptDecryptPositive() throws Exception {
        System.out.println("run all good parameters test ...");
        String encrypted = EncryptionUtils.springEncrypt(smallText);
        Assert.assertNotEquals(0, encrypted.length());
        String decrypted = EncryptionUtils.springDecrypt(encrypted.getBytes());
        Assert.assertNotNull(decrypted);
        Assert.assertEquals(smallText, decrypted);
    }

    @Test
    public void testSpringDecrypt() throws Exception {

        // full
        String encrypted = EncryptionUtils.springEncrypt(smallText);
        System.out.println(new String(encrypted));

        String decrypted = EncryptionUtils.springDecrypt(encrypted.getBytes());
        System.out.println(decrypted);
        Assert.assertEquals(smallText, decrypted);

        String decryptedBigText = EncryptionUtils.springDecrypt(bigEncryptedText.getBytes()); // ALGORITHM = "RSA"
        Assert.assertNotNull(decryptedBigText);
        Assert.assertEquals(secret, decryptedBigText);
    }
}
