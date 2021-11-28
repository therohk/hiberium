package com.konivax.models.template;

import com.konivax.utils.ClassUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.FtlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtlExpressionTest {

    @Test
    public void testFreemarkerCondition() {
        Map<String, Object> root = new HashMap<String, Object>();
        Assertions.assertTrue(FtlUtils.parseLocalExpression(root, "1=1", false));
        Assertions.assertFalse(FtlUtils.parseLocalExpression(root, "1=0", false));
        Assertions.assertFalse(FtlUtils.parseLocalExpression(root, "field??", false));
        root.put("field", "value");
        Assertions.assertTrue(FtlUtils.parseLocalExpression(root, "field??", false));
        Assertions.assertTrue(FtlUtils.parseLocalExpression(root, "field=='value'", false));
    }

    @Test
    public void testClassScanner() throws ClassNotFoundException {
        List<Class<?>> clazzes = ClassUtils.getClassesForPackage("com.konivax.models");
        for(Class clazz : clazzes)
            System.out.println(clazz.getName());

        Class clazz = ClassUtils.findClass("Concept", "com.konivax");
        System.out.println(clazz.getName());
        Assertions.assertEquals("Concept", clazz.getSimpleName());

        CellProcessor[] processors = CsvUtils.getCellProcessorForObject(clazz);
        for (CellProcessor processor : processors)
            System.out.println(processor != null ? processor.toString() : "empty");
    }
}
