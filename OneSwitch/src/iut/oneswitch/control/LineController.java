package iut.oneswitch.control;
/**
 * Classe abstraite pour les controlleurs des lignes horizontales et verticales
 * @author OneSwitch B
 *
 */
public abstract class LineController{

	protected boolean isMoving;
	protected boolean isShown;
	protected int iterations;

	public abstract void add();

	protected void addIterations(){
		iterations = (1 + iterations);
	}

	public int getIterations(){
		return iterations;
	}

	public abstract int getThickness();

	public abstract int getX();

	public abstract int getY();

	public abstract void init();

	public boolean isMoving(){
		return isMoving;
	}

	public boolean isShown(){
		return isShown;
	}

	public abstract void pause();

	public abstract void remove();

	public void resetIterations(){
		iterations = 0;
	}

	protected abstract void start();
}
