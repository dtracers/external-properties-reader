package plugin;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
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
        extends AbstractMavenLifecycleParticipant implements Mojo
{
    @Requirement
    private Logger logger;

    @Parameter(defaultValue = "${session}")
    private MavenSession session;

    @Override
    public void afterProjectsRead(MavenSession session) {
        execute(session);
    }

    public void execute(MavenSession session) {
        // for example, to set some POM properties
        Properties sysProps = session.getSystemProperties();
        Properties projProps = session.getCurrentProject().getProperties();
        logger.info("Grabbing files from properties");
        List<File> files = getFiles(projProps);
        for (File file : files) {
            logger.info("Properties that were loaded from file " + file.getName());
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                logger.error("File not found", e);
            }
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                logger.info(line);
                String[] property = line.split("=");
                projProps.setProperty(property[0].trim(), property[1].trim());
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
            }
            if (name.endsWith("PropFile")) {
                propertyNames.add(name);
            }
        }
        List<File> files = new ArrayList<File>();
        for (String propertyName: propertyNames) {
            String property = projProps.getProperty(propertyName);
            files.add(new File(property));
        }
        return files;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        execute(session);
    }

    public void setLog(final Log log) {

    }

    public Log getLog() {
        return null;
    }
}
