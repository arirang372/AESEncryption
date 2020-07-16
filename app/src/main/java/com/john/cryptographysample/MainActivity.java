package com.john.cryptographysample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private EditText clear_text;
    private Button encrypt_button;
    private TextView encrypted_text;

    private static final String SECRET_KEY = "mySecretKey12345"; //needs to be 16 bytes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clear_text = findViewById(R.id.clear_text);
        encrypt_button = findViewById(R.id.encrypt_button);
        encrypted_text = findViewById(R.id.encrypted_text);

        encrypt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clearText = clear_text.getText().toString();
                String encrypted = encrypt(clearText, SECRET_KEY);
                encrypted_text.setText(encrypted);
            }
        });
    }

    public String encrypt(String strToEncrypt, String secret) {
        try {
            SecretKeySpec keySpec = getKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public SecretKeySpec getKey(String myKey) {
        SecretKeySpec secretKey = null;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public String decrypt(String strToDecrypt, String secret) {
        try {
            SecretKeySpec keySpec = getKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private static final String HMAC_SHA512 = "HmacSHA512";

    public byte[] calculateHMAC(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA512);
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}