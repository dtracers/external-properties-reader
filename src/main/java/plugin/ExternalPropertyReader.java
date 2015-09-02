package plugin;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "external-properties-reader")
public class ExternalPropertyReader
        extends AbstractMavenLifecycleParticipant
{
    @Requirement
    private Logger logger;

    @Override
    public void afterProjectsRead(MavenSession session) {
        // for example, to set some POM properties
        Properties sysProps = session.getSystemProperties();
        Properties projProps = session.getCurrentProject().getProperties();
        List<File> files = getFiles(projProps);
        for (File file : files) {
            logger.info("File path being loaded" + file.getAbsolutePath());
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                logger.error("File not found", e);
            }
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                logger.info("value " + line);
                String[] property = line.split("=");
                logger.info("Property " + property.length);
                projProps.setProperty(property[0],property[1]);
            }
        }
    }

    private List<File> getFiles(Properties projProps) {
        Enumeration enumeration = projProps.propertyNames();
        List<String> propertyNames = new ArrayList<String>();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            if (name.startsWith("externalPropertyFile_")) {
                propertyNames.add(name);
                logger.info("Property name " + name);
            }
            if (name.endsWith("PropFile")) {
                propertyNames.add(name);
                logger.info("Property name " + name);
            }
        }
        List<File> files = new ArrayList<File>();
        for (String propertyName: propertyNames) {
            String property = projProps.getProperty(propertyName);
            files.add(new File(property));
        }
        return files;
    }
}
