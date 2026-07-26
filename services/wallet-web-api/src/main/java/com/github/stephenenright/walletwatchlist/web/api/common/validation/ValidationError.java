/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.validation;

public abstract class ValidationError {

	private String label;

	public ValidationError(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public abstract Object getErrors();
}
