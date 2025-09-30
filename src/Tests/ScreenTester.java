package Tests;

import SecondLevel.Customer;

/**
 * This file is to test screen usage with main
 */
public class ScreenTester {
    public static Customer customer;

    public static void main(String[] args) {
        customer = new Customer();
        customer.setWelcome();
    }
}
