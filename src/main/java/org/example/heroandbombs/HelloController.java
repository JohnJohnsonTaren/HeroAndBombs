package org.example.heroandbombs;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.application.Platform; // Необхідний для закриття програми (exit)

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label pause, lose;

    @FXML
    private Button restart; // FXML ID для кнопки Restart

    @FXML
    private Button exit; // FXML ID для кнопки Exit

    @FXML
    private ImageView forest1, forest2, hero, bomb;

    private final int FOREST_WIDTH = 800;

    // Константи для коректного скидання стану
    private final float INITIAL_HERO_LAYOUT_Y = 286.0f;
    private final float INITIAL_HERO_LAYOUT_X = 36.0f;
    private final float INITIAL_HERO_SPEED = 0.5f;
    private final float INITIAL_JUMP_DOWN_SPEED = 0.75f;

    private ParallelTransition parallelTransition;

    private TranslateTransition bombTransition;

    public static boolean jump = false;

    public static boolean right = false;

    public static boolean left = false;

    public static boolean isPause = false; // Використовується для Паузи/Продовження

    private float heroSpeed = INITIAL_HERO_SPEED, jumpDownSpeed = INITIAL_JUMP_DOWN_SPEED;

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            // 1. Логіка руху героя та стрибка
            if(jump && hero.getLayoutY() > 125f)
                hero.setLayoutY(hero.getLayoutY() - heroSpeed);
            else if (hero.getLayoutY() <= 290f) {
                jump = false;
                hero.setLayoutY(hero.getLayoutY() + jumpDownSpeed);
            }

            if(right && hero.getLayoutX() < 400f)
                hero.setLayoutX(hero.getLayoutX() + heroSpeed);

            if(left && hero.getLayoutX() > 25f)
                hero.setLayoutX(hero.getLayoutX() - heroSpeed);

            // 2. Логіка Паузи/Продовження
            if (isPause && !pause.isVisible()) {
                heroSpeed = 0;
                jumpDownSpeed = 0;
                parallelTransition.pause();
                bombTransition.pause();
                pause.setVisible(true);
            }
            else if (!isPause && pause.isVisible()) {
                pause.setVisible(false);
                heroSpeed = INITIAL_HERO_SPEED;
                jumpDownSpeed = INITIAL_JUMP_DOWN_SPEED;
                parallelTransition.play();
                bombTransition.play();
            }

            // 3. Логіка Програшу (Зіткнення з бомбою)
            if(hero.getBoundsInParent().intersects(bomb.getBoundsInParent())) {
                lose.setVisible(true);
                if (restart != null) restart.setVisible(true); // Показуємо кнопку Restart

                // Зупиняємо рух та анімації
                heroSpeed = 0;
                jumpDownSpeed = 0;
                parallelTransition.pause();
                bombTransition.pause();
                timer.stop(); // Зупиняємо ігровий цикл
            }
        }
    };

    /**
     * Скидає стан гри та починає її спочатку (Restart).
     */
    @FXML
    void restartGame() {
        // 1. Скидання логічних змінних
        jump = false;
        right = false;
        left = false;
        isPause = false;

        // 2. Скидання швидкостей та позицій
        heroSpeed = INITIAL_HERO_SPEED;
        jumpDownSpeed = INITIAL_JUMP_DOWN_SPEED;
        hero.setLayoutY(INITIAL_HERO_LAYOUT_Y);
        hero.setLayoutX(INITIAL_HERO_LAYOUT_X);

        // 3. Скидання анімацій:
        bombTransition.stop();
        bomb.setTranslateX(0); // Повертаємо бомбу на її початкову TranslateX (0)
        bombTransition.playFromStart();

        parallelTransition.playFromStart();

        // 4. Приховування/показ елементів
        lose.setVisible(false);
        if (restart != null) restart.setVisible(false); // Приховуємо кнопку Restart
        pause.setVisible(false);

        // 5. Запуск ігрового циклу
        timer.start();
    }

    /**
     * Закриває застосунок (Exit).
     */
    @FXML
    void exit() {
        Platform.exit();
    }


    @FXML
    void initialize() {
        // Ініціалізація анімації фону
        TranslateTransition forestOneTransition = new TranslateTransition(Duration.millis(5000), forest1);
        forestOneTransition.setFromX(0);
        forestOneTransition.setToX(FOREST_WIDTH * -1);
        forestOneTransition.setInterpolator(Interpolator.LINEAR);
        forestOneTransition.setCycleCount(Animation.INDEFINITE);

        TranslateTransition forestTwoTransition = new TranslateTransition(Duration.millis(5000), forest2);
        forestTwoTransition.setFromX(0);
        forestTwoTransition.setToX(FOREST_WIDTH * -1);
        forestTwoTransition.setInterpolator(Interpolator.LINEAR);
        forestTwoTransition.setCycleCount(Animation.INDEFINITE);

        // Ініціалізація анімації бомби
        bombTransition = new TranslateTransition(Duration.millis(3000), bomb);
        bombTransition.setFromX(0);
        bombTransition.setToX(FOREST_WIDTH * -1 - 100);
        bombTransition.setInterpolator(Interpolator.LINEAR);
        bombTransition.setCycleCount(Animation.INDEFINITE);
        bombTransition.play();

        // Запуск паралельної анімації фону
        parallelTransition = new ParallelTransition(forestOneTransition, forestTwoTransition);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();

        timer.start();
    }
}