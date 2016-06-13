/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.jp;

import java.io.File;
import org.dc.jps.core.JPService;
import org.dc.jps.exception.JPNotAvailableException;
import org.dc.jps.preset.AbstractJPDirectory;
import org.dc.jps.preset.JPPrefix;
import org.dc.jps.tools.FileHandler;

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
        return "This property configure the audio resource folder.";
    }
    
}
