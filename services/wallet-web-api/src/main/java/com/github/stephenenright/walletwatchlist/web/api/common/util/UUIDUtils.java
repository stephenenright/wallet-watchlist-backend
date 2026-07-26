/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import java.util.UUID;

public final class UUIDUtils {

	private static final TimeBasedEpochGenerator V7_GENERATOR = Generators.timeBasedEpochGenerator();

	private UUIDUtils() {
	}

	/**
	 * Creates a time-ordered (RFC 9562 version 7) UUID. The most significant bits
	 * are derived from the current Unix timestamp in milliseconds, making generated
	 * identifiers monotonically increasing and index friendly.
	 */
	public static UUID createTimeOrderedV7() {
		return V7_GENERATOR.generate();
	}
}
