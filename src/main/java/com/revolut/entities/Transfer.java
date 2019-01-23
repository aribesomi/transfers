package com.revolut.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Transfer implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Account from;
	private Account to;
	private BigDecimal value;
	private String description;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant created;
	
	public Transfer(Long id, Account from, Account to, BigDecimal value, String description, Instant created) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.value = value;
		this.description = description;
		this.created = created;
	}
	
	public Transfer(Long id, Account from, Account to, BigDecimal value, String description) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.value = value;
		this.description = description;
		this.created = Instant.now();
	}
	
	public Transfer(Long id) {
		this.id = id;
	}
	
	public Transfer() {
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Account getFrom() {
		return from;
	}
	public void setFrom(Account from) {
		this.from = from;
	}
	public Account getTo() {
		return to;
	}
	public void setTo(Account to) {
		this.to = to;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Instant getCreated() {
		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "Transfer [id=" + id + ", from=" + from + ", to=" + to + ", value=" + value + ", description="
				+ description + ", created=" + created + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transfer other = (Transfer) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
