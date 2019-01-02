package org.openbase.bco.eveson;

/*-
 * #%L
 * BCO Eveson
 * %%
 * Copyright (C) 2014 - 2019 openbase.org
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

import java.nio.ByteBuffer;
import rsb.Factory;
import rsb.config.ParticipantConfig;
import rsb.config.TransportConfig;
import rsb.converter.ConversionException;
import rsb.converter.Converter;
import rsb.converter.ConverterRepository;
import rsb.converter.ConverterSelectionStrategy;
import rsb.converter.ConverterSignature;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.UserData;
import rsb.converter.WireContents;

/**
 *
 * @author michael
 * @author mpohling
 */
public class RSBGenericConverterConfig {

    public static ParticipantConfig generateConfig() {
        
        ParticipantConfig config = Factory.getInstance().getDefaultParticipantConfig().copy();

        for (TransportConfig name : config.getTransports().values()) {
            ConverterRepository<?> converters = new ConverterRepository<ByteBuffer>() {

                @Override
                public ConverterSelectionStrategy<ByteBuffer> getConvertersForSerialization() {
                    return new DefaultConverterRepository<ByteBuffer>().getConvertersForSerialization();
                }

                @Override
                public ConverterSelectionStrategy<ByteBuffer> getConvertersForDeserialization() {
                    return new ConverterSelectionStrategy<ByteBuffer>() {

                        @Override
                        public Converter<ByteBuffer> getConverter(String string) {
                            return new Converter<ByteBuffer>() {

                                @Override
                                public WireContents<ByteBuffer> serialize(Class<?> type, Object o) throws ConversionException {
                                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                }

                                @Override
                                public UserData deserialize(String string, ByteBuffer wt) throws ConversionException {
                                    UserData data = new UserData(wt, ByteBuffer.class);
                                    return data;
                                }

                                @Override
                                public ConverterSignature getSignature() {
                                    ConverterSignature cs = new ConverterSignature("all", ByteBuffer.class);
                                    return cs;
                                }

                            };
                        }
                    };
                }

                @Override
                public void addConverter(Converter<ByteBuffer> converter) {
//                    repository.addConverter(converter);
//                    converter.getSignature().getDataType().get
//                    System.out.println("Registering converter for 1");
//                    System.out.println("Adding converter.");
                }
            };

            name.setConverters(converters);
        }
        return config;
    }
}
