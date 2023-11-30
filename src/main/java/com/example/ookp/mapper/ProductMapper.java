package com.example.ookp.mapper;

import com.example.ookp.dto.ProductDTO;
import com.example.ookp.model.Product;
import com.example.ookp.service.TypeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final TypeService typeService;

    public Product toProduct(ProductDTO productDTO) {
        var product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setImagePath(productDTO.getImagePath());
        if(productDTO.getTypeId() == 0) {
            product.setType(null);
        } else {
            product.setType(typeService.findById(productDTO.getTypeId()));
        }
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        product.setCount(productDTO.getCount());
        product.setTotalSales(productDTO.getTotalSales());
        return product;
    }
    public ProductDTO toProductDTO(Product product) {
        var productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setImagePath(product.getImagePath());
        if(product.getType() == null) {
            productDTO.setTypeId(0);
        } else {
            productDTO.setTypeId(product.getType().getId());
        }
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setCount(product.getCount());
        productDTO.setCount(product.getCount());
        productDTO.setTotalSales(product.getTotalSales());
        return productDTO;
    }
}
