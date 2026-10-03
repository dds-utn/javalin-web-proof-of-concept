package ar.edu.utn.frba.dds.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Webhook {
    private String url;

    @JsonCreator
    public Webhook(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() { return url; }
}
