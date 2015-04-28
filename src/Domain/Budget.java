package Domain;

/**
 * Created by Lasse on 27-04-2015.
 */
public class Budget {
    int initial_budget;
    int year;
    int quarter;
    int reserved;
    int reimbursed;

    public Budget(int initial_budget, int year, int quarter, int reimbursed, int reserved) {
        this.initial_budget = initial_budget;
        this.year = year;
        this.quarter = quarter;
        this.reimbursed = reimbursed;
        this.reserved = reserved;
    }

    public int getInitial_budget() {
        return initial_budget;
    }

    public int getYear() {
        return year;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getReimbursed() {
        return reimbursed;
    }

    public int getReserved() {
        return reserved;
    }

    public int getLeftAvailable() {
        return initial_budget - reserved - reimbursed;
    }
}
