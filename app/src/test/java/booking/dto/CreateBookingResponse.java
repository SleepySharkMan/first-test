package booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBookingResponse {
    private Integer bookingid;
    private CreateBookingDto booking;
}