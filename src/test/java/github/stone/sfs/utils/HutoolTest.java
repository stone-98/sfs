package github.stone.sfs.utils;

import cn.hutool.core.io.file.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author candy
 * @date 2021/10/23 17:04
 */
@Slf4j
public class HutoolTest {
    @Test
    public void readFileTest(){
        FileReader fileReader = new FileReader("index.html");
        log.info(fileReader.readString());
    }
}
