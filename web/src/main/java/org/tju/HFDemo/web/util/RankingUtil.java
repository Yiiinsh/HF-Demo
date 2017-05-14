package org.tju.HFDemo.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.HFDemo.common.config.Config;
import org.tju.HFDemo.core.dto.RecruitmentInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by shaohan.yin on 14/05/2017.
 */
public class RankingUtil {
    private static String RANKING_RATIO_KEY = "ranking.ratio";
    private static String DATE_BASE_PRIORITY_KEY = "date.base.priority";
    private static String PRIORITY_SUFFIX = ".priority";

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Config config = Config.DEFAULT;
    private DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");

    public List<RecruitmentInfo> sortByRank(List<RecruitmentInfo> list) {
        list.sort((infoA, infoB) -> {
            try {
                boolean isAOverdue = isOverdue(infoA);
                boolean isBOverdue = isOverdue(infoB);
                Date startDateA = dft.parse(infoA.getStart());
                Date startDateB = dft.parse(infoB.getStart());

                if(isAOverdue && isBOverdue) {
                    return startDateA.after(startDateB) ? 1 : -1;
                } else if (isAOverdue && !isBOverdue) {
                    return -1;
                } else if (!isAOverdue && isBOverdue) {
                    return 1;
                } else {
                    double rankingRatio = config.getDouble(RANKING_RATIO_KEY, 0.3);
                    double dateBasePriority = config.getDouble(DATE_BASE_PRIORITY_KEY, 1);
                    double basePriorityA = config.getDouble(infoA.getCompany() + PRIORITY_SUFFIX, 1);
                    double basePriorityB = config.getDouble(infoB.getCompany() + PRIORITY_SUFFIX, 1);

                    // compare priority
                    double priorityA = 0, priorityB = 0;
                    if(startDateA.after(startDateB)) {
                        priorityA = dateBasePriority * rankingRatio + basePriorityA * (1 - rankingRatio);
                        priorityB = basePriorityB * (1 - rankingRatio);
                    } else {
                        priorityA = basePriorityA * (1 - rankingRatio);
                        priorityB = dateBasePriority * rankingRatio + basePriorityB * (1 - rankingRatio);
                    }

                    return priorityA > priorityB? 1 : -1;
                }
            } catch (ParseException e) {
                logger.error("[parse date][fail]", e);
            }
            return -1;
        });

        return list;
    }

    private boolean isOverdue(RecruitmentInfo info) {
        try {
            return dft.parse(info.getEnd()).after(new Date());
        } catch (ParseException e) {
            logger.error("[isOverdue]", e);
        }
        return false;
    }

}
