package com.iut.oneswitch.application;

import java.io.IOException;

import android.graphics.Point;
import android.os.Handler;

/**
 * La classe Action Gesture permet la réalisation de diverses actions :
 * <ul>
 * <li>La simulation d'un clique n'importe où sur l'interface de l'appareil</li>
 * <li>La simulation du "Swipe"</li>
 * <li>La simulation d'un clique long</li>
 * </ul>
 * Ces dernières nécessitent l'obtention des droits "SuperUser".
 * @author OneSwitch B
 */
public class ActionGesture {
	
	/**
	 * Première coordonée.
	 * @see ActionGesture#touchAsRoot(Point pos)
	 * @see ActionGesture#swipeAsRoot(Point posUn, Point posDeux)
	 * @see ActionGesture#longTouchAsRoot(Point posUn)
	 */
	private Point pos1;
	
	/**
	 * Seconde coordonée.
	 * @see ActionGesture#touchAsRoot(Point pos)
	 * @see ActionGesture#swipeAsRoot(Point posUn, Point posDeux)
	 * @see ActionGesture#longTouchAsRoot(Point posUn)
	 */
	private Point pos2;
	
	/**
	 * Permet de cliquer sur l'interface avec un point pour donnée.
	 * Les droits "Super User" sont nécessaires afin de réaliser cela.
	 * La méthode exec() nous fournit les authorisations requises.
	 * @param pos Coordonnée où le clique doit être appliqué.
	 */
	public void touchAsRoot(Point pos){
		pos1 = pos;
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input tap " + pos1.x + " " + pos1.y);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
	
	/**
	 * Implémente l'action "glisser" avec deux points pour données.
	 * Les droits "Super User" sont nécessaires afin de réaliser cela.
	 * La méthode exec() nous fournit les authorisations requises.
	 * Le glissé s'effectue de la première coordonnée vers la seconde.
	 * @param posUn Première coordonée servant de point de départ pour le "Swipe".
	 * @param posDeux Seconde coordonnée servant de point d'arrivée pour le Swipe".
	 */
	public void swipeAsRoot(Point posUn, Point posDeux){
		pos1 = posUn;
		pos2 = posDeux;
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input swipe " + pos1.x +  " " + pos1.y + " " + pos2.x + " " + pos2.y + " 300");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
	
	/**
	 * Permet d'effectuer un clique dit long sur l'interface avec un point pour donnée.
	 * Les droits "Super User" sont nécessaires afin de réaliser cela.
	 * La méthode exec() nous fournit les authorisations requises.
	 * @param posUn Coordonnée où le clique doit être appliqué.
	 */
	public void longTouchAsRoot(Point posUn){
		pos1 = posUn;
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input swipe " + pos1.x +  " " + pos1.y + " " + pos1.x + " " + pos1.y + " 1000");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
}