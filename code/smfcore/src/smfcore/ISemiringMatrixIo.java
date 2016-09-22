package smfcore;

/**
 * Created by sam on 20.01.15.
 */
public interface ISemiringMatrixIo {
    void WriteToSmfFile(SemiringObject[][] matrix, ISemiringFeature[] semiringFeatures, String filename, boolean writeTranspose) throws Exception;
    void WriteToOrdinalFile(SemiringObject[][] matrix, String filename, Integer numScaleElements) throws Exception;
    SmfFileReadResult ReadFromSmfFile(String filename, boolean readIntoTranspose) throws Exception;
    SemiringObject[][] ReadFromOrdinalFile(String filename) throws Exception;
    int GetNumScaleElementsFromOrdinalFile(String filename) throws Exception;
}
