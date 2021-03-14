package com.github.aoklyunin.javaGLHelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import jMath.aoklyunin.github.com.vector.Vector2d;
import jMath.aoklyunin.github.com.vector.Vector4d;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

/**
 * Класс параметров текста, отображаемого на экране средствами OpenGL
 */
public class CaptionParams {
    /**
     * размер шрифта
     */
    private final int fontSize;
    /**
     * название шрифта
     */
    private final String fontName;
    /**
     * флаг, должен ли быть шрифт полужирным
     */
    private final boolean bold;
    /**
     * положение текста
     */
    private final Vector2d pos;
    /**
     * шаг текста, если он многострочный
     */
    private final double step;
    /**
     * цвет текста
     */
    private final Vector4d color;

    /**
     * Конструктор класса параметров текста, отображаемого на экране средствами OpenGL
     *
     * @param fontSize размер шрифта
     * @param fontName название шрифта
     * @param bold     флаг, должен ли быть шрифт полужирным
     */
    private CaptionParams(int fontSize, @Nullable String fontName, boolean bold) {
        this(fontSize, fontName, bold, null, null, Vector4d.ones());
    }

    /**
     * Конструктор класса параметров текста, отображаемого на экране средствами OpenGL
     *
     * @param fontSize размер шрифта
     * @param fontName название шрифта
     * @param bold     флаг, должен ли быть шрифт полужирным
     * @param pos      положение текста
     * @param step     шаг текста, если он многострочный
     * @param color    цвет текста
     */
    @JsonCreator
    private CaptionParams(
            @Nullable @JsonProperty("fontSize") Integer fontSize,
            @Nullable @JsonProperty("fontName") String fontName, @Nullable @JsonProperty("bold") Boolean bold,
            @Nullable @JsonProperty("pos") Vector2d pos, @Nullable @JsonProperty("step") Double step,
            @Nullable @JsonProperty("color") Vector4d color
    ) {

        this.fontSize = Objects.requireNonNullElse(fontSize, 24);
        this.fontName = Objects.requireNonNullElse(fontName, "ArialBlack");
        this.bold = Objects.requireNonNullElse(bold, true);
        this.pos = pos == null ? Vector2d.zeros() : new Vector2d(pos);
        this.step = Objects.requireNonNullElse(step, 0.0);
        this.color = color == null ? Vector4d.ones() : new Vector4d(color);
    }

    /**
     * Получить параметры текста по умолчанию
     *
     * @return параметры текста по умолчанию
     */
    @NotNull
    public static CaptionParams getDefaultCaptionParams() {
        return new CaptionParams(
                20,
                "ArialBlack",
                true
        );
    }


    /**
     * Получить контроллеры текстов из параметров
     *
     * @param map          словарь параметров
     * @param clientWidth  ширина окна
     * @param clientHeight высота окна
     * @return словарь контроллеров текста
     */
    @NotNull
    public static Map<String, GLTextController> getTextControllersFromParams(
            @NotNull Map<String, CaptionParams> map, int clientWidth, int clientHeight
    ) {
        return new HashMap<>(map.entrySet().stream().collect(
                toMap(
                        Map.Entry::getKey, entry -> new GLTextController(clientWidth, clientHeight, entry.getValue())
                )
        ));
    }


    /**
     * Получить размер шрифта
     *
     * @return размер шрифта
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Получить название шрифта
     *
     * @return название шрифта
     */
    @NotNull
    public String getFontName() {
        return fontName;
    }

    /**
     * Получить флаг, должен ли быть шрифт полужирным
     *
     * @return флаг, должен ли быть шрифт полужирным
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Получить положение текста
     *
     * @return положение текста
     */
    @NotNull
    public Vector2d getPos() {
        return pos;
    }

    /**
     * Получить  шаг текста, если он многострочный
     *
     * @return шаг текста, если он многострочный
     */
    public double getStep() {
        return step;
    }

    /**
     * Получить  цвет текста
     *
     * @return цвет текста
     */
    @NotNull
    public Vector4d getColor() {
        return color;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return CaptionParams{fontSize}
     */
    @Override
    public String toString() {
        return "CaptionParams{" + fontSize + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaptionParams that = (CaptionParams) o;

        if (fontSize != that.fontSize) return false;
        if (bold != that.bold) return false;
        if (Double.compare(that.step, step) != 0) return false;
        if (!Objects.equals(fontName, that.fontName)) return false;
        if (!Objects.equals(pos, that.pos)) return false;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = fontSize;
        result = 31 * result + (fontName != null ? fontName.hashCode() : 0);
        result = 31 * result + (bold ? 1 : 0);
        result = 31 * result + (pos != null ? pos.hashCode() : 0);
        temp = Double.doubleToLongBits(step);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
