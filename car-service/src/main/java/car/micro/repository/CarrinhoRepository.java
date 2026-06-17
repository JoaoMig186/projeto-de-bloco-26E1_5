package car.micro.repository;

import car.micro.domain.Carrinho;
import car.micro.domain.ENUM.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    List<Carrinho> findByStatusAndDataAtualizacaoBefore(
            StatusCarrinho status,
            LocalDateTime data
    );
    Optional<Carrinho> findByUsuarioId(Long usuarioId);

}