package com.example.assignment1_421_kell;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;

public class MainMenuController {

    @FXML
    private Button btnConfirm;

    @FXML
    private Label lblGrandTotal, lblTotalDiscount, lblTotalTax;

    @FXML
    private Label lblChildTickets, lblAdultTickets, lblTicketPrices;

    @FXML
    private Label lblCity, lblCardNumber, lblExpirationDate, lblState, lblStreetAddress, lblZip, lblFirstName, lblMiddle, lblLastName, lblPhoneNumber;

    @FXML
    private Label lblDiscount, lblEncounterError;

    @FXML
    private ListView<String> lstPurchases;

    @FXML
    private RadioButton rdbPterodactyl, rdbStegosaurus, rdbTyrannosaurs;

    @FXML
    private TextField txtAdultEncounter, txtChildEncounter, txtAdultTicket, txtChildTicket;

    @FXML
    private TextField txtCardNumber, txtCity, txtExpirationDate, txtFirstName, txtMiddle, txtLastName, txtPhoneNumber, txtState, txtStreetAddress, txtZip;

    @FXML
    private TextField txtDiscount;

    @FXML
    private ToggleGroup tg;

    //declaring the prices for each package ticket
    double sAdult = 155.00;
    double tAdult = 219.75;
    double tChild = 195.50;
    double sChild = 120.25;
    double pAdult = 53.00;
    double pChild = 26.50;

    //declaring the prices for each experience ticket
    double rAdult = 80.00;
    double rChild = 55.00;

    //count of each ticket (adult, child, adult encounter, child encounter)
    int numOfAdults, numOfChildren, numOfAdultEncounter, numOfChildEncounter;

    //variable to hold the total price for adult & child tickets
    double adultPrice;
    double childPrice;

    //hold the receipt information
    double discount;
    final double SALES_TAX = .06;
    double grandTotal = 0;
    String selectedPackage;

    //style to change the text to easier identify errors
    String errorRed = "-fx-text-fill: red";
    String errorBlack = "-fx-text-fill: black";
    String receiptInfo;
    //setting all verification to false
    //changes to true when the text field is filled out correctly
    //confirm button will then become available
    boolean first = false, middle = false, last = false, street = false, city = false, state = false, zip = false, phone = false, cc = false, expiration = false, adultTickets = true, childTickets = true, adultExperience = true, childExperience = true, validDiscount = true;

