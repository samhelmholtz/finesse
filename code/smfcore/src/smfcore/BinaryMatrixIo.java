/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryMatrixIo implements IBinaryMatrixIo {

    @Override
    public void WriteToFile(String filename, Integer[][] matrix) {
        System.out.println("Writing matrix to " + filename);
        PrintWriter writer = null;
        try {
            int n = matrix.length;
            int m = matrix[0].length;
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(n);
            writer.println(m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    writer.print(matrix[i][j] + " ");
                }
                writer.print("\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BinaryMatrixIo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BinaryMatrixIo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    @Override
    public Integer[][] ReadFromFile(String filename) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(filename));
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Integer[][] matrix = new Integer[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        return matrix;
    }
}
