/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.models.entity.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;

@IdGeneratorType(IdUuidTimeBasedGeneratorType.class)
@ValueGenerationType(generatedBy = IdUuidTimeBasedGeneratorType.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
public @interface IdUuidTimeBasedGenerator {
}
