package com.shoes.assignment.model;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "Shoe")
public class Shoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Double price;


    @Column(name="image")
    private String image;

    @Column(nullable = false)
    private String product_availability;
    

    public Shoe() {
    }

    public Shoe(Long shoeId, String name, String brand, String size, Double price, String image, String product_availability) {
        this.shoeId = shoeId;
        this.name = name;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.image = image;
        this.product_availability = product_availability;
        
    }

    // Getters and setters

    public Long getShoeId() {
        return shoeId;
    }

    public void setShoeId(Long shoeId) {
        this.shoeId = shoeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_availability() {
		return product_availability;
	}

	public void setProduct_availability(String product_availability) {
		this.product_availability = product_availability;
	}

	@Override
    public String toString() {
        return "Shoe{" +
                "shoeId=" + shoeId +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", product_availability=" + product_availability +
                '}';
    }
}
