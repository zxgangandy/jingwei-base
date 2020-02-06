package io.jingwei.base.utils.model;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;


public interface BasicObjectMapper<SOURCE, TARGET> {
    @Mappings({})
    @InheritConfiguration
    TARGET to(SOURCE source);

    @InheritConfiguration
    List<TARGET> to(Collection<SOURCE> source);

    @InheritInverseConfiguration
    SOURCE from(TARGET source);

    @InheritInverseConfiguration
    List<SOURCE> from(Collection<TARGET> source);
}
