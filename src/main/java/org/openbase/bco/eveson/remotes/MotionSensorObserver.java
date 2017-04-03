package org.openbase.bco.eveson.remotes;

import org.openbase.bco.eveson.EventPlayer;
import org.openbase.bco.eveson.ScopePlayer;
import java.util.Map;
import org.openbase.jul.exception.InstantiationException;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import static rst.domotic.state.MotionStateType.MotionState.State.MOTION;
import rst.domotic.unit.dal.MotionDetectorDataType;
import rst.domotic.unit.dal.MotionDetectorDataType.MotionDetectorData;

/**
 *
 * @author mgao
 */
public class MotionSensorObserver implements Observer<MotionDetectorDataType.MotionDetectorData> {

    private ScopePlayer sp;
    private String sampleFile;
    private final String id;

    public MotionSensorObserver(String id) throws InterruptedException, InstantiationException {
        final Map<String, ScopePlayer> scopeSampleMap = EventPlayer.getInstance().getScopeSampleMap();
        this.id = id;
        if (scopeSampleMap.containsKey(id)) {
            final ScopePlayer player = scopeSampleMap.get(id);
            sampleFile = player.getSampleFile();

            sp = new ScopePlayer(sampleFile, ScopePlayer.Type.PLAY, player.getMAX_VOICES(), player.getRelativeAmplitude(), null);
        }
    }

    @Override
    public void update(Observable<MotionDetectorDataType.MotionDetectorData> source, MotionDetectorData data) throws Exception {

//        System.out.println("MotionSensorObserver: " + data.getMotionState().getValue() + " in " + id);
        if (data.getMotionState().getValue().equals(MOTION)) {
//            System.out.println("Motion Sensor:" + sampleFile);
            sp.play(1);
        }
    }

}
