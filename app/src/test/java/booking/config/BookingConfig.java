package booking.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/booking.properties"
})
public interface BookingConfig extends Config {

    @Key("bookingUrl")
    String bookingUrl();

    @Key("username")
    String username();

    @Key("password")
    String password();
}