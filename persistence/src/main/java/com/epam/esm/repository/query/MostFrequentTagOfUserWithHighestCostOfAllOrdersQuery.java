package com.epam.esm.repository.query;

public class MostFrequentTagOfUserWithHighestCostOfAllOrdersQuery implements NativeQuery {
    private static final String ID_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS = "SELECT id_user " +
            "FROM (SELECT id_user, max(sum_cost) OVER (PARTITION BY id_user) AS max_sum_cost FROM (SELECT id_user, " +
            "sum(cost) AS sum_cost FROM purchase GROUP BY id_user) AS sub1) AS sub2 LIMIT 1";
    private static final String MOST_RECENT_TAG_ID_OF_USER_ORDERS = "SELECT id_tag FROM (SELECT id_tag, max(cnt) OVER " +
            "(PARTITION BY id_tag) AS max_cnt FROM (SELECT id_tag, count(*) OVER (PARTITION BY id_tag) AS cnt FROM " +
            "certificate_tag WHERE id_certificate in (SELECT id_certificate FROM purchase_certificate AS oc JOIN " +
            "purchase AS co ON oc.id_purchase = co.id WHERE co.id_user = (%s))) as sub3 ) as sub4 LIMIT 1";

    @Override
    public String getQuery() {
        String mostRecentTagOfUserOrders =
                String.format(MOST_RECENT_TAG_ID_OF_USER_ORDERS, ID_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS);
        return String.format("SELECT * FROM tag WHERE id = (%s)", mostRecentTagOfUserOrders);
    }
}
