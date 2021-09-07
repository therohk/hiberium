package ${package_base}.controller;

import ${package_base}.jdbc.repository.GenericRepository;
import ${package_base}.utils.ClassUtils;
import ${package_base}.utils.CsvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
  * useful for seeding and cloning tables
  * requires local java dependencies in project
  * @author therohk 2021/08/16
  */
@Slf4j
@Controller
public class BulkEntityController {

    @Autowired
    private GenericRepository genericRepository;

    @RequestMapping(value = "/export/csv/{entity}", method = RequestMethod.GET)
    public void exportTableToCsv(
            HttpServletResponse response,
            @PathVariable(value = "entity") String entityName
            //@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            //@RequestParam(value = "perPage", defaultValue = "1000") Integer perPage
    ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        String dateStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = entityName + "."+dateStamp+".csv";
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        response.setContentType("text/csv; charset=UTF-8");

        JpaRepository repository = genericRepository.getRepositoryInstance(entityClass);
        List<?> entityList = repository.findAll();

        CsvUtils.writeCsvWriterData(response.getWriter(), entityList, entityClass);
        log.info("EXPORT {} into csv total={}", entityName, entityList.size());
    }

    @RequestMapping(value = "/import/csv/{entity}", method = RequestMethod.POST)
    public ResponseEntity<List<?>> importTableFromCsv(
            @PathVariable(value = "entity") String entityName,
            @RequestParam("file") MultipartFile file
            //@RequestParam(value = "strategy", defaultValue = "B") String strategy,
            //@RequestParam(value = "headerStyle", defaultValue = "table") String headerStyle,
            //@RequestParam(value = "preserveKey", defaultValue = "true") Boolean preserveKey
    ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        List<?> entityList = CsvUtils.readCsvReaderData(reader, entityClass);

        JpaRepository repository = genericRepository.getRepositoryInstance(entityClass);
        List<?> objectList = repository.saveAll(entityList);

        log.info("IMPORT {} from csv total={}", entityName, entityList.size());
        return ResponseEntity.ok().body(objectList);
    }

}