    //method for when the app first loads
    //lots of listeners for text fields are included
    //listeners have to be called in the initialize method
    public void initialize() {
        //group the toggles
        tg = new ToggleGroup();
        //adding the toggles to the toggle group
        rdbPterodactyl.setToggleGroup(tg);
        rdbStegosaurus.setToggleGroup(tg);
        rdbTyrannosaurs.setToggleGroup(tg);
        //listening for a selection of a toggle (from the toggle group)
        tg.selectedToggleProperty().addListener((observableValue, oldValue, newValue) -> {
            //to hold which package is selected
            RadioButton sel = (RadioButton) newValue;
            //setting the text of the selected toggle
            selectedPackage = sel.getText();
            //setting the price of each ticket based on which package was selected
            //if Tyrannosaurs Terror is selected, set the price to tadult and tchild
            if (selectedPackage.equals("Tyrannosaurs Terror")) {
                //formatting the price label
                lblTicketPrices.setText("Adult: $" + String.format("%.2f", tAdult) + " Child: $" + String.format("%.2f", tChild));
                //setting the price
                adultPrice = tAdult;
                childPrice = tChild;
                //if Stegosaurus Plates is selected, set the price to sadult and schild
            } else if (selectedPackage.equals("Stegosaurus Plates")) {
                //formatting the price label
                lblTicketPrices.setText("Adult: $" + String.format("%.2f", sAdult) + " Child: $" + String.format("%.2f", sChild));
                //setting the price
                adultPrice = sAdult;
                childPrice = sChild;
                //if Pterodactyl Droppings is selected, set the price to padult and pchild
            } else {
                //formatting the price label
                lblTicketPrices.setText("Adult: $" + String.format("%.2f", pAdult) + " Child: $" + String.format("%.2f", pChild));
                //setting the price
                adultPrice = pAdult;
                childPrice = pChild;
            }

            //calling the calculate receipt function to update the live receipt
            //this function shows the package type, how many adult tickets, how many kid tickets, how many encounter tickets, and the running total
            calculateReceipt();

            //once the package is selected, make the ticket price label visible
            lblTicketPrices.setVisible(true);
            //once the package is selected, make the ticket count editable
            //this makes it harder for the user to make a mistake
            txtAdultTicket.setEditable(true);
            txtChildTicket.setEditable(true);
            txtDiscount.setEditable(true);
        });

        //listens to when the textfield (how many adult tickets text field) is changed
        txtAdultTicket.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //clear the number of encounter tickets (prevents having more encounter experience than actual tickets)
            txtAdultEncounter.clear();
            //set the number of adults to how many the user entered
            //also sets the text if there is an error, and changes it back to correct if the error is fixed
            numOfAdults = verificationTickets(txtAdultTicket, lblAdultTickets, "Adult Tickets: ");
            //if the number of adults is not 0, make the encounter text field editable
            if (numOfAdults > 0) {
                txtAdultEncounter.setEditable(true);
                adultTickets = true;
            } else {
                //catching invalid input
                txtAdultEncounter.setEditable(false);
                adultTickets = false;
            }
            //calling the calculate receipt function to update the live receipt
            //this will specifically update the grand total based on how many adult tickets*the adult price of the specific package
            calculateReceipt();
        });

        //listens to when the text field (how many child tickets textfield) is changed
        txtChildTicket.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //clear the number of encounter tickets (prevents having more encounter experience than actual tickets)
            txtChildEncounter.clear();
            //set the number of children to how many the user entered
            //also sets the text if there is an error, and changes it back to correct if the error is fixed
            numOfChildren = verificationTickets(txtChildTicket, lblChildTickets, "Child Tickets: ");
            //if the number of children is not 0, make the encounter text field editable
            if (numOfChildren > 0) {
                txtChildEncounter.setEditable(true);
                childTickets = true;
            } else {
                //catching invalid input
                txtChildEncounter.setEditable(false);
                childTickets = false;
            }
            //calling the calculate receipt function to update the live receipt
            //this will specifically update the grand total based on how many child tickets*the child price of the specific package
            calculateReceipt();
        });

        //listens to when the text field (how many adult encounters text field) is changed
        txtAdultEncounter.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //set the number of adult encounters to how many the user entered
            //also sets the text if there is an error, and changes it back to correct if the error is fixed
            numOfAdultEncounter = verificationTickets(txtAdultEncounter, lblEncounterError, "You must purchase park tickets in order to purchase encounter experience.");
            //ensures that the number of adult encounter tickets is less than the number of adult tickets purchased
            if (numOfAdultEncounter > numOfAdults) {
                //catching invalid input
                adultExperience = false;
                lblEncounterError.setText("You must purchase park tickets in order to purchase encounter experience.");
                lblEncounterError.setStyle(errorRed);
            } else {
                //catching valid input
                adultExperience = true;
            }
            //calling the calculate receipt function to update the live receipt
            //this will specifically update the grand total based on how many adult encounter tickets*number of adult tickets
            calculateReceipt();
        });

        //listens to when the text field (how many child encounters text field) is changed
        txtChildEncounter.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //set the number of child encounters to how many the user entered
            //also sets the text if there is an error, and changes it back to correct if the error is fixed
            numOfChildEncounter = verificationTickets(txtChildEncounter, lblEncounterError, "You must purchase park tickets in order to purchase encounter experience.");
            //ensures that the number of child encounter tickets is less than the number of child tickets purchased
            if (numOfChildEncounter > numOfChildren) {
                //catching invalid input
                childExperience = false;
                lblEncounterError.setText("You must purchase park tickets in order to purchase encounter experience.");
                lblEncounterError.setStyle(errorRed);
            } else {
                childExperience = true;
            }
            //calling the calculate receipt function to update the live receipt
            //this will specifically update the grand total based on how many child encounter tickets*number of child tickets
            calculateReceipt();
        });

        //listener that detects changes to the text field
        txtFirstName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensures the first name text field does not have numbers
            first = verificationString(txtFirstName, lblFirstName, "First Name: ", "Please enter a valid name! ", "^[a-zA-Z ]*$");
           //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtLastName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensures the last name text field does not have numbers
            last = verificationString(txtLastName, lblLastName, "Last Name: ", "Please enter a valid name! ", "^[a-zA-Z ]*$");
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtMiddle.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //only allowing user to type in one character
            if (txtMiddle.getText().length() > 1) {
                txtMiddle.setText(oldValue);
            }
            //ensuring that the length of the middle initial is 1
            middle = verificationString(txtMiddle, lblMiddle, "Middle Initial: ", "Please enter a valid initial! ", "^[a-zA-Z]*$");
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtStreetAddress.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //set to false to catch invalid input
            street = false;
            //ensuring street address is at least 2 characters
            if (txtStreetAddress.getText().length() > 1) {
                street = true;
                lblStreetAddress.setText("Street Address: ");
                lblStreetAddress.setStyle(errorBlack);
            } else {
                //catching invalid input
                street = false;
                lblStreetAddress.setText("Please enter a valid street address! ");
                lblStreetAddress.setStyle(errorRed);
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtCity.textProperty().addListener((observableValue, oldValue, newValue) -> {
            city = verificationString(txtCity, lblCity, "City: ", "Please enter a valid city! ", "^[a-zA-Z ]*$");
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtState.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //catching invalid input
            state = false;
            //ensuring state text field is not allowing more than 2 characters to be typed
            if (txtState.getText().length() > 2) {
                txtState.setText(oldValue);
            } else if (txtState.getText().length() < 2) {
                //catching invalid input
                state = false;
                lblState.setText("Please enter a valid state! ");
                lblState.setStyle(errorRed);
                //ensuring state abbreviation is 2 characters
            } else if (txtState.getText().length() == 2) {
                state = verificationString(txtState, lblState, "State: ", "Please enter a valid state! ", "^AL|AK|AZ|AR|CA|CO|CT|DE|FL|GA|HI|ID|IN|IA|KS|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY*$");
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtZip.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensuring text field is not allowing more than 5 characters to be typed
            if (txtZip.getText().length() > 5) {
                txtZip.setText(oldValue);
            } else if (txtZip.getText().length() < 5) {
                //catching invalid input
                zip = false;
                lblZip.setText("Please enter a valid zip code! ");
                lblZip.setStyle(errorRed);
                //ensuring zip is only 5 characters
            } else if (txtZip.getText().length() == 5) {
                zip = verificationString(txtZip, lblZip, "Zip: ", "Please enter a valid zip code! ", "^[0-9]*$");
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtPhoneNumber.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensuring text field is not allowing more than 12 characters to be typed
            if (txtPhoneNumber.getText().length() > 12) {
                txtPhoneNumber.setText(oldValue);
            } else if (txtPhoneNumber.getText().length() < 12) {
                //catching invalid input
                phone = false;
                lblPhoneNumber.setText("Please enter a valid phone number! ");
                lblPhoneNumber.setStyle(errorRed);
                //ensuring phone number is 12 characters
            } else if (txtPhoneNumber.getText().length() == 12) {
                phone = verificationString(txtPhoneNumber, lblPhoneNumber, "Phone Number: ", "Please enter a valid phone number! ", "^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtCardNumber.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensuring text field is not allowing more than 5 characters to be typed
            if (txtCardNumber.getText().length() > 19) {
                txtCardNumber.setText(oldValue);
            } else if (txtCardNumber.getText().length() < 19) {
                //catching invalid input
                cc = false;
                lblCardNumber.setText("Please enter a valid card number! ");
                lblCardNumber.setStyle(errorRed);
                //ensuring card number is 19 characters
            } else if (txtCardNumber.getText().length() == 19) {
                cc = verificationString(txtCardNumber, lblCardNumber, "Card Number: ", "Please enter a valid card number! ", "^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}$");
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtExpirationDate.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //ensuring text field is not allowing more than 5 characters to be typed
            if (txtExpirationDate.getText().length() > 5) {
                txtExpirationDate.setText(oldValue);
            } else if (txtExpirationDate.getText().length() < 5) {
                //catching invalid input
                expiration = false;
                lblExpirationDate.setText("Please enter a valid date! ");
                lblExpirationDate.setStyle(errorRed);
                //ensuring expiration is 5 characters
            } else if (txtExpirationDate.getText().length() == 5) {
                expiration = verificationString(txtExpirationDate, lblExpirationDate, "Expiration Date: ", "Please enter a future date! ", "^(0[1-9]|1[0-2])-([2-9]{2})");
            }
            //calls this method because if the input is not valid, you cannot submit the form
            checkButton();
        });

        //listener that detects changes to the text field
        txtDiscount.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //setting text with valid discount
            lblDiscount.setText("Discount: ");
            lblDiscount.setStyle(errorBlack);
            validDiscount = true;
            //if nothing is in the discount, make sure there is no error and set discount to 0
            if (txtDiscount.getText().equals("0") || txtDiscount.getText().isEmpty()) {
                discount = 0;
                calculateReceipt();
                return;
            }
            try {
                //ensuring discount is between 0 and 100
                discount = Double.parseDouble(txtDiscount.getText());
                if (discount < 0 || discount > 100) {
                    validDiscount = false;
                    lblDiscount.setText("Invalid discount! ");
                    lblDiscount.setStyle(errorRed);
                }
            } catch (Exception error) {
                //catching invalid input
                validDiscount = false;
                lblDiscount.setText("Invalid discount! ");
                lblDiscount.setStyle(errorRed);
            }
            //calling the calculate receipt function to update the live receipt
            //this will specifically update the grand total based on managers discount
            calculateReceipt();
        });
    }


    //listens to when the confirm button is clicked
    @FXML
    void confirmOrder(ActionEvent event) {
        //showing the receipt
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thank you for your order!");
        alert.setContentText(receiptInfo + "\n\n Come again!");
        //show receipt, close window
        alert.showAndWait();
        System.exit(0);
    }

    //method that is used to verify if the tickets entered is valid (no negatives)
    //also used to make sure that the number of encounter tickets is less or equal to the number of tickets purchased
    public int verificationTickets(TextField tf, Label l, String text) {
        int num = 0;
        try {
            //if input is 0 or empty than it is valid
            if (tf.getText().equals("0") || tf.getText().isEmpty()) {
                l.setText(text);
                l.setStyle(errorBlack);
            } else if (Integer.parseInt(tf.getText()) > 0) {
                l.setText(text);
                l.setStyle(errorBlack);
                num = Integer.parseInt(tf.getText());
            } else {
                l.setStyle(errorRed);
                l.setText("Please enter a valid number of tickets");
            }
            //catch invalid input
        } catch (Exception error) {
            l.setStyle(errorRed);
            l.setText("Please enter a valid number of tickets");
            num = 0;
        }
        return num;
    }

    //testing to make sure all inputs are valid in the customer information section
    public boolean verificationString(TextField tf, Label l, String valid, String invalid, String regex) {
        //getting value in text field
        String text = tf.getText();
        //if the string is empty it is not valid
        if (tf.getText().isEmpty()) {
            return false;
        }
        //pass all inputs to uppercase so that the regex is easier and user can type in any
        //upper or lowercase letter
        if (text.toUpperCase().matches(regex)) {
            l.setStyle(errorBlack);
            //set the text to valid
            l.setText(valid);
            return true;
        } else {
            //catch the error
            l.setStyle(errorRed);
            l.setText(invalid);
        }
        return false;
    }

    //this method updates the total costs and live receipt
    public void calculateReceipt() {
        //declare temp value, initialize global variables to be updated
        double temp;
        receiptInfo = "";
        grandTotal = 0;
        //clear and reset list everytime selections change
        lstPurchases.getItems().clear();
        lstPurchases.getItems().add("Package Type: " + selectedPackage);

        //if the number of adults is valid
        if (numOfAdults != 0) {
            //update temp to current price
            temp = numOfAdults * adultPrice;
            //formatting receipt
            lstPurchases.getItems().add(numOfAdults + " Adult tickets: $" + String.format("%.2f", temp));
            //update grand total
            grandTotal += temp;
        }
        //if the number of children is valid
        if (numOfChildren != 0) {
            //update temp to current price
            temp = numOfChildren * childPrice;
            //formatting receipt
            lstPurchases.getItems().add(numOfChildren + " Child tickets: $" + String.format("%.2f", temp));
            //update grand total
            grandTotal += temp;
        }
        //if the number of adult encounter is valid
        if (numOfAdultEncounter != 0) {
            //update temp to current price
            temp = numOfAdultEncounter * rAdult;
            //formatting receipt
            lstPurchases.getItems().add(numOfAdultEncounter + " Adult Encounter tickets: $" + String.format("%.2f", temp));
            //update grand total
            grandTotal += temp;
        }
        //if the number of child encounter is valid
        if (numOfChildEncounter != 0) {
            //update temp to current price
            temp = numOfChildEncounter * rChild;
            //formatting receipt
            lstPurchases.getItems().add(numOfChildEncounter + " Child Encounter tickets: $" + String.format("%.2f", temp));
            //update grand total
            grandTotal += temp;
        }

        //setting total discount label and updating it
        lblTotalDiscount.setText("$" + String.format("%.2f", ((grandTotal * discount) / 100)));
        if (discount != 0) {
            //subtracting discount from grand total
            grandTotal -= (grandTotal * (discount / 100));
        }

        //setting total tax label and updating grand total
        lblTotalTax.setText("$" + String.format("%.2f", (grandTotal * SALES_TAX)));
        grandTotal = grandTotal * (1 + SALES_TAX);

        //setting grand total label and updating receipt info after
        lblGrandTotal.setText("$" + String.format("%.2f", grandTotal));
        receiptInfo = "Tax: " + String.format("%.2f", (grandTotal * SALES_TAX)) + "\n Discount: " + String.format("%.2f", ((grandTotal * discount) / 100)) + "\n Grand Total: " + String.format("%.2f", grandTotal);
        //calls this method because if the input is not valid, you cannot submit the form
        checkButton();
    }

    //ensures all text fields are filled out correctly to enable the checkout button
    public void checkButton() {
        //temp variable to hold the text calculated in the grand total label
        double temp = 0;
        if(!lblGrandTotal.getText().isEmpty()) {
            //making sure to not get the $ in the string
            temp = Double.parseDouble(lblGrandTotal.getText().substring(1));
        }
        //if all the text fields are valid and there are items in the cart, enable the confirm button
        if (temp != 0 && first && middle && last && street && city && state && zip && phone && cc && expiration && adultTickets && childTickets && adultExperience && childExperience && validDiscount) {
            btnConfirm.setDisable(false);
        } else {
            btnConfirm.setDisable(true);
        }
    }
}


