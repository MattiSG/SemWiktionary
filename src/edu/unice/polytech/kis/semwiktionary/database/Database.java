/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*
*@version 0.0.0
*/

package edu.unice.polytech.kis.semwiktionary.database;


import edu.unice.polytech.kis.semwiktionary.model.Word;


/**Singleton class to access the unique SemWiktionary database.
*/
public class Database {
	/**Singleton unique instance.
	*/
	private Database instance;
	
	/**Singleton constructor, therefore private.
	*/
	private Database() {
		//TODO
	}
	
	/**Singleton accessor.
	*/
	public static Database getInstance() {
		if (! Database.instance)
			Database.instance = new Database();
		
		return Database.instance;
	}
	
	
	public void addWord(Word word) {
		//TODO	
	}
}
