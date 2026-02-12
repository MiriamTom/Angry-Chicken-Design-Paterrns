package cz.cvut.fit.niadp.mvcgame.model;

public class Position {
    private int dimX = 0;
	private int dimY = 0;
	
	public Position( ){

	}

	public Position( int posX, int posY ){
		this.dimX = posX;
		this.dimY = posY;
	}

	public int getX( ) {
		return dimX;
	}
    
    public int getY( ) {
		return dimY;
	}
    
    public void setY( int y ) {
		this.dimY = y;
	}
    
    public void setX( int x ) {
		this.dimX = x;
	}

	public void add( Vector vector ) {
		this.setX((int) (this.getX( ) + vector.getX( )));
		this.setY((int) (this.getY( ) + vector.getY( )));
	}
	
}