package shopping.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shopping.dto.ProductDto;
import shopping.service.ProductService;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ProductMockTest {

    @Test
    public void createProduct() {
        //given
        ProductService mockProductService = mock(ProductService.class);
        ProductController productController = new ProductController(mockProductService);
        ProductDto productDto = new ProductDto("",1,"");

        //when
        productController.createProduct(productDto);

        //then
        then(mockProductService).should(times(0)).save(productDto);
    }

    @Test
    public void findProduct() {
        //given
        ProductService mockProductService = mock(ProductService.class);
        ProductController productController = new ProductController(mockProductService);
        ProductDto productDto = new ProductDto("test",1,"");

        //when
        productController.createProduct(productDto);

        //then
        then(mockProductService).should(times(0)).save(productDto);
    }
}
