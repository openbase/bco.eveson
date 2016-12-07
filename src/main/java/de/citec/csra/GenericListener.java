package de.citec.csra;

import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import rsb.Event;
import rsb.Factory;
import rsb.Listener;
import rsb.RSBException;

/**
 *
 * @author jplettemeier
 */
public class GenericListener {

    private final ScopePlayer player;
    private final String scope;
    private final Listener listener;

    public GenericListener(final String scope, final ScopePlayer player) throws org.openbase.jul.exception.InstantiationException, InterruptedException {
        try {
            this.scope = scope;
            this.player = player;

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    shutdown();
                }
            });

            this.listener = Factory.getInstance().createListener(scope, RSBGenericConverterConfig.generateConfig());

            listener.addHandler((Event event) -> {
//                System.out.println("Play Sample[" + player.getSampleFile() + "] for Scope[" + scope + "]");
                player.play(0.5);
            }, true);

            listener.activate();
            System.out.println("Listener activated for " + this);
        } catch (RSBException ex) {
            throw new org.openbase.jul.exception.InstantiationException(this, ex);
        }
    }

    public String getScope() {
        return scope;
    }

    public void shutdown() {
        if (listener == null) {
            return;
        }

        try {
            listener.deactivate();
        } catch (RSBException | InterruptedException ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Could not shutdown " + this), System.out);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + scope + "]";
    }
}
