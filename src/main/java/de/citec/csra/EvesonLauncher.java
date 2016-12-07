package de.citec.csra;

import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
import de.citec.csra.jp.JPAudioVolume;
import de.citec.csra.jp.JPThemeFile;
import org.openbase.bco.registry.lib.launch.AbstractLauncher;
import static org.openbase.bco.registry.lib.launch.AbstractLauncher.main;
import org.openbase.jps.core.JPService;
import org.openbase.jps.preset.JPShowGUI;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rst.devices.sensfloor.FloorModuleStateType;

/**
 *
 * @author divine
 */
public class EvesonLauncher extends AbstractLauncher<Eveson> {

    public EvesonLauncher() throws org.openbase.jul.exception.InstantiationException {
        super(Eveson.class, Eveson.class);
    }

    @Override
    public void loadProperties() {
        JPService.registerProperty(JPAudioResoureFolder.class);
        JPService.registerProperty(JPAudioOutputDevice.class);
        JPService.registerProperty(JPAudioVolume.class);
        JPService.registerProperty(JPShowGUI.class, false);
        JPService.registerProperty(JPThemeFile.class);
    }

    public static void main(String args[]) throws Throwable {
        main(args, Eveson.class, EvesonLauncher.class);
    }
}
