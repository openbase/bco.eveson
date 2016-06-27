/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.remotes;

import de.citec.csra.ScopePlayer;
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

    ScopePlayer sp;
    String sampleFile;

    public MotionSensorObserver(String id) throws InterruptedException, InstantiationException{
        // todo: get sample from map
        sp = new ScopePlayer(sampleFile, ScopePlayer.Type.PLAY);
    }

    @Override
    public void update(Observable<MotionSensorType.MotionSensor> source, MotionSensorType.MotionSensor data) throws Exception {
        if (data.getMotionState().getValue().equals(MOVEMENT) && sampleFile != null) {
            sp.play(1);
        }
    }

}
