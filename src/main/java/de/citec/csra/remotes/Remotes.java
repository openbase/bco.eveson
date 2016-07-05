package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openbase.bco.dal.remote.unit.MotionSensorRemote;
import org.openbase.bco.manager.location.remote.LocationRemote;
import org.openbase.bco.registry.device.lib.DeviceRegistry;
import org.openbase.bco.registry.device.remote.CachedDeviceRegistryRemote;
import org.openbase.bco.registry.location.lib.LocationRegistry;
import org.openbase.bco.registry.location.remote.CachedLocationRegistryRemote;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.InstantiationException;
import rst.homeautomation.state.InventoryStateType;
import rst.homeautomation.unit.UnitConfigType;
import rst.homeautomation.unit.UnitTemplateType;

/**
 *
 *
 * @author mgao
 */
public class Remotes {

    DeviceRegistry deviceRegistry;
    LocationRegistry locationRegistry;

    public Remotes() throws InstantiationException, InterruptedException {
        try {
            CachedDeviceRegistryRemote.waitForData();
            CachedLocationRegistryRemote.waitForData();
            this.deviceRegistry = CachedDeviceRegistryRemote.getRegistry();
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
            locationRemote.addDataObserver(new LocationObserver());
            List<UnitConfigType.UnitConfig> motionSensors = deviceRegistry.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.MOTION_SENSOR);
            List<MotionSensorRemote> motionSensorRemotes = new ArrayList<>();

            MotionSensorRemote remote;

            int i = 1;
            String id;
            for (UnitConfigType.UnitConfig motionSensorConfig : motionSensors) {

                if (!deviceRegistry.getDeviceConfigById(motionSensorConfig.getDeviceId()).getInventoryState().getValue().equals(InventoryStateType.InventoryState.State.INSTALLED)) {
                    continue;
                }
                remote = new MotionSensorRemote();
                remote.init(motionSensorConfig);
                remote.activate();
                motionSensorRemotes.add(remote);
                id = motionSensorConfig.getType().name() + "_" + i;
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
        } finally {
            deviceRegistry.shutdown();
        }
    }
}