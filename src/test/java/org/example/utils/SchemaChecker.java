package org.example.utils;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class SchemaChecker {

    public static void assertMatchesSchema(Response response, String schemaPath) {
        assertThat("JSON не соответствует схеме: " + schemaPath,
                response.getBody().asString(),
                matchesJsonSchemaInClasspath(schemaPath));
    }
}
