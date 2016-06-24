/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.remotes;

import de.citec.csra.ScopePlayer;
import org.openbase.jul.pattern.Observable;
import org.openbase.jul.pattern.Observer;
import static rst.homeautomation.state.MotionStateType.MotionState.State.MOVEMENT;
import rst.homeautomation.unit.MotionSensorType;

/**
 *
 * @author mgao
 */
public class MotionSensorObserver implements Observer<MotionSensorType.MotionSensor> {

    String sampleFile;

    public MotionSensorObserver(String scope) {
        switch (scope) {
            case "/home/living/motionsensor/couch/":
                sampleFile = "/birds/1.wav";
                break;
            case "/home/living/motionsensor/table/":
                sampleFile = "/birds/2.wav";
                break;
            case "/home/living/motionsensor/media/":
                sampleFile = "/birds/3.wav";
                break;
            case "/home/wardrobe/motionsensor/entrance/":
                sampleFile = "/birds/4.wav";
                break;
            case "/home/wardrobe/motionsensor/hallway/":
                sampleFile = "/birds/5.wav";
                break;
            case "/home/sports/motionsensor/interaction/":
                sampleFile = "/birds/7.wav";
                break;
            case "/home/sports/motionsensor/pathway/":
                sampleFile = "/birds/8.wav";
                break;
            case "/home/kitchen/motionsensor/global/":
                sampleFile = "/birds/9.wav";
                break;
            case "/home/bath/motionsensor/global/":
                sampleFile = "/birds/10.wav";
                break;
            case "/home/bath/motionsensor/entrance/":
                sampleFile = "/birds/11.wav";
                break;
                
        }
    }

    @Override
    public void update(Observable<MotionSensorType.MotionSensor> source, MotionSensorType.MotionSensor data) throws Exception {
        ScopePlayer sp = new ScopePlayer(sampleFile, ScopePlayer.Type.PLAY);
        if (data.getMotionState().getValue().equals(MOVEMENT) && sampleFile != null) {
            sp.play(1);
        }
    }

}
