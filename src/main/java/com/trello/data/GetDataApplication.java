package com.trello.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import core.utility.Logging;
import lombok.Data;
import java.io.File;

@Data
public class GetDataApplication {

    private String baseUrl;

    private static GetDataApplication data;

    static {

        if (data == null) {

            try {

                ObjectMapper om = new ObjectMapper(new YAMLFactory());
                data = om.readValue(
                        new File("src/main/resources/application.yaml"),
                        GetDataApplication.class);

            } catch (Exception e) {
                Logging.fail("A problem was encountered reading the application.yaml file!");
            }
        }
    }

    public static GetDataApplication getParameters() {
        return data;
    }


}
