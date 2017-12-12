package fi.academy.codeliveserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by Administrator on 05/12/2017.
 */
@Configuration
@EnableWebSocketMessageBroker   // MIKA: Luo viestinvälittäjän ja mahdollistaa websocketien käytön
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // MIKA: Määrittelee STOMP-protokollalle osoitteen, johon selain ottaa yhteyttä rekisteröityäkseen
        // addEndpoint("/rekisteroidy");

        // Määrittelee SockJS fallback-tuen, jota käytetään mikäli käyttäjän selain ei tue websocketteja
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
        // JARI: Yllä oleva .setAllowedOrigins("*") mahdollistaa kutsujen lähettämisen myös muualta kuin saman toimialueen sisältä
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // setApplicationDestinationPrefixes("/pistoke");
        // MIKA: Sisääntulokanava, johon tullut liikenne ohjataan edelleen viestinvälittäjälle
//        registry.setApplicationDestinationPrefixes("/app");

        // enableSimpleBroker("/keskustelunaihe_ulos");
        // MIKA: Luo viestinvälittäjälle murupolun, jota pitkin vastaukset lähetetään käyttäjälle
        registry.enableSimpleBroker("/channel");


    }
}
