package com.hp.ocr_googlevisionapi.models;

import java.util.List;

public class HistoryModel {
    /**
     * status : success
     * amount_details : [{"id":"1","amount":"50"}]
     */

    private String status;
    private List<AmountDetailsBean> amount_details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AmountDetailsBean> getAmount_details() {
        return amount_details;
    }

    public void setAmount_details(List<AmountDetailsBean> amount_details) {
        this.amount_details = amount_details;
    }

    public static class AmountDetailsBean {
        /**
         * id : 1
         * amount : 50
         */

        private String id;
        private String amount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
