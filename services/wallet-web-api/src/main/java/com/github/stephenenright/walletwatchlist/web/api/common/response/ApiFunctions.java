/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import java.util.List;
import org.springframework.data.domain.Page;

public abstract class ApiFunctions {

	public interface ListPagedApiCall<RT> {
		GetResponse<Page<RT>> execute();
	}

	public interface ListApiCall<RT> {
		GetResponse<List<RT>> execute();
	}

	public interface GetApiCall<RT> {
		GetResponse<RT> execute();
	}

	public interface CreateApiCall<RT> {
		CreateResponse<RT> execute();
	}

	public interface UpdateApiCall<RT> {
		UpdateResponse<RT> execute();
	}

	public interface DeleteApiCall<RT> {
		DeleteResponse<RT> execute();
	}
}
