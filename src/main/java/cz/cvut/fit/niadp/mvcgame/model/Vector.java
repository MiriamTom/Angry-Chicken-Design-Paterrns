package cz.cvut.fit.niadp.mvcgame.model;

public class Vector {

	private double x;
	private double y;

	public Vector() {
		this(0, 0);
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	// pomocné operácie, veľmi užitočné pri trajektórii:
	public Vector add(Vector v) {
		return new Vector(this.x + v.x, this.y + v.y);
	}

	public Vector multiply(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

    public int getDX() {
		return (int) x;
    }
}
