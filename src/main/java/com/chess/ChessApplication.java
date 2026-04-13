package com.chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.chess.service.AuthService;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chess.ui.ChessUI;

@SpringBootApplication
public class ChessApplication {

    private static final Logger log = LoggerFactory.getLogger(ChessApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChessApplication.class);
        application.setHeadless(false);
        ConfigurableApplicationContext context = application.run(args);

        boolean uiEnabled = context.getEnvironment().getProperty("app.ui.enabled", Boolean.class, false);
        boolean headless = GraphicsEnvironment.isHeadless();

        log.info("Swing UI enabled: {}, headless runtime: {}", uiEnabled, headless);

        if (uiEnabled && !headless) {
            // Launch the Swing UI in the EDT (Event Dispatch Thread) only when explicitly enabled.
            SwingUtilities.invokeLater(() -> {
                log.info("Launching Swing auth dialog");
                AuthService authService = context.getBean(AuthService.class);
                ChessUI chessUI = new ChessUI(context, authService);
                chessUI.display();
            });
        } else if (!uiEnabled) {
            log.warn("Swing UI is disabled. Set app.ui.enabled=true to launch the desktop UI.");
        } else {
            log.warn("Headless runtime detected. The Swing UI cannot be shown in this session.");
        }
    }
}
