package fr.jayblanc.resteasy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/upload")
@Produces({ MediaType.APPLICATION_JSON })
public class UploadResource {
   
    private static final Logger LOGGER = Logger.getLogger(UploadResource.class.getName());
    
    private static final java.nio.file.Path STORE = Paths.get("~/.upload");
    
    @POST
    @Path("/1")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload1(@MultipartForm UploadFormRepresentation form, @Context HttpHeaders headers) throws Exception {
        LOGGER.log(Level.INFO, "POST /upload/1");
        
        if ( !Files.exists(STORE) ) {
            Files.createDirectories(STORE);
        }
        java.nio.file.Path tmpfile = Paths.get(STORE.toString(), Long.toString(System.nanoTime()));
        if (form.getData() != null) {
            Files.copy(form.getData(), tmpfile);
        }
        if (form.getName() != null) {
            java.nio.file.Path namedfile = Paths.get(STORE.toString(), form.getName());
            Files.move(tmpfile, namedfile);
        }
        
        LOGGER.log(Level.INFO, "Upload done");
        return Response.ok().build();
    }

}
