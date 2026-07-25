package com.ankitrainer;

import com.ankitrainer.config.service.ConfigService;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class DesktopApp extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(AnkiTrainerApp.class);
    }

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        webView.getEngine().setJavaScriptEnabled(true);
        ConfigService configService = springContext.getBean(ConfigService.class);

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, old, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("java", new JavaBridge(webView));
            }
        });

        // alert
        webView.getEngine().setOnAlert(event -> {
            System.out.println("🔹 JS Alert: " + event.getData());
        });

        String startUrl;
        if (configService.hasSavedConfig()) {
            startUrl = "http://localhost:8080/menu";
        } else {
            startUrl = "http://localhost:8080/config";
        }
        webView.getEngine().load(startUrl);

        Scene scene = new Scene(webView, 900, 700);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                System.out.println("🔹 Enter pressed in JavaFX Scene!");
                webView.getEngine().executeScript("window.handleEnterFromJava();");
            }
        });
        stage.setTitle("Anki Verb Trainer");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static class JavaBridge {
        private final WebView webView;

        public JavaBridge(WebView webView) {
            this.webView = webView;
        }

        public void handleEnter() {
            System.out.println("🔹 Enter pressed from Java!");
            webView.getEngine().executeScript("window.handleEnterFromJava();");
        }
    }
}