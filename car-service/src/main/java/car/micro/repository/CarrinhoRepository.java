package car.micro.repository;

import car.micro.domain.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

}