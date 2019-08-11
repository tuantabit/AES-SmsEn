package com.example.aesrajat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.ConditionVariable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {


    EditText encrytext, encrypassword, encrytedText, password0;
    TextView output, Outputtext0;
    Button encrytbtn, decode;
    String AES = "AES";
    String outputString;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    EditText textPhoneNo;
    String msg, phoneNo,themsgstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrytext = (EditText) findViewById(R.id.textencryt);
        encrypassword = (EditText) findViewById(R.id.passwordencryt);
        password0 = (EditText) findViewById(R.id.Password0);
        encrytedText = (EditText) findViewById(R.id.ecrytedText);

        output = (TextView) findViewById(R.id.Outputtext);
        Outputtext0 = (TextView) findViewById(R.id.Outputtext0);
        encrytbtn = (Button) findViewById(R.id.encrytbtn);
        decode = (Button) findViewById(R.id.decode);
        textPhoneNo = findViewById(R.id.phone);


        //check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            //if permission is not granted then check if the user has denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
            {

            }
            else
            {
                //a pop up will appear asking for required permission i.e. allow or deny
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }

            //part AESCrypt 2:
            String password = "password";
            String message = "hello world";
            String encrypted = "2B22cS3UC5s35WBihLBo8w";

            if (BuildConfig.DEBUG) {
                AESCrypt.DEBUG_LOG_ENABLED = true;
            }

            String encryptedMsg = null;
            try {
                encryptedMsg = AESCrypt.encrypt(password, message);
            }catch (GeneralSecurityException e){
                //   fail("error occurred during encrypt");
                e.printStackTrace();
            }

        String messageAfterDecrypt = null;
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, encrypted);

        }catch (GeneralSecurityException e){
          //  fail("error occurred during Decrypt");
            e.printStackTrace();
        }
        }


        encrytbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputString = encrytText(encrytext.getText().toString(), encrypassword.getText().toString());
                    themsgstring=outputString;
                    output.setText(outputString);
                    sendTextMessagePlz();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//filtering the messages :
        String s = "AES lbkabjdfalksbdflajskdnfpwj3r23/sdfd==";
        String ss = Character.toString(s.charAt(0));
        String ss1 = Character.toString(s.charAt(1));
        String ss2 = Character.toString(s.charAt(2));

        Toast.makeText(this, ss + ss1 + ss2, Toast.LENGTH_SHORT).show();
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.deleteCharAt(0);
        stringBuilder.deleteCharAt(0);
        stringBuilder.deleteCharAt(0);
        stringBuilder.deleteCharAt(0);
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String outputString0 = decrytText(encrytedText.getText().toString(), password0.getText().toString());
                    Outputtext0.setText(outputString0);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }


    //after getting the result of permission request, the result will be passed through this method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        //will check the requestCode
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Thanks for permitting!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Why didn't you permit me idiot!", Toast.LENGTH_LONG).show();
                }
            }
        }//switch
    }//method

    protected void sendTextMessagePlz()
    {
        msg = themsgstring;
        phoneNo = textPhoneNo.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(this, "Sent!", Toast.LENGTH_LONG).show();
    }




    private String decrytText(String stringtobedecryted, String passwordofencrytedText) throws Exception {

        SecretKeySpec key = generateKey(passwordofencrytedText);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodevalue = Base64.decode(stringtobedecryted, Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodevalue);
        String decrytedValue = new String(decvalue);
        return decrytedValue;
    }

    private String encrytText(String texttoencryt, String passwordofencrytText) throws Exception {

        SecretKeySpec key = generateKey(passwordofencrytText);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(texttoencryt.getBytes());
        String encrytvalue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encrytvalue;
    }

    private SecretKeySpec generateKey(String passwordofencrytText0) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = passwordofencrytText0.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
