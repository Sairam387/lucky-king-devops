package com.sairam.game;

import java.util.List;

public class WalletTransactionTest {

    public static void main(String[] args) {

        try {

            WalletTransactionDao dao =
                    new WalletTransactionDao();

            List<Object[]> transactions =
                    dao.getAllTransactions();

            System.out.println(
                    "Total transactions: "
                            + transactions.size()
            );

            for (Object[] transaction :
                    transactions) {

                System.out.println(
                        transaction[0]
                                + " | "
                                + transaction[1]
                                + " | "
                                + transaction[2]
                                + " | "
                                + transaction[3]
                                + " | "
                                + transaction[4]
                                + " | "
                                + transaction[5]
                                + " | "
                                + transaction[6]
                                + " | "
                                + transaction[7]
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}