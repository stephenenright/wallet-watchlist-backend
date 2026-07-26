/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.models.entity.generator;

import com.github.stephenenright.walletwatchlist.web.api.common.util.UUIDUtils;
import java.lang.reflect.Member;
import java.util.EnumSet;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.descriptor.java.UUIDJavaType;

public class IdUuidTimeBasedGeneratorType implements BeforeExecutionGenerator {
	private UUIDJavaType.ValueTransformer valueTransformer;

	private IdUuidTimeBasedGeneratorType(Member idMember) {
		configure(idMember);
	}

	public IdUuidTimeBasedGeneratorType(IdUuidTimeBasedGenerator config, Member member,
			CustomIdGeneratorCreationContext context) {
		configure(member);
	}

	public IdUuidTimeBasedGeneratorType(IdUuidTimeBasedGenerator config, Member member,
			GeneratorCreationContext context) {
		configure(member);
	}

	private void configure(Member idMember) {
		final Class<?> propertyType = ReflectHelper.getPropertyType(idMember);

		if (UUID.class.isAssignableFrom(propertyType)) {
			valueTransformer = UUIDJavaType.PassThroughTransformer.INSTANCE;
		} else if (String.class.isAssignableFrom(propertyType)) {
			valueTransformer = UUIDJavaType.ToStringTransformer.INSTANCE;
		} else if (byte[].class.isAssignableFrom(propertyType)) {
			valueTransformer = UUIDJavaType.ToBytesTransformer.INSTANCE;
		} else {
			throw new HibernateException(
					"Unanticipated return type [" + propertyType.getName() + "] for UUID conversion");
		}
	}

	@Override
	public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o, Object o1,
			EventType eventType) {

		return valueTransformer.transform(UUIDUtils.createTimeOrderedV7());
	}

	@Override
	public EnumSet<EventType> getEventTypes() {
		return EventTypeSets.INSERT_ONLY;
	}
}
