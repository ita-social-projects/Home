package com.softserveinc.ita.homeproject.homeservice.mapper.config.converter;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination.BaseModelDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination.Container1Dto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination.Inner1Dto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination.InnerDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination.InnerItem1Dto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source.BaseModel;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source.ChildContainer1;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source.Container;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source.Inner1;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source.InnerItem1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ConditionalConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AbstractTypeConverterTest {

    private static final String DESTINATION_PACKAGE_PREFIX = "com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.destination";
    private static final String SOURCE_PACKAGE_PREFIX = "com.softserveinc.ita.homeproject.homeservice.mapper.config.converter.classes.source";

    private static Stream<MapperProvider> mappersProvider() {
        return Stream.of(
                MapperProvider.create((modelMapper) -> {
                    ConditionalConverter<BaseModel, BaseModelDto> conditionalConverter =
                            new AbstractTypeConverter<>(SOURCE_PACKAGE_PREFIX, DESTINATION_PACKAGE_PREFIX);
                    modelMapper.getConfiguration().getConverters().add(0, conditionalConverter);
                })
        );
    }

    @ParameterizedTest
    @MethodSource("mappersProvider")
    void testMapperWithAbstractTypeConverter(MapperProvider mapperProvider) {
        ModelMapper modelMapper = mapperProvider.getModelMapper();

        Container childContainer1 = new ChildContainer1();

        Inner1 inner = new Inner1();
        String onParent = "Opa";
        inner.setOnParent(onParent);
        String textOnInner1 = "OpaChild";
        inner.setOnChild1(textOnInner1);
        InnerItem1 innerItem1 = new InnerItem1();
        int amount = 15;
        innerItem1.setAmount(amount);
        inner.setInnerItemList(Collections.singletonList(innerItem1));
        childContainer1.setOuterField(inner);

        Container1Dto map = modelMapper.map(childContainer1, Container1Dto.class);

        InnerDto outerField = map.getOuterField();
        assertTrue(outerField instanceof Inner1Dto);
        Inner1Dto inner1Dto = (Inner1Dto) outerField;
        assertEquals(inner1Dto.getOnChild1(), textOnInner1);
        assertEquals(inner1Dto.getInnerItemList().size(), 1);
        assertTrue(inner1Dto.getInnerItemList().get(0) instanceof InnerItem1Dto);
        InnerItem1Dto innerItem1Dto = (InnerItem1Dto) inner1Dto.getInnerItemList().get(0);
        assertEquals(innerItem1Dto.getAmount(), amount);
    }

    protected static class MapperProvider {
        private final ModelMapper modelMapper;

        private MapperProvider(Consumer<ModelMapper> config) {
            modelMapper = new ModelMapper();
            config.accept(this.modelMapper);
        }

        public static MapperProvider create(Consumer<ModelMapper> config) {
            return new MapperProvider(config);
        }

        public ModelMapper getModelMapper() {
            return modelMapper;
        }
    }

}