package empty;
public class Shape {
    private String color;

    public Shape() {}
    public Shape(String color) {
        this.color = color;
    }

    public void setColor(String color) { this.color = color; }

    public String getColor() { return color; }

    public double getArea() { return 0; }
    public double getPerimeter() { return 0; }
}
