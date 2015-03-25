package iut.oneswitch.control;

public abstract class LineController{
	
	protected boolean isMoving;
	protected boolean isShown;
	protected int iterations;

	public abstract void add();

	protected void addIterations(){
		this.iterations = (1 + this.iterations);
	}

	public int getIterations(){
		return this.iterations;
	}

	public abstract int getThickness();

	public abstract int getX();

	public abstract int getY();

	public abstract void init();

	public boolean isMoving(){
		return this.isMoving;
	}

	public boolean isShown(){
		return this.isShown;
	}

	public abstract void pause();

	public abstract void remove();

	public void resetIterations(){
		this.iterations = 0;
	}

	protected abstract void start();
}
