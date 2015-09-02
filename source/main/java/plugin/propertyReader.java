import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "mySpecialService")
public class propertyReader
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
          Scanner scanner = new Scanner(file);
          while (scanner.hasNext()) {
            String line = scanner.nextLine();
            logger.log("value " + line);
            String property = line.split("=");
            logger.log("Property " + property.length);
            projProps.setProperty(property[0],property[1]);
          }

        }
    }
}
