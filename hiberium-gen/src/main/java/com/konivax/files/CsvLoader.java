package com.konivax.files;

import com.konivax.models.Attribute;
import com.konivax.models.Concept;
import com.konivax.utils.ReflectUtils;
import com.konivax.utils.format.CsvUtils;
import com.konivax.utils.format.JsonUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvLoader {

    public static List<Concept> loadConceptDefFile(String filePath) {
        System.out.println("loading concepts from "+filePath);
        List<Concept> conceptList = new ArrayList<Concept>();
        try {
            FileReader fileReader = new FileReader(filePath, Charset.forName("UTF-8"));
            ICsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
            String[] headers = beanReader.getHeader(true);
//            System.out.println(CollectionUtils.arrayToList(headers).toString());

            String[] fieldMapping = ReflectUtils.getFieldNamesAsArray(Concept.class, headers);
            CellProcessor[] processors = CsvUtils.getCellProcessorForObject(Concept.class, headers);
//            System.out.println(CollectionUtils.arrayToList(fieldMapping).toString());

            Concept concept = null;
            while ((concept = beanReader.read(Concept.class, fieldMapping, processors)) != null) {
                System.out.println(JsonUtils.serializeJavaObject(concept));
                conceptList.add(concept);
            }

        } catch (IOException e) {
            throw new RuntimeException("failed to parse concept definitions file: " + e.getMessage());
        }

        return conceptList;
    }

    public static List<Attribute> loadAttributeXrefFile(String filePath) {
        System.out.println("loading attributes from "+filePath);
        List<Attribute> attributeList = new ArrayList<Attribute>();

        try {
            FileReader fileReader = new FileReader(filePath, Charset.forName("UTF-8"));
            ICsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
            String[] headers = beanReader.getHeader(true);
//            System.out.println(CollectionUtils.arrayToList(headers).toString());

            String[] fieldMapping = ReflectUtils.getFieldNamesAsArray(Attribute.class, headers);
            CellProcessor[] processors = CsvUtils.getCellProcessorForObject(Attribute.class, headers);
//            System.out.println(CollectionUtils.arrayToList(fieldMapping).toString());

            Attribute attribute = null;
            while ((attribute = beanReader.read(Attribute.class, fieldMapping, processors)) != null) {
                System.out.println(JsonUtils.serializeJavaObject(attribute));
                attributeList.add(attribute);
            }

        } catch (IOException e) {
            throw new RuntimeException("failed to parse attribute definitions file: " + e.getMessage());
        }
        return attributeList;
    }


    public static void attachConceptAttributes(List<Concept> conceptList, List<Attribute> attributeList) {

        for(Concept concept : conceptList) {

            String conceptName = concept.getConceptName();

            List<Attribute> attributeReq = attributeList.stream()
                    .filter(a -> a.getConceptName().equals(conceptName))
                    .collect(Collectors.toList());

            concept.setAttributeXref(attributeReq);
        }

    }

    private static void processConcept(Concept concept) {

    }

    private static void processAttribute(Attribute attribute) {

    }

    public static void validateConceptAttributes(Concept concept) {

    }

}
