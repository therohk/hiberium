package com.konivax.utils.format;

import com.konivax.utils.ReflectUtils;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import javax.persistence.Column;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.konivax.models.mapper.FieldConstants.*;

public final class CsvUtils {

    private CsvUtils() { }

    /**
     * support for comments in csv files
     */
    public static CsvPreference getCsvPreferences() {
        return new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                .skipComments(new CommentStartsWith("#"))
                .build();
    }

    public static <T> CellProcessor[] getCellProcessorForObject(Class<T> clazz) {
        String[] columnMapping = ReflectUtils.getColumnNamesAsArray(clazz);
        return getCellProcessorForObject(clazz, columnMapping);
    }

    public static <T> CellProcessor[] getCellProcessorForObject(Class<T> clazz, String[] headers) {
        Field[] fields = clazz.getDeclaredFields();
        CellProcessor[] processors = new CellProcessor[headers.length];

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];

            //find matching field
            Field field = null;
            for(Field currField : fields) {
                if(currField.getAnnotation(Column.class) == null)
                    continue;
                if(header.equals(currField.getAnnotation(Column.class).name()))
                  field = currField;
            }

            String className = field.getType().getSimpleName();
            switch (className) {
                case TYPE_STRING:
                    processors[i] = null;
                    break;
                case TYPE_INTEGER:
                    processors[i] = new Optional(new ParseInt());
                    break;
                case TYPE_DOUBLE:
                    processors[i] = new Optional(new ParseDouble());
                    break;
                case TYPE_DATETIME:
                    processors[i] = new Optional(new ParseDate("yyyy-MM-dd HH:mm:ss.SSSSS", true));
                    break;
                case TYPE_BOOLEAN:
                    processors[i] = new Optional(new ParseBool());
                    break;
                case TYPE_CHARACTER:
                    processors[i] = new Optional(new ParseChar());
                    break;
            }
        }
        return processors;
    }

   //--------------------------------------------------------------------------

    public static <T> List<T> readCsvFileData(String filePath, Class<T> clazz) {
        System.out.println("loading csv from "+filePath);
        try {
            FileReader fileReader = new FileReader(filePath, Charset.forName("UTF-8"));
            return readCsvReaderData(fileReader, clazz);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static <T> List<T> readCsvReaderData(Reader reader, Class<T> clazz) {
        System.out.println("loading into "+clazz.getSimpleName()+" from reader");
        List<T> entityList = new ArrayList<T>();
        try {
            ICsvBeanReader beanReader = new CsvBeanReader(reader, getCsvPreferences());
            String[] headers = beanReader.getHeader(true);

            String[] fieldMapping = ReflectUtils.getFieldNamesAsArray(clazz, headers);
            CellProcessor[] processors = getCellProcessorForObject(clazz, headers);
            System.out.println("csv header mapping "+
                    Arrays.asList(headers).toString()+" -> "+Arrays.asList(fieldMapping).toString());

            T entity = null;
            while ((entity = beanReader.read(clazz, fieldMapping, processors)) != null) {
                System.out.println(JsonUtils.serializeJavaObject(entity));
                entityList.add(entity);
            }

        } catch (IOException ioe) {
            throw new RuntimeException("failed to parse csv file : " + ioe.getMessage());
        } catch (SuperCsvException sce) {
            CsvContext context = sce.getCsvContext();
            String errorText = String.format("error on lineNum=%s ; rowNum=%s ; colNum=%s",
                    context.getLineNumber(), context.getRowNumber(), context.getColumnNumber());
            System.out.println(errorText);
            System.out.println(context.getRowSource());
            throw new RuntimeException(sce);
        }

        return entityList;
    }

    public static <T> void writeCsvResponseData(PrintWriter writer, List<T> items, Class<T> clazz) throws IOException {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, getCsvPreferences());
        String[] columnMapping = ReflectUtils.getColumnNamesAsArray(clazz);
        String[] fieldMapping = ReflectUtils.getFieldNamesAsArray(clazz);

        csvWriter.writeHeader(columnMapping);
        for (T item : items) {
            csvWriter.write(item, fieldMapping);
        }
        csvWriter.close();
    }
}
