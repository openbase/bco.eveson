package de.citec.csra.remotes;

import de.citec.csra.EventPlayer;
import de.citec.csra.ScopePlayer;
import java.util.Map;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import static rst.homeautomation.state.MotionStateType.MotionState.State.MOVEMENT;
import rst.homeautomation.unit.MotionSensorType;

/**
 *
 * @author mgao
 */
public class MotionSensorObserver implements Observer<MotionSensorType.MotionSensor> {

    private ScopePlayer sp;
    private String sampleFile;
    private final String id; 

    public MotionSensorObserver(String id) throws InterruptedException, InstantiationException {
        final Map<String, ScopePlayer> scopeSampleMap = EventPlayer.getInstance().getScopeSampleMap();
        this.id = id;
        if (scopeSampleMap.containsKey(id)) {
            final ScopePlayer player = scopeSampleMap.get(id);
            sampleFile = player.getSampleFile();
            
            sp = new ScopePlayer(sampleFile, ScopePlayer.Type.PLAY,player.getMAX_VOICES(), player.getRelativeAmplitude());
        }
    }

    @Override
    public void update(Observable<MotionSensorType.MotionSensor> source, MotionSensorType.MotionSensor data) throws Exception {
        
//        System.out.println("MotionSensorObserver: " + data.getMotionState().getValue() + " in " + id);
        if (data.getMotionState().getValue().equals(MOVEMENT) ) {
//            System.out.println("Motion Sensor:" + sampleFile);
            sp.play(1);
        }
    }

}
