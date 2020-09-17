package tw.eeit117.wematch.product.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBeanService implements IProductBeanService {

	@Autowired
	private ProductBeanDAO productDao;

	@Override
	public List<ProductBean> selectAll() {
		return productDao.selectAll();
	}

	@Override
	public ProductBean insert(ProductBean productBean) {
		return productDao.insert(productBean);
	}

}