package org.openbase.bco.eveson.jp;

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

import org.openbase.jps.exception.JPNotAvailableException;
import org.openbase.jps.preset.AbstractJPFloat;

/**
 *
 * @author mgao
 */
public class JPAudioVolume extends AbstractJPFloat {

    public final static String[] COMMAND_IDENTIFIERS = {"-a", "--amplitude"};
    
    public JPAudioVolume() {
        super(COMMAND_IDENTIFIERS);
    }
    
    @Override
    public String getDescription() {
        return "This property controls the volume of the application. 1.0 = Maximum volume";
    }

    @Override
    protected Float getPropertyDefaultValue() throws JPNotAvailableException {
        return 1.0f;
    }
    
}
