package com.lexx.security.crypto;

import org.junit.Assert;
import org.junit.Test;

public class TestCrypto {

    @Test
    public void testEncrypt() throws Exception {
        String location = "com/lexx/security/crypto/config-server.jks";
        String password = "changeit";
        String alias = "config-server-key";
        String secret = "mys3cr3t";

        String target_text = "text to test encryption on";

        Crypto crypto = new Crypto();
        crypto.setLocation(location);
        crypto.setPassword(password);
        crypto.setAlias(alias);
        crypto.setSecret(secret);

        byte[] encryptedBytes = crypto.encrypt(target_text);
        Assert.assertNotEquals(0, encryptedBytes.length);
        System.out.println(encryptedBytes);
        String decrypted = crypto.decrypt(encryptedBytes);
        Assert.assertNotNull(decrypted);

        Assert.assertEquals(target_text, decrypted);
    }
}
