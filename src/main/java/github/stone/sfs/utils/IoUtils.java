package github.stone.sfs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author stone
 * @date 2021/10/23 19:07
 */
public class IoUtils {
    public static String readFile(File file) {
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = bufferedReader.readLine()) != null) {
                content.append(tempStr + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public static ByteArrayInputStream outSwapIn(ByteArrayOutputStream out) {
        return new ByteArrayInputStream(out.toByteArray());
    }
}
