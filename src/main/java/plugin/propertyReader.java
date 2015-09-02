package plugin;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "external-properties-reader")
public class PropertyReader
        extends AbstractMavenLifecycleParticipant
{

    /**
     * My File.
     */
    @Parameter
    private File[] files;

    @Requirement
    private Logger logger;

    @Override
    public void afterProjectsRead(MavenSession session) {
        // for example, to set some POM properties
        Properties sysProps = session.getSystemProperties();
        Properties projProps = session.getCurrentProject().getProperties();
        for (File file : files) {
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
}
