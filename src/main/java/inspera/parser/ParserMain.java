package inspera.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import inspera.parser.domain.Examination;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

        JsonNode afterJsonObject = objectMapper.readTree(afterJsonString);

        //System.out.println(afterJsonObject);

        Examination afterExamination = objectMapper.convertValue(afterJsonObject, Examination.class);

        System.out.println(afterExamination);

        ZonedDateTime dt = afterExamination.getMeta().getEndTime().atZone(ZoneId.of("Europe/Oslo"));
        System.out.println(dt);

        //System.out.println(DateTimeFormatter.BASIC_ISO_DATE.format(dt));

        Arrays.stream(afterExamination.getClass().getDeclaredFields()).forEach(System.out::println);
        System.out.println("VALUES===--------------------");
        for(Field field : afterExamination.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(field.get(afterExamination));
        }
    }
}
