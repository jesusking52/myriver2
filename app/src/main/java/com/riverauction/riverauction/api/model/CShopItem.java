package com.riverauction.riverauction.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class CShopItem {

    @JsonProperty("sku_id")
    private String skuId;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("bonus_description")
    private String bonusDescription;

    @JsonProperty("bonus_description2")
    private String bonusDescription2;
    @JsonProperty("price")
    private Integer price;


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getBonusDescription() {
        return bonusDescription;
    }

    public void setBonusDescription(String bonusDescription) {
        this.bonusDescription = bonusDescription;
    }

    public String getBonusDescription2() {
        return bonusDescription2;
    }

    public void setBonusDescription2(String bonusDescription2) {
        this.bonusDescription2 = bonusDescription2;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
