package de.citec.csra.remotes;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceAllocator;
import com.softsynth.shared.time.TimeStamp;
import de.citec.csra.ScopePlayer;
import de.citec.csra.old_tests.Eveson;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.openbase.bco.registry.device.remote.DeviceRegistryRemote;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import rsb.AbstractEventHandler;
import rsb.Event;
import rst.homeautomation.unit.MotionSensorType;
import rst.homeautomation.unit.TemperatureSensorType;
import rst.homeautomation.unit.UnitConfigType;
import rst.homeautomation.unit.UnitTemplateType;

import org.openbase.bco.dal.remote.unit.MotionSensorRemote;
import org.openbase.bco.dal.remote.unit.TemperatureSensorRemote;
import org.openbase.bco.manager.location.remote.LocationRemote;
import org.openbase.bco.registry.location.remote.LocationRegistryRemote;
import org.openbase.jul.exception.CouldNotPerformException;
import org.openbase.jul.exception.InitializationException;
import org.openbase.jul.exception.InstantiationException;
import static rst.homeautomation.state.MotionStateType.MotionState.State.MOVEMENT;

/**
 * 
 *
 * @author mgao
 */
public class Remotes {

    DeviceRegistryRemote deviceRegistryRemote;
    LocationRegistryRemote locationRegistryRemote;

    public Remotes() throws InstantiationException {
        this.deviceRegistryRemote = new DeviceRegistryRemote();
        this.locationRegistryRemote = new LocationRegistryRemote();
        try {
            deviceRegistryRemote.init();
            deviceRegistryRemote.activate();
        } catch (InitializationException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CouldNotPerformException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init() {

        try {
            //listener.activate();

            LocationRemote locationRemote = new LocationRemote();
            locationRemote.init(locationRegistryRemote.getRootLocationConfig());
            locationRemote.activate();
            locationRemote.addDataObserver(new LocationObserver());
//            locationRemote.getPowerConsumption();
            List<UnitConfigType.UnitConfig> motionSensors = deviceRegistryRemote.getUnitConfigs(UnitTemplateType.UnitTemplate.UnitType.MOTION_SENSOR);
            List<MotionSensorRemote> motionSensorRemotes = new ArrayList<>();

            MotionSensorRemote remote;
            for (UnitConfigType.UnitConfig motionSensorConfig : motionSensors) {
                remote = new MotionSensorRemote();
                remote.init(motionSensorConfig);
                remote.activate();
                motionSensorRemotes.add(remote);
                remote.addDataObserver(new MotionSensorObserver(motionSensorConfig.getScope().toString()));
            }

        } catch (InstantiationException | InitializationException | InterruptedException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CouldNotPerformException ex) {
            Logger.getLogger(Remotes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //listener.deactivate();
            deviceRegistryRemote.shutdown();

        }

    }

}
