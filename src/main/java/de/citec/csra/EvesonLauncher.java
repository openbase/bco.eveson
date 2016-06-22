/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra;

import de.citec.csra.jp.JPAudioOutputDevice;
import de.citec.csra.jp.JPAudioResoureFolder;
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
        JPService.parseAndExitOnError(args);
        
        new Eveson().launch();

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
            }
            System.exit(0);
        } catch (final Exception ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Eveson runtime error occured!", ex), System.err);
            System.exit(255);
        }
    }
    
}
