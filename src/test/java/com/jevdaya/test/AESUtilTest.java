package com.jevdaya.test;
import org.junit.jupiter.api.Test;

import com.jevday.util.AESUtil;

import static org.junit.jupiter.api.Assertions.*;

class AESUtilTest {

    // ✅ 1. Test Encrypt + Decrypt (Happy Path)
    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        String original = "HelloWorld123";

        String encrypted = AESUtil.encrypt(original);
        String decrypted = AESUtil.decrypt(encrypted);

        assertNotNull(encrypted);
        assertNotEquals(original, encrypted); // encrypted should differ
        assertEquals(original, decrypted);    // decrypted should match original
    }

    // ✅ 2. Test Empty String
    @Test
    void shouldHandleEmptyString() {
        String original = "";

        String encrypted = AESUtil.encrypt(original);
        String decrypted = AESUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    // ✅ 3. Test Special Characters
    @Test
    void shouldHandleSpecialCharacters() {
        String original = "!@#$%^&*()_+123ABCxyz";

        String encrypted = AESUtil.encrypt(original);
        String decrypted = AESUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    // ✅ 4. Test Null Input (Encrypt)
    @Test
    void shouldThrowExceptionWhenEncryptingNull() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            AESUtil.encrypt(null);
        });

        assertTrue(exception.getMessage().contains("Error encrypting"));
    }

    // ✅ 5. Test Invalid Base64 Input (Decrypt)
    @Test
    void shouldThrowExceptionWhenDecryptingInvalidData() {
        String invalidData = "invalid-base64";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            AESUtil.decrypt(invalidData);
        });

        assertTrue(exception.getMessage().contains("Error decrypting"));
    }
}
