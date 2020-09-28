package com.tradetest.model;

import com.google.gson.annotations.SerializedName;

/***
 * {"sym":"XETHZUSD", "T":"Trade", "P":226.85, "Q":0.02, "TS":1538409733.3449,
 * "side": "b", "TS2":1538409738828589281}
 * 
 * @author tejas
 *
 */
public class Trade {

	/***
	 * sym : Stock name string
	 */
	@SerializedName(value = "sym")
	private String name;

	/***
	 * P: Price of Trade double
	 */
	@SerializedName(value = "P")
	private Double price;

	/***
	 * Q: Quantity Traded double
	 */
	@SerializedName(value = "Q")
	private Double quantity;

	/***
	 * TS2: Timestamp in UTC uint64
	 */
	@SerializedName(value = "TS2")
	private Long time;

	public Trade() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	
	@Override
	public String toString() {
		return "Trade [name=" + name + ", price=" + price + ", quantity=" + quantity + ", time=" + time + "]";
	}

}
