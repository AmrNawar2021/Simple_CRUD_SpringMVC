package amr.fawzi.crudApp.repository;

import amr.fawzi.crudApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
