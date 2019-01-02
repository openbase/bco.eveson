package org.openbase.bco.eveson.remotes;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2019 openbase.org
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

import org.openbase.bco.dal.remote.layer.unit.Units;
import org.openbase.bco.eveson.EventPlayer;
import org.openbase.bco.eveson.PowerTest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openbase.bco.dal.remote.layer.unit.MotionDetectorRemote;

import org.openbase.bco.registry.remote.Registries;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.JPShowGUI;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.type.domotic.state.EnablingStateType.EnablingState;
import org.openbase.type.domotic.unit.UnitConfigType.UnitConfig;
import org.openbase.type.domotic.unit.UnitTemplateType;

/**
 *
 *
 * @author mgao
 * @author Divine Threepwood
 */
public class Remotes {

    public void init() throws InterruptedException {
        try {
            final LocationObserver locationObserver = new LocationObserver();
            // connect observer to root location
            Units.getUnit(Registries.getUnitRegistry(true).getRootLocationConfig(), false, Units.LOCATION).addDataObserver(locationObserver);
            if (JPService.getProperty(JPShowGUI.class).getValue()) {
                new PowerTest(locationObserver).setVisible(true);
            }
            List<UnitConfig> motionDetectorConfigs = Registries.getUnitRegistry(true).getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.MOTION_DETECTOR);

            int i = 1;
            String id;

            for (final UnitConfig motionDetectorConfig : motionDetectorConfigs) {

                // filter disabled motion detectors
                if (motionDetectorConfig.getEnablingState().getValue() != EnablingState.State.ENABLED) {
                    continue;
                }

                final MotionDetectorRemote remote = Units.getUnit(motionDetectorConfig, false, Units.MOTION_DETECTOR);

                id = motionDetectorConfig.getUnitType().name() + "_" + i;
                if (EventPlayer.getInstance().getScopeSampleMap().containsKey(id)) {
                    remote.addDataObserver(new MotionSensorObserver(id));
                }
//                System.out.println("Listener registered for " + motionSensorConfig.getType().name() + "_" + i + "(" + remote.getScope().toString() + ")");
                i++;
            }
        } catch (InstantiationException | InitializationException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CouldNotPerformException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JPNotAvailableException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
