/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smfcore;

import java.io.FileNotFoundException;

/**
 *
 * @author sam
 */
public interface IBinaryMatrixIo {
    void WriteToFile(String filename, Integer[][] matrix);
    Integer[][] ReadFromFile(String filename) throws FileNotFoundException;
}
