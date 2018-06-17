package inspera.parser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import inspera.parser.deserializer.GeneralDateTimeDeserializer;
import inspera.parser.serializer.GeneralDateTimeSerializer;
import inspera.parser.domain.Examination;
import inspera.parser.domain.Metadata;
import inspera.parser.domain.diff.ExaminationDifference;
import inspera.parser.domain.diff.MetaDiff;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmang on 14-06-2018.
 */
public class ParserMain {

    public static void main(String[] args) throws URISyntaxException, IOException, IllegalAccessException, NoSuchFieldException {
        String beforeJsonString = new String(Files.readAllBytes(Paths.get(ParserMain.class.getClassLoader().getResource("before.json").toURI())));

        String afterJsonString = new String(Files.readAllBytes(Paths.get(ParserMain.class.getClassLoader().getResource("after.json").toURI())));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

        ObjectMapper objectMapper = new ObjectMapper();
        /*SimpleModule module = new SimpleModule("mmod");
        module.addDeserializer(ZonedDateTime.class, new GeneralDateTimeDeserializer());
        module.addSerializer(ZonedDateTime.class, new GeneralDateTimeSerializer());
        objectMapper.registerModule(module);*/

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer(formatter1));
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(javaTimeModule);

        JsonNode beforeJsonObject = objectMapper.readTree(beforeJsonString);

        //System.out.println(beforeJsonObject);

        Examination beforeExamination = objectMapper.convertValue(beforeJsonObject, Examination.class);

        System.out.println(beforeExamination);
        System.out.println("-----------------------------------------------------------------");
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
                        System.out.println(metaField.getType().getSimpleName());
                        if(!firstMetaValueHolder.equals(secondMetaValueHolder)) {

                            if(firstMetaValueHolder instanceof LocalDateTime) {
                                LocalDateTime ldtb = (LocalDateTime) firstMetaValueHolder;
                                LocalDateTime ldta = (LocalDateTime) secondMetaValueHolder;

                                ZonedDateTime zdtb = ldtb.atZone(ZoneId.of("Europe/Oslo"));
                                ZonedDateTime zdta = ldta.atZone(ZoneId.of("Europe/Oslo"));

                                metaDiff.setField(metaField.getName());
                                metaDiff.setBeforeValue(zdtb);
                                metaDiff.setAfterValue(zdta);
                            } else {
                                metaDiff.setField(metaField.getName());
                                metaDiff.setBeforeValue(firstMetaValueHolder);
                                metaDiff.setAfterValue(secondMetaValueHolder);
                            }

                            System.out.println(metaDiff.getAfterValue().getClass());

                            metaDiffList.add(metaDiff);
                        }
                    }


                }
            }


        }

        examinationDifference.setMetaDiff(metaDiffList);

        System.out.println(examinationDifference);


        String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(examinationDifference);

        System.out.println(str);

        System.out.println(beforeExamination.getClass().getDeclaredField("meta"));

        //System.out.println(objectMapper.readValue(str, ExaminationDifference.class));



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
