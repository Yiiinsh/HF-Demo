package org.tju.HFDemo.core.constant;

/**
 * Created by shaohan.yin on 04/05/2017.
 */
public enum HFOperations {
    INSERT("insert"),
    INSERT_BATCH("insertBatch"),
    UPDATE("update"),
    UPDATE_BATCH("updateBatch"),
    REMOVE("remove"),
    REMOVE_BATCH("removeBatch"),
    QUERY("query"),
    QUERY_BATCH("queryBatch"),
    QUERY_RECRUITMENT("queryRecruitment");

    private String value;

    HFOperations(String val) {
        this.value = val;
    }

    public String val() {
        return this.value;
    }
}
