package webshop.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${run}.properties"
})
public interface WebDriverConfig extends Config {

    @DefaultValue("local")
    String run();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    String browserVersion();

    @DefaultValue("1920x1080")
    String browserSize();

    String selenoidUrl();

    String selenoidUser();

    String selenoidPassword();

    boolean enableVideo();

    boolean enableVNC();
}