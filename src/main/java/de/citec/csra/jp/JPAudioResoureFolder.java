/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.jp;

import java.io.File;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.AbstractJPDirectory;
import org.openbase.jps.preset.JPPrefix;
import org.openbase.jps.tools.FileHandler;

/**
 *
 * @author mgao
 */
public class JPAudioResoureFolder extends AbstractJPDirectory {

    public final static String[] COMMAND_IDENTIFIERS = {"-r", "--audio-resource-folder"};
    
    public JPAudioResoureFolder() {
        super(COMMAND_IDENTIFIERS, FileHandler.ExistenceHandling.Must, FileHandler.AutoMode.Off);
        
    }
    
    @Override
    protected File getPropertyDefaultValue() throws JPNotAvailableException {
        return new File(JPService.getProperty(JPPrefix.class).getValue(), "/share/audio/samples/eveson");
    }

    @Override
    public String getDescription() {
        return "This property configures the audio resource folder.";
    }
    
}
