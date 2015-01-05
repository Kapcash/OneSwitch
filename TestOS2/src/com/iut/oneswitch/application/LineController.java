package com.iut.oneswitch.application;

public abstract class LineController {
	protected boolean isShown;
	protected boolean isMoving;
	
	abstract public void init();
	abstract public void add();
	abstract public void remove();
	abstract protected void start();
	abstract public void pause();
	
	/**
	 * Ligne affichée sur l'écran ?
	 * @return vrai ou faux
	 */
	
	public boolean isShown(){return isShown;};
	/**
	 * Ligne en déplacement sur l'écran
	 * @return vrai ou faux
	 */
	
	public boolean isMoving(){return isMoving;};
	abstract public int getX();
	abstract public int getY();
	abstract public int getThickness();
}
