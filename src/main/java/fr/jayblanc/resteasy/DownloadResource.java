package fr.jayblanc.resteasy;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/download")
@Produces({ MediaType.APPLICATION_JSON })
public class DownloadResource {

    private static final Logger LOGGER = Logger.getLogger(DownloadResource.class.getName());

    @GET
    @Path("/1")
    @Produces({ MediaType.TEXT_HTML, MediaType.WILDCARD })
    public Response downloadFile1(@QueryParam("file") String filepath, @Context Request request) throws Exception {
        LOGGER.log(Level.INFO, "GET /download/1");
        java.nio.file.Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            throw new Exception("Unable to find a file for path [" + filepath + "] in the storage");
        }
        File content = path.toFile();
        ResponseBuilder builder = Response.ok(content).header("Content-Type", Files.probeContentType(path)).header("Content-Length", Files.size(path)).header("Accept-Ranges", "bytes")
                    .header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(content.getName(), "utf-8"));
        return builder.build();
    }
    
    @GET
    @Path("/2")
    @Produces({ MediaType.TEXT_HTML, MediaType.WILDCARD })
    public Response downloadFile2(@QueryParam("file") String filepath, @Context Request request) throws Exception {
        LOGGER.log(Level.INFO, "GET /download/2");
        java.nio.file.Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            throw new Exception("Unable to find a file for path [" + filepath + "] in the storage");
        }
        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(0);
        cc.setMustRevalidate(true);
        Date lmd = new Date(Files.getLastModifiedTime(path).toMillis() / 1000 * 1000);
        ResponseBuilder builder = null;
        if (System.currentTimeMillis() - Files.getLastModifiedTime(path).toMillis() > 1000) {
            builder = request.evaluatePreconditions(lmd);
        }
        if (builder == null) {
            File content = path.toFile();
            builder = Response.ok(content).header("Content-Type", Files.probeContentType(path)).header("Content-Length", Files.size(path)).header("Accept-Ranges", "bytes")
                    .header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(content.getName(), "utf-8"));
            builder.lastModified(lmd);
        }
        builder.cacheControl(cc);
        return builder.build();
    }

    @GET
    @Path("/3")
    @Produces({ MediaType.TEXT_HTML, MediaType.WILDCARD })
    public void downloadFile3(@QueryParam("file") String filepath, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        LOGGER.log(Level.INFO, "GET /download/3");
        java.nio.file.Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            throw new Exception("Unable to find a file for path [" + filepath + "] in the storage");
        }
        response.setContentType(Files.probeContentType(path));
        response.setContentLengthLong(Files.size(path));
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(path.getFileName().toString(), "utf-8"));
        ServletOutputStream out = response.getOutputStream();
        Files.copy(path, out);
    }

}