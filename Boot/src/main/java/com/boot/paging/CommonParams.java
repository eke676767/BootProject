package com.boot.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommonParams {
	private int page;
    private int recordPerPage;
    private int pageSize;
    private String keyword;
    private String searchType;
    private Pagination pagination;
}
