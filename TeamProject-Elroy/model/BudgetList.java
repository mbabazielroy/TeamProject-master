package model;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This class will make a new object that will allow the GUI to pull information
 * Will do calculations for the total, store the name, store the budget, and all of the item objects
 * @author Nathaniel Mann
 * @version v1.0
 *
 */

public class BudgetList {

	/**A new list that will hold all of the items*/
	private List<Item> BudgetList = new ArrayList<Item>();

	/**The amount left after all of the bought items are subtracted from the budget*/
	private BigDecimal myBudgetLeft;

	/**The name of the budget planner*/
	private String myName;

	/**The budget from the user input also put in bigDecimal form to allow for money to be calculated properly*/
	private BigDecimal myBudget;

	private JSONObject myEverything;


	/**A constructor*/
	public BudgetList() {

	}

	/**
	 * The method will add the item to the list that we have made to use later on
	 * @param theItem The item that the user has made put in the budget planner to be used later on
	 */
	public void add(final Item theItem) {
		BudgetList.add(theItem);
	}

	/**
	 *
	 * @param theItemList Takes an item list then will calculate the budget
	 */
	public BigDecimal calculateTotal(List<Item> theItemList) {
		BigDecimal total = BigDecimal.ZERO;
		final Iterator<Item> itr = theItemList.iterator();
		while(itr.hasNext()) {
			final Item theItem = itr.next();

			//Get's the value to be added to the total value
			final BigDecimal theQuantity = BigDecimal.valueOf(theItem.getQuantity());
			final BigDecimal thePrice = theItem.getPrice().multiply(theQuantity);

			total = total.add(thePrice);

		}
		myBudgetLeft = myBudget.subtract(total);
		return myBudgetLeft;
	}

	/**
	 * Allows the user to change the name if wanted
	 * @param theName Sets the name of the budget planner
	 */
	public void setName(String theName) {
		myName = theName;
	}

	/**
	 * Allow the user to change their budget if wanted
	 * @param theBudget of the budget planner
	 */
	public void setBudget(BigDecimal theBudget) {
		myBudget = theBudget;
	}

	public void addBackToBudget(BigDecimal addBack) {
		myBudgetLeft = addBack.add(myBudgetLeft);
	}

	/**
	 * Returns the budget - total = money left
	 * @return The money left from the items that they are planning to buy to display the amount of money left
	 */

	/**
	 * Returns if their budget left is positive or not
	 * @return
	 */

	/**
	 *
	 * @return The name of the budget
	 */
	public String getName() {
		return myName;
	}



	/**
	 *
	 * @return theBudget of the planner
	 */
	public BigDecimal getBudget() {
		return myBudget;
	}


	/**
	 * Clears the item list
	 */
	public void clear() {
		BudgetList.clear();
	}

	/**
	 * Puts all of the necessary information such as budget, name, and items into a JSON file
	 * Allows the data to be saved to be used later on
	 * @param theList The list of items that will be transferred to a json file
	 */
	@SuppressWarnings("unchecked")
	public void JsonWriter(ArrayList<Item> theList) {


		JSONArray jsonarray = new JSONArray();
		for(int i = 0; i < theList.size(); i++) {
			JSONObject obj = new JSONObject();
            JSONObject objItem =  new JSONObject();
            objItem.put("name", theList.get(i).getName());
            objItem.put("price", theList.get(i).getPrice());
            objItem.put("quantity", theList.get(i).getQuantity());
            obj.put("item", objItem);
            jsonarray.add(obj);
		}
		myEverything = new JSONObject();
		myEverything.put("name", myName);
		myEverything.put("budget", myBudget);
		myEverything.put("items", jsonarray);

		try (FileWriter file = new FileWriter("Budget.JSON", false)){
			file.write(myEverything.toString());

		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
