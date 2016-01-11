package fr.jayblanc.resteasy;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest/*")
public class TestApplication extends Application {
    
    private HashSet<Class<?>> classes = new HashSet<Class<?>>();

    public TestApplication() {
        classes.add(DownloadResource.class);
        //classes.add(UploadResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        HashSet<Object> set = new HashSet<Object>();
        return set;
    }
}