package com.riverauction.riverauction.api.service.board.params;

import java.util.HashMap;

public class GetBoardsParams extends HashMap<String, String> {

    private GetBoardsParams(Integer user_id,
                            Integer universities,
                            Integer high,
                            Integer middle,
                            Integer overseas,
                            Integer human,
                            Integer nature,
                            Integer other,
                            Integer student,
                            Integer parents,
                            Integer board_idx,
                            Integer reply_idx,
                            Integer category_id,
                            String category2_id,
                            String content,
                            Integer nextToken,
                            Integer view_Cnt) {


        if (user_id != null) {
            put("user_id", String.valueOf(user_id));
        }

        if (universities != null) {
            put("universities", String.valueOf(universities));
        }

        if (high != null) {
            put("high", String.valueOf(high));
        }

        if (middle != null) {
            put("middle", String.valueOf(middle));
        }

        if (overseas != null) {
            put("overseas", String.valueOf(overseas));
        }

        if (human != null) {
            put("human", String.valueOf(human));
        }

        if (nature != null) {
            put("nature", String.valueOf(nature));
        }

        if (other != null) {
            put("other", String.valueOf(other));
        }

        if (student != null) {
            put("student", String.valueOf(student));
        }

        if (board_idx != null) {
            put("board_idx", String.valueOf(board_idx));
        }

        if (reply_idx != null) {
            put("reply_idx", String.valueOf(reply_idx));
        }

        if (category_id != null) {
            put("category_id", String.valueOf(category_id));
        }

        if (category2_id != null) {
            put("category2_id", String.valueOf(category2_id));
        }

        if (view_Cnt != null) {
            put("view_Cnt", String.valueOf(view_Cnt));
        }

        if (content != null) {
            put("content", String.valueOf(content));
        }

        if (parents != null) {
            put("parents", String.valueOf(parents));
        }

        if (nextToken != null) {
            put("next_token", String.valueOf(nextToken));
        }
    }

    public static class Builder {
        private Integer user_id;
        private Integer universities;
        private Integer high;
        private Integer middle;
        private Integer overseas;
        private Integer human;
        private Integer nature;
        private Integer other;
        private Integer student;
        private Integer parents;
        private Integer board_idx;
        private Integer reply_idx;
        private Integer view_Cnt;
        private Integer nextToken;
        public Integer category_id;
        private String category2_id;
        private String content;

        public Builder setuser_id(Integer user_id) {
            this.user_id = user_id;
            return this;
        }

        public Builder setboard_idx(Integer board_idx) {
            this.board_idx = board_idx;
            return this;
        }

        public Builder setreply_idx(Integer reply_idx) {
            this.reply_idx = reply_idx;
            return this;
        }

        public Builder setView_Cnt(Integer view_Cnt) {
            this.view_Cnt = view_Cnt;
            return this;
        }

        public Builder setUniversities(Integer universities) {
            this.universities = universities;
            return this;
        }

        public Builder setHigh(Integer high) {
            this.high = high;
            return this;
        }

        public Builder setMiddle(Integer middle) {
            this.middle = middle;
            return this;
        }

        public Builder setOverseas(Integer overseas) {
            this.overseas = overseas;
            return this;
        }

        public Builder setHuman(Integer human) {
            this.human = human;
            return this;
        }

        public Builder setNature(Integer nature) {
            this.nature = nature;
            return this;
        }

        public Builder setOther(Integer other) {
            this.other = other;
            return this;
        }

        public Builder setStudent(Integer student) {
            this.student = student;
            return this;
        }

        public Builder setParents(Integer parents) {
            this.parents = parents;
            return this;
        }

        public Builder setcontent(String content) {
            this.content = content;
            return this;
        }

        public Builder setNextToken(Integer nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public Builder setCateogry2Id(String category2_id) {
            this.category2_id = category2_id;
            return this;
        }

        public Builder setCateogryId(Integer category_id) {
            this.category_id = category_id;
            return this;
        }
        public GetBoardsParams build() {
            return new GetBoardsParams(user_id, universities, high, middle, overseas, human, nature, other, student, parents, board_idx, reply_idx, category_id, category2_id, content, nextToken, view_Cnt);
        }
    }
}
