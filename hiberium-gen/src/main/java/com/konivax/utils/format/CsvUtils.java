package com.konivax.utils.format;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

import javax.persistence.Column;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {

    private CsvUtils() { }

    public static <T> CellProcessor[] getCellProcessorForObject(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        CellProcessor[] processors = new CellProcessor[fields.length];

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String className = field.getType().getName();

            switch (className) {
                case "java.lang.String":
                    processors[i] = null;
                    break;
                case "java.lang.Integer":
                    processors[i] = new ParseInt();
                    break;
                case "java.lang.Double":
                    processors[i] = new ParseDouble();
                    break;
                case "java.util.Date":
                    processors[i] = new ParseDate("yyyy-MM-dd HH:mm:ss.SSSSS", true);
                    break;
            }
        }
        return processors;
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

            String className = field.getType().getName();
            switch (className) {
                case "java.lang.String":
                    processors[i] = null;
                    break;
                case "java.lang.Integer":
                    processors[i] = new ParseInt();
                    break;
                case "java.lang.Double":
                    processors[i] = new ParseDouble();
                    break;
                case "java.util.Date":
                    processors[i] = new ParseDate("yyyy-MM-dd HH:mm:ss.SSSSS", true);
                    break;
            }
        }
        return processors;
    }

    public static <T> String[] getFieldNamesAsArray(Class<T> clazz, String[] headers) {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();

        for(String header : headers) {
            for(Field field : fields) {
                if(field.getAnnotation(Column.class) == null)
                    continue;
                if(header.equals(field.getAnnotation(Column.class).name()))
                    fieldNames.add(field.getName());
            }
        }
        if(headers.length != fieldNames.size())
            throw new RuntimeException("fields not available or mismatched");
        return fieldNames.toArray((String[]) Array.newInstance(String.class, fieldNames.size()));
    }

}
