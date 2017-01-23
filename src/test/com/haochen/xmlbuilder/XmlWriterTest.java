import static org.junit.Assert.*;

import com.haochen.xmlbuilder.XmlWriter;
import com.haochen.xmlbuilder.util.XmlUtil;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * XMLWriter Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>一月 22, 2017</pre>
 */
public class XmlWriterTest {
    XmlWriter writer;
    List list;
    Map map;
    Object object;

    @Before
    public void before() throws Exception {
        writer = new XmlWriter();
        Outer outer = new Outer();
        Inner inner = new Inner();
        InInner inInner = new InInner();
        outer.inner = inner;
        inner.inInner = inInner;
        inInner.outer = outer;
        object = outer;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: write(Object obj, File file)
     */
    @Test
    public void testWriteForObjFile() throws Exception {
//TODO: Test goes here...
        File file = new File("test_write_for_obj_file.xml");
        writer.write(object, file);
    }

    /**
     * Method: write(String xmlString, File file)
     */
    @Test
    public void testWriteForXmlStringFile() throws Exception {
//TODO: Test goes here...
        File file = new File("test_write_for_xml_string_file.xml");
        writer.write(XmlUtil.xmlString(object), file);
    }
}
