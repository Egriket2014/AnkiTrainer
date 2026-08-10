package com.ankitrainer;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

        webView.getEngine().load("http://localhost:8080/menu");

        Scene scene = new Scene(webView, 900, 700);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("🔹 Enter pressed in JavaFX Scene!");
                webView.getEngine().executeScript("window.handleEnterFromJava();");
            } else if (event.getCode() == KeyCode.TAB) {
                System.out.println("🔹 Tab pressed in JavaFX Scene!");
                webView.getEngine().executeScript("window.handleTabFromJava();");
                event.consume();
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