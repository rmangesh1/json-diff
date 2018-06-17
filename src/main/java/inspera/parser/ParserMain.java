package inspera.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import inspera.parser.domain.Examination;
import inspera.parser.domain.Metadata;
import inspera.parser.domain.diff.ExaminationDifference;
import inspera.parser.domain.diff.MetaDiff;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rmang on 14-06-2018.
 */
public class ParserMain {

    public static void main(String[] args) throws URISyntaxException, IOException, IllegalAccessException {
        String beforeJsonString = new String(Files.readAllBytes(Paths.get(ParserMain.class.getClassLoader().getResource("before.json").toURI())));

        String afterJsonString = new String(Files.readAllBytes(Paths.get(ParserMain.class.getClassLoader().getResource("after.json").toURI())));

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode beforeJsonObject = objectMapper.readTree(beforeJsonString);

        //System.out.println(beforeJsonObject);

        Examination beforeExamination = objectMapper.convertValue(beforeJsonObject, Examination.class);

        System.out.println(beforeExamination);

        System.out.println(objectMapper.writeValueAsString(beforeExamination));

        JsonNode afterJsonObject = objectMapper.readTree(afterJsonString);

        //System.out.println(afterJsonObject);

        Examination afterExamination = objectMapper.convertValue(afterJsonObject, Examination.class);

        System.out.println(afterExamination);

        Field [] fields = Examination.class.getDeclaredFields();
        ExaminationDifference examinationDifference = new ExaminationDifference();
        List<MetaDiff> metaDiffList = new ArrayList<>();
        MetaDiff metaDiff = null;

        Object firstExamValueContainer = null;
        Object secondExamValueContainer = null;

        for(Field field : fields) {

            if(field.getType().equals(Metadata.class)) {
                field.setAccessible(true);
                firstExamValueContainer = field.get(beforeExamination);
                secondExamValueContainer = field.get(afterExamination);

                Field [] metaFields = null;

                if(!firstExamValueContainer.equals(secondExamValueContainer)) {
                    metaFields = firstExamValueContainer.getClass().getDeclaredFields();

                    Object firstMetaValueHolder = null;
                    Object secondMetaValueHolder = null;



                    for(Field metaField : metaFields) {
                        metaField.setAccessible(true);
                        firstMetaValueHolder = metaField.get(firstExamValueContainer);
                        secondMetaValueHolder = metaField.get(secondExamValueContainer);

                        metaDiff = new MetaDiff();

                        if(!firstMetaValueHolder.equals(secondMetaValueHolder)) {
                            metaDiff.setField(metaField.getName());
                            metaDiff.setBeforeValue(firstMetaValueHolder);
                            metaDiff.setAfterValue(secondMetaValueHolder);

                            metaDiffList.add(metaDiff);
                        }
                    }


                }
            }


        }

        examinationDifference.setMetaDiff(metaDiffList);

        System.out.println(examinationDifference);

        System.out.println(objectMapper.writeValueAsString(examinationDifference));



        /*System.out.println(afterExamination);

        ZonedDateTime dt = afterExamination.getMeta().getEndTime().atZone(ZoneId.of("Europe/Oslo"));
        System.out.println(dt);

        //System.out.println(DateTimeFormatter.BASIC_ISO_DATE.format(dt));

        Arrays.stream(afterExamination.getClass().getDeclaredFields()).forEach(System.out::println);
        System.out.println("VALUES===--------------------");
        for(Field field : afterExamination.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.get(afterExamination));
        }*/
    }
}
