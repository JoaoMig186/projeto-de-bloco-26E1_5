package car.micro.DTO;

public record ItemDeliveryDTO(

        Long produtoId,

        Long lojaId,

        String cepRetirada,

        Integer quantidade,

        Double peso,

        Boolean fragil

) {
}