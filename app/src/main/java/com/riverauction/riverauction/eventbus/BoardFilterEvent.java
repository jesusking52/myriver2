package com.riverauction.riverauction.eventbus;

import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;

public class BoardFilterEvent {
    private GetBoardsParams.Builder builder;
    public BoardFilterEvent() {
    }

    public BoardFilterEvent(GetBoardsParams.Builder builder) {
        this.builder = builder;
    }

    public GetBoardsParams.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(GetBoardsParams.Builder builder) {
        this.builder = builder;
    }
}
