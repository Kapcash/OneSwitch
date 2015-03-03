package com.example.oneswitch.control;

public abstract class LineController {
	
	/**
	 * 
	 */
	protected boolean isShown;
	
	/**
	 * 
	 */
	protected boolean isMoving;
	
	/**
	 * 
	 */
	protected int iterations;
	
	/**
	 * 
	 */
	abstract public void init();
	
	/**
	 * 
	 */
	abstract public void add();
	
	/**
	 * 
	 */
	abstract public void remove();
	
	/**
	 * 
	 */
	abstract protected void start();
	
	/**
	 * 
	 */
	abstract public void pause();
	
	/**
	 * 
	 * @return Vrai si la ligne est affich�e.
	 */
	public boolean isShown(){return isShown;};
	
	/**
	 *
	 * @return Vrai si la ligne est en mouvement.
	 */
	public boolean isMoving(){return isMoving;};
	
	/**
	 * Ajoute une it�ration � la variable permettant de compter le nombre de "tour" d'une ligne.
	 */
	protected void addIterations(){
		iterations+=1;
	}
	
	/**
	 * Remet � z�ro l'attribut iterations.
	 */
	public void resetIterations(){
		iterations=0;
	}
	
	/**
	 * Acesseur de l'attribut iterations.
	 * @return Le nombre de "tour" � l'�cran d'une ligne.
	 */
	public int getIterations(){
		return iterations;
	}
	
	/**
	 * 
	 * @return
	 */
	abstract public int getX();
	
	/**
	 * 
	 * @return
	 */
	abstract public int getY();
	
	/**
	 * 
	 * @return
	 */
	abstract public int getThickness();
}