package fr.jayblanc.resteasy;

import java.io.InputStream;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class UploadFormRepresentation {

    @FormParam("name")
    @PartType("text/plain")
    private String name = "";

    @FormParam("data")
    @PartType("application/octet-stream")
    private InputStream data = null;
    
    public UploadFormRepresentation() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }

}
