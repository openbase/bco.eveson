/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openbase.bco.eveson.jp;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2017 openbase.org
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
