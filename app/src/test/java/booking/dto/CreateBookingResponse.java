package booking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter 
public class CreateBookingResponse {
    private Integer bookingid;
    private BookingDTO booking;
}