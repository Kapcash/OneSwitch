package com.iut.oneswitch.application.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Class redéfinissant la class ListPreference.
 * Permet une mise à jour automatique du "summary" d'une liste
 * en fonction de la valeur choisie.
 * ! Uniquement utilisée dans les .xml de preferences. !
 * @author Florent
 */
public class AutoSummaryListPreference extends ListPreference {
	 
	/**
	 * Constructeur appelant le second constructeur
	 * @param context Context de l'application, 
	 */
	public AutoSummaryListPreference(Context context) {
        this(context, null);
    }
 
	/**
	 * Constructeur appelant le constructeur de la superclass
	 * @param context Context de l'application
	 * @param attrs List des attributs
	 */
    public AutoSummaryListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            setSummary(getSummary());
        }
    }
 
    @Override
    public CharSequence getSummary() {
        int pos = findIndexOfValue(getValue());
        return getEntries()[pos];
    }

    @Override
    public void setValue(final String value) {
        super.setValue(value);
        notifyChanged();
    }
}
