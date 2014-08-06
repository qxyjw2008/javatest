package com.bstek.demo.ims.entity;

// Generated 2012-9-7 16:29:46 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Sell generated by hbm2java
 */
@Entity
@Table(name = "sell", catalog = "ims")
public class Sell implements java.io.Serializable {

	private Integer id;
	private Customer customer;
	private Integer customerId;
	private Goods goods;
	private BigDecimal totalPrice;
	private BigDecimal unitPrice;
	private BigDecimal totalnumber;
	private BigDecimal paid;
	private Date sellTime;
	private String comment;

	public Sell() {
	}

	public Sell(Customer customer, Goods goods, BigDecimal totalPrice,
			BigDecimal unitPrice, BigDecimal totalnumber, BigDecimal paid,
			Date sellTime, String comment) {
		this.customer = customer;
		this.goods = goods;
		this.totalPrice = totalPrice;
		this.unitPrice = unitPrice;
		this.totalnumber = totalnumber;
		this.paid = paid;
		this.sellTime = sellTime;
		this.comment = comment;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id",insertable=false,updatable=false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@Column(name = "customer_id")
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	public Goods getGoods() {
		return this.goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	@Column(name = "total_price", precision = 20)
	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name = "unit_price", precision = 9)
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "totalnumber", precision = 9)
	public BigDecimal getTotalnumber() {
		return this.totalnumber;
	}

	public void setTotalnumber(BigDecimal totalnumber) {
		this.totalnumber = totalnumber;
	}

	@Column(name = "paid", precision = 20)
	public BigDecimal getPaid() {
		return this.paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sell_time", length = 0)
	public Date getSellTime() {
		return this.sellTime;
	}

	public void setSellTime(Date sellTime) {
		this.sellTime = sellTime;
	}

	@Column(name = "comment", length = 400)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
