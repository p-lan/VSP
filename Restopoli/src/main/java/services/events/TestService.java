package services.events;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by patricklanger on 12.04.16.
 */
public class TestService {

    private String type;
    private String schema;
    private String id;
    private boolean required;

    private String nameType;
    private boolean nameRequired;

    private String descriptionType;

    private String serviceType;
    private boolean serviceRequired;

    private String uriType;
    private boolean uriRequired;

    public TestService(){

        type = "";
        schema = "";
        id = "";
        required = true;

        nameType = "";
        nameRequired = true;

        descriptionType = "";

        serviceType = "";
        serviceRequired = true;

        uriType = "";
        uriRequired = true;
    }

    public JsonObject getJson(){
        JsonObject value = Json.createObjectBuilder()
                .add("type", type)
                .add("$schema", schema)
                .add("id", id)
                .add("required", required)
                .add("name", Json.createObjectBuilder()
                        .add("type", nameType)
                        .add("required", nameRequired))
                .add("description", Json.createObjectBuilder()
                        .add("type", descriptionType))
                .add("servce", Json.createObjectBuilder()
                        .add("type", serviceType)
                        .add("required", serviceRequired))
                .add("uri", Json.createObjectBuilder()
                        .add("type", uriType)
                        .add("required", uriRequired))
                .build();

        return value;
    }



}
