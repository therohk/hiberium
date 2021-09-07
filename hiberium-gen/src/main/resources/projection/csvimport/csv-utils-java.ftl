package ${package_base}.utils;

import lombok.extern.slf4j.Slf4j;
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
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public final class CsvUtils {

    private CsvUtils() { }

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
                case "String":
                    processors[i] = null;
                    break;
                case "Integer":
                    processors[i] = new Optional(new ParseInt());
                    break;
                case "Long":
                    processors[i] = new Optional(new ParseLong());
                    break;
                case "Double":
                    processors[i] = new Optional(new ParseDouble());
                    break;
                case "Date":
                    processors[i] = new Optional(new ParseDate("yyyy-MM-dd HH:mm:ss.SSSSS", true));
                    break;
                case "Boolean":
                    processors[i] = new Optional(new ParseBool());
                    break;
                case "Character":
                    processors[i] = new Optional(new ParseChar());
                    break;
            }
        }
        return processors;
    }

    //--------------------------------------------------------------------------

    public static <T> List<T> readCsvFileData(String filePath, Class<T> clazz) {
        log.info("loading csv from "+filePath);
        try {
            FileReader fileReader = new FileReader(filePath, StandardCharsets.UTF_8);
            return readCsvReaderData(fileReader, clazz);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static <T> List<T> readCsvReaderData(Reader reader, Class<T> clazz) {
        log.info("loading into "+clazz.getSimpleName()+" from reader");
        List<T> entityList = new ArrayList<T>();
        try {
            ICsvBeanReader beanReader = new CsvBeanReader(reader, getCsvPreferences());
            String[] headers = beanReader.getHeader(true);

            String[] fieldMapping = getFieldNamesAsArray(clazz, headers);
            CellProcessor[] processors = getCellProcessorForObject(clazz, headers);
            log.info("csv header mapping {} -> {}", Arrays.asList(headers).toString(), Arrays.asList(fieldMapping));

            T entity = null;
            while ((entity = beanReader.read(clazz, fieldMapping, processors)) != null) {
                log.debug(entity.toString());
                entityList.add(entity);
            }

        } catch (IOException ioe) {
            throw new RuntimeException("failed to parse csv file : " + ioe.getMessage());
        } catch (SuperCsvException sce) {
            CsvContext context = sce.getCsvContext();
            log.error("error on line={} ; row={} ; col={}",
                    context.getLineNumber(), context.getRowNumber(), context.getColumnNumber());
            log.error(context.getRowSource().toString());
            throw new RuntimeException(sce);
        }

        return entityList;
    }

    public static <T> void writeCsvWriterData(PrintWriter writer, List items, Class<T> clazz) {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, getCsvPreferences());
        String[] columnMapping = getColumnNamesAsArray(clazz);
        String[] fieldMapping = getFieldNamesAsArray(clazz);
        try {
            csvWriter.writeHeader(columnMapping);
            for (Object item : items)
                csvWriter.write(item, fieldMapping);
            csvWriter.close();
        } catch (IOException ioe) {
            throw new RuntimeException("failed to write csv file : " + ioe.getMessage());
        }
    }

    public static CsvPreference getCsvPreferences() {
        return new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                .skipComments(new CommentStartsWith("#"))
                .build();
    }

    //-------------------------------------------------------------------------

    public static <T> String[] getFieldNamesAsArray(Class<T> clazz) {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames.toArray((String[]) Array.newInstance(String.class, fieldNames.size()));
    }

    public static <T> String[] getFieldNamesAsArray(Class<T> clazz, String[] headers) {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();

        for (String header : headers) {
            boolean matched = false;
            for (Field field : fields) {
                if (field.getAnnotation(Column.class) == null)
                    continue;
                if (header.equals(field.getAnnotation(Column.class).name())) {
                    fieldNames.add(field.getName());
                    matched = true;
                    break;
                }
            }
            if (!matched)
                throw new RuntimeException("field " + header + " not available");
        }
        if (headers.length != fieldNames.size())
            throw new RuntimeException("fields not available or mismatched");
        return fieldNames.toArray((String[]) Array.newInstance(String.class, fieldNames.size()));
    }

    public static <T> String[] getColumnNamesAsArray(Class<T> clazz) {
        List<String> columnNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null)
                columnNames.add(column.name());
        }
        return columnNames.toArray((String[]) Array.newInstance(String.class, columnNames.size()));
    }

}