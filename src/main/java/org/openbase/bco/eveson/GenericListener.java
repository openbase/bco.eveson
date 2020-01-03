package org.openbase.bco.eveson;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2020 openbase.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.openbase.bco.eveson.eventfilter.EventFilter;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.exception.printer.ExceptionPrinter;
import org.openbase.jul.extension.rsb.com.exception.RSBResolvedException;
import rsb.Event;
import rsb.Factory;
import rsb.Listener;
import rsb.RSBException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jplettemeier
 */
public class GenericListener {

    private ScopePlayer player;
    private String scope;
    private Listener listener;
    private EventFilter eventFilter;

    public GenericListener(final String scope, final ScopePlayer player) throws InstantiationException, InterruptedException {
        try {
            this.scope = scope;
            this.player = player;
            String filterType = player.getEventFilter();

            if (filterType != null) {
                Class<? extends EventFilter> filterClass = (Class<? extends EventFilter>) Class.forName(EventFilter.class.getPackage().getName() + "." + filterType);
                eventFilter = filterClass.getConstructor().newInstance();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));

            if (eventFilter != null) {
                this.listener = Factory.getInstance().createListener(scope);
            } else {
                this.listener = Factory.getInstance().createListener(scope, RSBGenericConverterConfig.generateConfig());
            }


            listener.addHandler((Event event) -> {
//                System.out.println("================================");
//                System.out.println("Data["+scope+"]:"+((FloorModuleState) event.getData()).toString());
//                System.out.println("Play Sample[" + player.getSampleFile() + "] for Scope[" + scope + "]");
                if (eventFilter != null) {
                    if (eventFilter.isCompatible(event.getData())) {
                        if (eventFilter.filter(event.getData())) {
//                            System.out.println("event got filtered!");
                            return;
                        }
                    }
                }
//                System.out.println("================================");
//                event.getData().getClass()
                player.play(0.5);
            }, true);

            listener.activate();
            System.out.println("Listener activated for " + this);
        } catch (RSBException ex) {
            throw new org.openbase.jul.exception.InstantiationException(this, new RSBResolvedException(ex));
        } catch (ClassNotFoundException | java.lang.InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new InstantiationException(this, ex);
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
        } catch (RSBException ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Could not shutdown " + this, new RSBResolvedException(ex)), System.out);
        } catch (InterruptedException ex) {
            ExceptionPrinter.printHistory(new CouldNotPerformException("Shutdown of " + this + " was interrupted!"), System.out);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + scope + "]";
    }
}
