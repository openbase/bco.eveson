
package de.citec.csra;

import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
import de.citec.csra.jp.JPAudioVolume;
import de.citec.csra.jp.JPThemeFile;
import org.openbase.jps.core.JPService;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;

/**
 *
 * @author divine
 */
public class EvesonLauncher {

    public static void main(String args[]) throws Throwable {

        // JPS setup
        JPService.setApplicationName(Eveson.class.getSimpleName().toLowerCase());
        JPService.registerProperty(JPAudioResoureFolder.class);
        JPService.registerProperty(JPAudioOutputDevice.class);
        JPService.registerProperty(JPAudioVolume.class);
        JPService.registerProperty(JPThemeFile.class);
        JPService.parseAndExitOnError(args);

        try {
            new Eveson().launch();
        } catch (final Exception ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Eveson runtime error occured!", ex), System.err);
            System.exit(255);
        }
    }

}
