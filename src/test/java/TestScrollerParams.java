import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aoklyunin.javaGLHelper.scrollers.params.RangeScrollerParams;
import com.github.aoklyunin.javaGLHelper.scrollers.params.SimpleScrollerParams;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static jMath.aoklyunin.github.com.Algorithms.sameJSONContent;

public class TestScrollerParams {

    @Test
    public void testSimpleScrollerParams() {
        String inPath = "src/main/resources/in/simpleScrollerParams.json";
        String outPath = "src/main/resources/out/simpleScrollerParams.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            SimpleScrollerParams worldControllerParams = objectMapper.readValue(new File(inPath), SimpleScrollerParams.class);
            objectMapper.writeValue(new File(outPath), worldControllerParams);

        } catch (IOException e) {
            throw new AssertionError("test error" + e);
        }
        assert sameJSONContent(inPath, outPath);
    }

    @Test
    public void testRangeScrollerParams() {
        String inPath = "src/main/resources/in/rangeScrollerParams.json";
        String outPath = "src/main/resources/out/rangeScrollerParams.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RangeScrollerParams worldControllerParams = objectMapper.readValue(new File(inPath), RangeScrollerParams.class);
            objectMapper.writeValue(new File(outPath), worldControllerParams);

        } catch (IOException e) {
            throw new AssertionError("test error" + e);
        }
        assert sameJSONContent(inPath, outPath);
    }
}
