package com.trello.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import core.utility.Logging;
import lombok.Data;
import java.io.File;

@Data
public class GetDataAuthorization {

    private String token;
    private String apiKey;
    private String secret;

    private static GetDataAuthorization data;

    static {

        if (data == null) {

            try {

                ObjectMapper om = new ObjectMapper(new YAMLFactory());
                data = om.readValue(
                        new File("src/main/resources/authorization.yaml"),
                        GetDataAuthorization.class);

            } catch (Exception e) {
                Logging.fail("A problem was encountered reading the authorization.yaml file!");
            }
        }
    }

    public static GetDataAuthorization getParameters() {
        return data;
    }
}
