package com.trendshop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trendshop.common.BusinessException;
import com.trendshop.common.PageResult;
import com.trendshop.entity.Product;
import com.trendshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<String> getCategories() {
        return productMapper.selectCategories();
    }

    public PageResult<Product> listProducts(int page, int pageSize, String category, String keyword, String sort) {
        Page<Product> p = new Page<>(page, pageSize);
        IPage<Product> result = productMapper.selectProductPage(p, category, keyword, sort);
        return PageResult.of(result.getRecords(), page, pageSize, result.getTotal());
    }

    public Product getProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        return product;
    }

    public void updateProduct(Long id, Product updates) {
        Product product = productMapper.selectById(id);
        if (product == null) throw new BusinessException(404, "商品不存在");

        if (updates.getName() != null) product.setName(updates.getName());
        if (updates.getCategory() != null) product.setCategory(updates.getCategory());
        if (updates.getPrice() != null) product.setPrice(updates.getPrice());
        if (updates.getOriginalPrice() != null) product.setOriginalPrice(updates.getOriginalPrice());
        if (updates.getImage() != null) product.setImage(updates.getImage());
        if (updates.getStock() != null) product.setStock(updates.getStock());
        if (updates.getDescription() != null) product.setDescription(updates.getDescription());
        if (updates.getBadge() != null) product.setBadge(updates.getBadge());
        if (updates.getBadgeType() != null) product.setBadgeType(updates.getBadgeType());

        productMapper.updateById(product);
    }

    public long getTotalCount() {
        return productMapper.selectCount(null);
    }
}
