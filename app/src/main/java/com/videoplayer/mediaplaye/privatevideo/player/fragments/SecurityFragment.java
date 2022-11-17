package com.videoplayer.mediaplaye.privatevideo.player.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.videoplayer.mediaplaye.privatevideo.player.ActivityEncrypt;
import com.videoplayer.mediaplaye.privatevideo.player.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class SecurityFragment extends Fragment {
    private String encryptedFileName = "Enc_File2.txt";
    private static String algorithm = "AES";
    static SecretKey yourKey = null;
    char[] p = {'p', 'a', 's', 's'};
    String[] sourceFiles = { "C:/file1", "C:/file2" };

    public SecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_security, container, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(baos);

        byte[] buffer = new byte[4096];

        for (int i = 0; i < sourceFiles.length; i++)
        {
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(sourceFiles[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                zout.putNextEntry(new ZipEntry(sourceFiles[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int length = 0;
            while (true)
            {
                try {
                    if (!((length = fin.read(buffer)) > 0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    zout.write(buffer, 0, length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                zout.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            zout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = baos.toByteArray();

        ActivityEncrypt activityEncrypt = new ActivityEncrypt();
        activityEncrypt.saveFile("abccccsdfgfds");

        //activityEncrypt.decodeFile()
        activityEncrypt.readFile();
        Toast.makeText(getContext(), "" + activityEncrypt.readFile(), Toast.LENGTH_SHORT).show();
        return v;
    }


}
