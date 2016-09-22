/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sam
 */
public class SmfUtils {

    public static void WriteCsv(double[][] results, String filename) {
        try {
            PrintWriter pw = new PrintWriter(filename, "UTF-8");
            for (int i = 0; i < results.length; i++) {
                for (int j = 0; j < results[0].length - 1; j++) {
                    pw.write(results[i][j] + ",");
                }
                pw.write(results[i][results[0].length - 1] + "\n");
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SmfUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SmfUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String GetSortableTimestampString() {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "");
    }
}
