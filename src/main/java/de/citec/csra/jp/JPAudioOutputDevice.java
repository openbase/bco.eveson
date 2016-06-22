/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.csra.jp;

import com.jsyn.devices.AudioDeviceFactory;
import com.jsyn.devices.AudioDeviceManager;
import java.util.List;
import org.openbase.jps.exception.JPBadArgumentException;
import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.AbstractJPString;

/**
 *
 * @author mgao
 */
public class JPAudioOutputDevice extends AbstractJPString {

    public final static String[] COMMAND_IDENTIFIERS = {"-o", "--audio-output-device"};
    private int audioOutputDeviceId;

    public JPAudioOutputDevice() {
        super(COMMAND_IDENTIFIERS);
    }

    @Override
    protected String getPropertyDefaultValue() throws JPNotAvailableException {
        AudioDeviceManager audioManager = AudioDeviceFactory.createAudioDeviceManager();
        audioOutputDeviceId = audioManager.getDefaultOutputDeviceID();
        return audioManager.getDeviceName(audioOutputDeviceId);
    }

    @Override
    protected String parse(List<String> arguments) throws JPBadArgumentException {
        checkArgumentCount(1);
        String selectedDevice = arguments.get(0).toLowerCase();
        int selectedDeviceId = -1;

        AudioDeviceManager audioManager = AudioDeviceFactory.createAudioDeviceManager();

        // find defined audio device
        for (int i = 0; i < audioManager.getDeviceCount(); i++) {
            if (audioManager.getDeviceName(i).toLowerCase().contains(selectedDevice)) {
                selectedDeviceId = i;
                System.out.println("found audio device: "+audioManager.getDeviceName(i) +"["+audioManager.getMaxInputChannels(selectedDeviceId) + ":in|"+ audioManager.getMaxOutputChannels(selectedDeviceId)+":out]");
            }
        }

        if (selectedDeviceId == -1) {
            throw new JPBadArgumentException("The specified AudioDevice[" + arguments.get(0) + "] could not be found!");
        }

        audioOutputDeviceId = selectedDeviceId;
        return audioManager.getDeviceName(selectedDeviceId);
    }

    public int getAudioOutputDeviceId() {
        return audioOutputDeviceId;
    }

    @Override
    public String getDescription() {
        return "This property configure the audio output device.";
    }
}
