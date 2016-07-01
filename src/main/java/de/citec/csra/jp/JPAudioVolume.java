package de.citec.csra.jp;

import org.openbase.jps.exception.JPBadArgumentException;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.AbstractJPFloat;

/**
 *
 * @author mgao
 */
public class JPAudioVolume extends AbstractJPFloat {

    public final static String[] COMMAND_IDENTIFIERS = {"-a", "--amplitude"};
    
    public JPAudioVolume() {
        super(COMMAND_IDENTIFIERS);
    }
    
    @Override
    public String getDescription() {
        return "This property controls the volume of the application. 1.0 = Maximum volume";
    }

    @Override
    protected Float getPropertyDefaultValue() throws JPNotAvailableException {
        return 1.0f;
    }
    
}
