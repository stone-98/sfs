package github.stone.sfs.reader;

import github.stone.sfs.model.ReadContent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shikui@tidu.com
 * @date 2021/10/22
 */
@Slf4j
public class FileReaderTest {
    @Test
    public void readerTest(){
        FileReader fileReader = new FileReader();
        ReadContent read = fileReader.read("C:\\");
    }
}
