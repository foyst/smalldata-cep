package uk.co.foyst.smalldata.cep.api.dto;

public class RestEventConsumerConfigDto extends EventConsumerConfigDto {

    public RestEventConsumerConfigDto() {
    }

    public RestEventConsumerConfigDto(final String eventConsumerId, final String streamId) {
        super(eventConsumerId, streamId);
    }
}
