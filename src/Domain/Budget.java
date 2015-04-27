package Domain;

/**
 * Created by Lasse on 27-04-2015.
 */
public class Budget {
    int id;
    int initial_budget;
    int year;
    int quarter;
    int reimbursed;
    int reserved;

    public Budget(int id, int initial_budget, int year, int quarter, int reimbursed, int reserved) {
        this.id = id;
        this.initial_budget = initial_budget;
        this.year = year;
        this.quarter = quarter;
        this.reimbursed = reimbursed;
        this.reserved = reserved;
    }

    public int getId() {
        return id;
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
}
