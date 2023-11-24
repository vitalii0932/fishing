package com.example.ookp.mapper;

import com.example.ookp.dto.ProductDTO;
import com.example.ookp.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductDTO productDTO);
    ProductDTO toProductDTO(Product product);
}
