package com.trello.models.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCardResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("closed")
    private boolean closed;

    @JsonProperty("idBoard")
    private String idBoard;

    @JsonProperty("idList")
    private String idList;

    @JsonProperty("idShort")
    private int idShort;

    @JsonProperty("name")
    private String name;

    @JsonProperty("shortLink")
    private String shortLink;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("url")
    private String url;

}
