package Domain;

import DataLayer.DatabaseFacade;

import java.util.ArrayList;

/**
 * Created by Lasse on 23-04-2015.
 */
public class Statistics {

    // Call for a specific statistic

    public ArrayList getStatistic(String statisticType, DatabaseFacade facade) {

        switch (statisticType) {
            case "Amount_DeniedClaims":
                // do something
                // return getAmountDeniedClaims
                break;
            case "Amount_CompletedProjects":
                // do something
                break;
            default:
                break;
        }
        return null;
    }





}
