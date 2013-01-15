// Christopher Gonzales
// 10/19/2012
//
// This program administers a game of Assassin by creating a list of alive assassins with a given
// file a names. The class performs tasks and moves killed assassins into a dead list until a 
// single alive assassin emerges as the winner.

import java.util.*;

public class AssassinAdmin {
	private AssassinNode killRing;		// reference to the front of the kill ring
	private AssassinNode graveYard;		// reference to the front of the graveyard
	
	// pre : names must be a non-empty List of Strings (throws IllegalArgumentException if not)
	// post: constructs an AssassinAdmin object with a kill ring list with, and in the same order 
	//       as, the given file of names
	public AssassinAdmin(List<String> names) {
		if (names.isEmpty()) 
			throw new IllegalArgumentException("names list is empty");
	
		AssassinNode prevAssassin = null;
		for (int i = names.size() - 1; i >= 0; i--) {
			String name = names.get(i);
			AssassinNode currAssassin = new AssassinNode(name, prevAssassin);
			killRing = currAssassin;
			prevAssassin = currAssassin;
		}
	}

	// post: prints the names of the people in the kill ring and who they are stalking. If there
	//       is only one person, it reports that they are stalking themselves.
	void printKillRing() {
		AssassinNode current = killRing;
		while (current.next != null) {
			System.out.println("    " + current.name + " is stalking " + current.next.name);
			current = current.next;
		}
		System.out.println("    " +current.name + " is stalking " + killRing.name);
	}

	// post: prints the names of dead people & their killer in the graveyard. Reported in reverse 
	//       kill order, meaning the most recently killed is printed first. If graveyard is empty
	//       no output is produced.
	void printGraveyard() {
		AssassinNode current = graveYard;
		while (current != null) {
			System.out.println("    " + current.name + " was killed by " + current.killer);
			current = current.next;
		}
	}
	
	// post: returns true if given name is currently in the kill ring. False otherwise. Method
	//       is case insensitive 
	boolean killRingContains(String name) {
		return contains(killRing, name);
	}
	
	// post: returns true if given name is currently in the graveyard. False otherwise. Method
	//       is case insensitive 
	boolean graveyardContains(String name) {
		return contains(graveYard, name);
	}

	// post: returns true if given name is currently in the given list. False otherwise. Method
	//       is case insensitive 
	boolean contains(AssassinNode list, String name) {
		AssassinNode current = list;
		while (current != null) {
			if (current.name.compareToIgnoreCase(name) == 0) 
				return true;
			current = current.next;
		}
		return false;
	}
	
	// post: returns true if there is only one player in the kill ring. False otherwise. 
	boolean gameOver() {
		return killRing.next == null;
	}
	
	// post: returns name of winner if game is over. Otherwise, returns null 
	String winner () {
		if (gameOver()) 
			return killRing.name;
		return null;
	}
	
	// pre : Kill ring contains given name (throws IllegalArgumentException if not). Game is not
	//       over (throws IllegalStateException if is)
	// post: moves player with given name from the kill ring to the graveyard. Method is case 
	//       insensitive
	void kill(String name) {
		if (!killRingContains(name)) 
			throw new IllegalArgumentException();
		if (gameOver()) 
			throw new IllegalStateException();
		
		AssassinNode current = killRing;
		// Check if victim is the first node
		if (current.name.compareToIgnoreCase(name) == 0) {
			// Find killer's name in last node
			while (current.next != null) 
				current = current.next;
			String killer = current.name;
			
			// MOVE FIRST NODE TO GRAVEYARD
			AssassinNode temp = killRing;
			temp.killer = killer;
			killRing = killRing.next;
			temp.next = graveYard;
			graveYard = temp; 
		} else { 
			while (current.next != null) {
				if (current.next.name.compareToIgnoreCase(name) == 0) {
					AssassinNode temp = current.next;
					temp.killer = current.name;
					current.next = current.next.next;
					temp.next = graveYard;
					graveYard = temp;
				} else {
					current = current.next;
				}
			}
		}
	}
}
