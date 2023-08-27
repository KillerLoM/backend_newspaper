package com.example.backend.Response;
import com.example.backend.Newspapers.newspaper;

import java.util.List;

public class NewspaperResponse {
    private List<newspaper> newspapers;
    private int pageIndex;
    private int pageSize;
    private int total;

    public List<newspaper> getNewspapers() {
        return newspapers;
    }

    public void setNewspapers(List<newspaper> newspapers) {
        this.newspapers = newspapers;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
