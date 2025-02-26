package com.practical.merabillpractical.Utils;

import android.content.Context;
import android.util.Log;

import com.practical.merabillpractical.data.module.Payments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    private static final String FILE_NAME = "LastPayment.txt";

    public static void writeFile(List<Payments> payments, Context context) {
        String content = Constants.paymentDataToJson(payments);
        File fileDir = context.getFilesDir();
        File txtFile = new File(fileDir, FILE_NAME);
        try {
            if (!txtFile.exists()) {
                if (txtFile.createNewFile()) {
                    writeFile(txtFile, content);
                }
            } else {
                writeFile(txtFile, content);
            }
        } catch (IOException e) {
            Log.e("TAG", "Error " + e.getMessage());
        }
    }

    private static void writeFile(File file, String content) throws IOException {
        Writer writer = new FileWriter(file, false);
        writer.append(content);
        writer.flush();
        writer.close();
    }

    public static List<Payments> getPayments(Context context) {
        File fileDir = context.getFilesDir();
        File txtFile = new File(fileDir, FILE_NAME);
        if (txtFile.exists()) {
            try {
                StringBuilder strBuffer = new StringBuilder();
                FileReader fileReader = new FileReader(txtFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    strBuffer.append(line);
                }
                bufferedReader.close();
                if (!strBuffer.toString().isEmpty()) {
                    return Constants.stringToPaymentList(strBuffer.toString());
                }
            } catch (IOException e) {
                Log.e("TAG", "Read file error! " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }
}
