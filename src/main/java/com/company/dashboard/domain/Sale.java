package com.company.dashboard.domain;

import javax.persistence.*;

@Entity(name = "gov_sales")
public class Sale {
    @Id
    @Column(name = "id")
    private final int id = 1;

    @Column(name = "total")
    private int price;

    public Sale(int price) {
        this.price = price;
    }

    public Sale() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringBuilder("{price:")
                .append(price)
                .append(", id:")
                .append(id)
                .append("}\n")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sale)) return false;

        Sale sale = (Sale) o;

        if (getId() != sale.getId()) return false;
        return getPrice() == sale.getPrice();

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getPrice();
        return result;
    }
}
