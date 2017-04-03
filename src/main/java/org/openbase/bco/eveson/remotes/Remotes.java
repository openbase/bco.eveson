package org.openbase.bco.eveson.remotes;

import org.openbase.bco.eveson.EventPlayer;
import org.openbase.bco.eveson.PowerTest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openbase.bco.dal.remote.unit.MotionDetectorRemote;
import org.openbase.bco.dal.remote.unit.location.LocationRemote;
import org.openbase.bco.registry.location.lib.LocationRegistry;
import org.openbase.bco.registry.location.remote.CachedLocationRegistryRemote;
import org.openbase.bco.registry.unit.lib.UnitRegistry;
import org.openbase.bco.registry.unit.remote.CachedUnitRegistryRemote;
import org.openbase.jps.core.JPService;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.JPShowGUI;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.InstantiationException;
import rst.domotic.state.EnablingStateType.EnablingState;
import rst.domotic.unit.UnitConfigType;
import rst.domotic.unit.UnitTemplateType;

/**
 *
 *
 * @author mgao
 * @author Divine Threepwood
 */
public class Remotes {

    UnitRegistry unitRegistry;
    LocationRegistry locationRegistry;

    public Remotes() throws InstantiationException, InterruptedException {
        try {
            CachedUnitRegistryRemote.waitForData();
            CachedLocationRegistryRemote.waitForData();
            this.unitRegistry = CachedUnitRegistryRemote.getRegistry();
            this.locationRegistry = CachedLocationRegistryRemote.getRegistry();
        } catch (CouldNotPerformException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init() throws InterruptedException {
        try {
            LocationRemote locationRemote = new LocationRemote();
            locationRemote.init(locationRegistry.getRootLocationConfig());
            locationRemote.activate();
            final LocationObserver locationObserver = new LocationObserver();
            locationRemote.addDataObserver(locationObserver);
            if (JPService.getProperty(JPShowGUI.class).getValue()) {
                new PowerTest(locationObserver).setVisible(true);
            }
            List<UnitConfigType.UnitConfig> motionDetectors = unitRegistry.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.MOTION_DETECTOR);
            List<MotionDetectorRemote> motionDetectorRemotes = new ArrayList<>();

            MotionDetectorRemote remote;

            int i = 1;
            String id;
            for (UnitConfigType.UnitConfig motionDetectorConfig : motionDetectors) {

                if (motionDetectorConfig.getEnablingState().getValue() != EnablingState.State.ENABLED) {
                    continue;
                }
                remote = new MotionDetectorRemote();
                remote.init(motionDetectorConfig);
                remote.activate();
                motionDetectorRemotes.add(remote);
                id = motionDetectorConfig.getType().name() + "_" + i;
//                System.out.println(id + ": Scope: " + motionSensorConfig.getScope().toString());
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
        } finally {
            unitRegistry.shutdown();
        }
    }
}
