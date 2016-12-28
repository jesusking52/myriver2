package com.riverauction.riverauction.api.service.board.params;

import java.util.HashMap;

public class GetBoardsParams extends HashMap<String, String> {

    private GetBoardsParams(Integer favorite,
                            Integer universities,
                            Integer high,
                            Integer middle,
                            Integer overseas,
                            Integer human,
                            Integer nature,
                            Integer other,
                            Integer student,
                            Integer parents,
                            String search,
                            Integer nextToken) {


        if (favorite != null) {
            put("favorite", String.valueOf(favorite));
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

        if (search != null) {
            put("search", String.valueOf(search));
        }

        if (parents != null) {
            put("parents", String.valueOf(parents));
        }

        if (nextToken != null) {
            put("next_token", String.valueOf(nextToken));
        }
    }

    public static class Builder {
        private Integer favorite;
        private Integer universities;
        private Integer high;
        private Integer middle;
        private Integer overseas;
        private Integer human;
        private Integer nature;
        private Integer other;
        private Integer student;
        private Integer parents;
        private Integer nextToken;
        private String search;

        public Builder setFavorite(Integer favorite) {
            this.favorite = favorite;
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

        public Builder setSearch(String search) {
            this.search = search;
            return this;
        }

        public Builder setNextToken(Integer nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public GetBoardsParams build() {
            return new GetBoardsParams(favorite, universities, high, middle, overseas, human, nature, other, student, parents,search, nextToken);
        }
    }
}
