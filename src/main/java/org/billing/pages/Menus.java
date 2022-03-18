package org.billing.pages;

import javafx.scene.control.Button;

public class Menus extends Baseclass {

    private final Button showBills;

    private final Button Dashboard;
    private final Button close;

    private final Dashboard dashboard = new Dashboard();
    private final ShowBills showBill = new ShowBills();

    public Menus(Button... buttons) {
        this.showBills = buttons[0];
        this.Dashboard = buttons[1];
        this.close = buttons[2];
    }

    public void setAction() {

        this.showBills.setOnMouseClicked(event -> showBill.loadShowBills());

        this.Dashboard.setOnMouseClicked(event -> dashboard.showDashboard());


        this.close.setOnMouseClicked(event -> System.exit(0));
    }

}
