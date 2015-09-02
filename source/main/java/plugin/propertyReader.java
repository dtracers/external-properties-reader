import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "mySpecialService")
public class propertyReader
    extends AbstractMavenLifecycleParticipant
{

    @Requirement
    private Logger logger;

    @Override
    public void afterProjectsRead( MavenSession session ) {
        // ...do you magic here

        // for example, to set some POM properties
        Properties sysProps = session.getSystemProperties();
        ....
        Properties projProps = session.getCurrentProject().getProperties();
        projProps.setProperty("..",val);

    }
}
