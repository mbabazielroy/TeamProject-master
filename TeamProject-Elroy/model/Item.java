package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Nathaniel Mann
 * @version v1.0
 * This item class will help instantatiate an object with a name, price, and quantity
 * It also has getters & setters to get the fields
 */

public final class Item {
	/**The name of the item*/
	private String myName;

	/**The price of the item*/
	private BigDecimal myPrice;

	/**The quantity of the item that the user will buy*/
	private int myQuantity;

	//private int myPriority;

	/**
	 *
	 * @param theName A name from the user that will set the name field of the object
	 * @param thePrice A price from the user that will set the price field of the object
 	 * @param theQuantity A quantity from the user that will set how much of the object the user will buy
	 */
	public Item(String theName, BigDecimal thePrice, int theQuantity/*, int thePriority*/) {
		myName = theName;
		myPrice = thePrice.setScale(2, RoundingMode.HALF_EVEN);
		myQuantity = theQuantity;
		//myPriority = thePriority;
	}

	/**
	 *
	 * @return Returns the private field myName;
	 */
	public String getName() {
		return myName;
	}

	/**
	 *
	 * @return Returns the private BigDecimal
	 */
	public BigDecimal getPrice() {
		return myPrice;
	}

	/**
	 *
	 * @return The amount of the item that the user is buying
	 */
	public int getQuantity() {
		return myQuantity;
	}


	/**
	 * Changes the name of the item if the user wants to edit it
	 * @param theName The name of the item that the user will input
	 */
	public void setName(String theName) {
		myName = theName;
	}

	/**
	 * Changes the price of the item if the user wants to edit it
	 * @param thePrice The price of the item that the user will input
	 */
	public void setPrice(BigDecimal thePrice) {

		myPrice = thePrice.setScale(2, RoundingMode.HALF_EVEN);
	}

	/**
	 * Changes the amount of the item that the user will buy if they want to edit it
	 * @param theQuantity The amount of the item that the user will input
	 */
	public void setQuantity(int theQuantity) {
		myQuantity = theQuantity;
	}

	/**
	 * Returns a whole string of the objects information
	 * @return A string of all of the information of the item
	 */
	public String toString() {
		final StringBuilder builder = new StringBuilder(300);
		builder.append("Name of the item: ");
		builder.append(myName);
		builder.append("              Price of Item: ");
		builder.append(numberFormat(myPrice));
		builder.append("              Quantity: ");
		builder.append(myQuantity);
		return builder.toString();
	}

	private String numberFormat(final BigDecimal thePrice) {
        final NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(thePrice);
    }

}
