package edu.cit.tabungar.stevenjan.campusequipmentloan.Repository;

import edu.cit.tabungar.stevenjan.campusequipmentloan.Model.Loan;

import java.time.temporal.*;

public interface Rules {
    double calculatePenalty(Loan loan);


    public static class DailyRules implements Rules {
        private final double dailyPenalty;

        public DailyRules(double dailyPenalty) {
            this.dailyPenalty = dailyPenalty;
        }

        @Override
        public double calculatePenalty(Loan loan) {
            if (loan.getReturnDate() == null || !loan.getReturnDate().isAfter(loan.getDueDate())) {
                return 0;
            }
            long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate());
            return daysLate * dailyPenalty;
        }
    }

}
