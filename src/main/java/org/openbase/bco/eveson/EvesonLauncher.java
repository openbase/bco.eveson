package org.openbase.bco.eveson;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2021 openbase.org
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

import org.openbase.bco.authentication.lib.BCO;
import org.openbase.bco.eveson.jp.JPAudioOutputDevice;
import org.openbase.bco.eveson.jp.JPAudioResoureFolder;
import org.openbase.bco.eveson.jp.JPAudioVolume;
import org.openbase.bco.eveson.jp.JPThemeFile;
import org.openbase.jps.core.JPService;
import org.openbase.jps.preset.JPShowGUI;
import org.openbase.jul.pattern.launch.AbstractLauncher;

/**
 *
 * @author divine
 */
public class EvesonLauncher extends AbstractLauncher<Eveson> {

    public EvesonLauncher() throws org.openbase.jul.exception.InstantiationException {
        super(Eveson.class, Eveson.class);
    }

    @Override
    public void loadProperties() {
        JPService.registerProperty(JPAudioResoureFolder.class);
        JPService.registerProperty(JPAudioOutputDevice.class);
        JPService.registerProperty(JPAudioVolume.class);
        JPService.registerProperty(JPShowGUI.class, false);
        JPService.registerProperty(JPThemeFile.class);
    }

    public static void main(String[] args) throws Throwable {
        BCO.printLogo();
        main(BCO.class, Eveson.class, args, EvesonLauncher.class);
    }
}
