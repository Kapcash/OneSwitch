package com.iut.oneswitch.application;

public abstract class LineController {
	protected boolean isShown;
	protected boolean isMoving;
	protected int iterations;
	
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
	
	/**
	 * Ajoute une itération au compteur
	 */
	protected void addIterations(){
		iterations+=1;
	}
	
	/**
	 * remet a zero le nombre d'iterations
	 */
	public void resetIterations(){
		iterations=0;
	}
	
	/**
	 * retourne le nombre d'iterations effectuées
	 * @return nombre d'iterations
	 */
	public int getIterations(){
		return iterations;
	}
	
	abstract public int getX();
	abstract public int getY();
	abstract public int getThickness();
}