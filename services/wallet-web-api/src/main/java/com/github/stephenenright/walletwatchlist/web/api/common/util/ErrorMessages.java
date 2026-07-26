package com.github.stephenenright.walletwatchlist.web.api.common.util;

/**
 * Error message provider.
 *
 * <p>
 * Vendored from the {@code common-models-lib} monorepo library. The original
 * resolved messages through an i18n {@code Messages}/resource-bundle
 * infrastructure that is not present in this service, so this trimmed copy
 * returns literal English strings for only the methods actually referenced by
 * the copied models and validation code. Wire this up to a
 * {@code MessageSource} if localized messages are needed later.
 */
public final class ErrorMessages {

	private ErrorMessages() {
	}

	public static String errorAuthForbidden() {
		return "You are not authorized to perform this action.";
	}

	public static String errorValidationFailed() {
		return "Validation failed.";
	}

	public static String itemNotFound() {
		return "The requested item could not be found.";
	}

	public static String errorDelete(Class<?> cls) {
		return cls == null ? "Unable to delete the item." : "Unable to delete the " + cls.getSimpleName() + ".";
	}

	public static String errorUpdate(Class<?> cls) {
		return cls == null ? "Unable to update the item." : "Unable to update the " + cls.getSimpleName() + ".";
	}

	public static String errorRequired() {
		return "This field is required.";
	}

	public static String errorGenericValueExists() {
		return "The value already exists.";
	}

	public static String errorDoesNotExist(String label) {
		return label + " does not exist.";
	}

	public static String errorMinLength(int minLength) {
		return "Must be at least " + minLength + " characters.";
	}

	public static String dateInvalid() {
		return "The date is invalid.";
	}

}
