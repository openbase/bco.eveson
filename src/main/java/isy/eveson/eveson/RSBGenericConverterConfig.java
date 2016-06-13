package isy.eveson.eveson;

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
